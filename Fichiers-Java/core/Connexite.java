package core ;

import java.awt.Color;
import java.io.* ;

import base.Readarg ;

public class Connexite extends Algo {
	
	private int origineAuto;
	private int originePieton;
	private int destination;
	private float dureeMax;
	private float variationAuto;
	private static boolean useBus = Math.random() > 0.5 ? true : false;
	private static int vitessePieton = 25;
			
    public Connexite(Graphe gr, PrintStream sortie, Readarg readarg) {
    	super(gr, sortie, readarg) ;
    	origineAuto = readarg.lireInt ("Numero du sommet d'origine de l'auto ? ") ;
    	originePieton = readarg.lireInt ("Numero du sommet d'origine du pieton ? ") ;
    	destination = readarg.lireInt ("Numero du sommet de destination ? ") ;
    	dureeMax = readarg.lireFloat ("Duree max de marche ? ") ;
    	variationAuto = readarg.lireFloat ("Variation max du temps de trajet de l'auto (en %) ? ") ;
    }

    public void run() {
    	long startTime = System.currentTimeMillis();
    	
    	Label[] labelsPieton = DijPower(originePieton,true);
    	Label[] labelsAuto = DijPower(origineAuto,false);
    	Label[] labelsDestination = DijPower(destination,false);
    	
    	float normalTimeAuto = labelsAuto[destination].getCout();
    	float maxTimeAuto = normalTimeAuto+normalTimeAuto*variationAuto/100;
    	float coutMin = Float.POSITIVE_INFINITY;
    	int indexMin = 0;
    	boolean okay = false;
    	
    	for (int i=0; i<graphe.getNoeuds().size(); i++) {
    		float cout =labelsPieton[i].getCout()+labelsAuto[i].getCout()+labelsDestination[i].getCout();
    		float coutAuto = labelsAuto[i].getCout()+labelsDestination[i].getCout();
    		if (cout<coutMin && coutAuto < maxTimeAuto){
    			coutMin = cout;
    			indexMin = i;
    			okay = true;
			}
		}
    	
    	System.out.println("********************************************************************************************************************");
    	if (useBus) {
    		System.out.println("LE PIETON PREND lE BUS");
    	} else {
    		System.out.println("LE PIETON EST A PIED");
    	}
    	if (okay) {
	    	System.out.println("Duree de l'operation : "+(System.currentTimeMillis() - startTime)+" ms");
	    	System.out.println("Duree a pied (ou bus) du trajet pour le pieton : "+labelsPieton[indexMin].getCout()+" min");
	    	System.out.println("Duree totale du trajet pour le pieton : "+(labelsPieton[indexMin].getCout()+labelsDestination[indexMin].getCout())+" min");
	    	System.out.println("Duree totale du trajet pour l'automobiliste sans covoiturage : "+normalTimeAuto+" min");
	    	float timeAuto = (labelsAuto[indexMin].getCout()+labelsDestination[indexMin].getCout());
	    	System.out.println("Duree totale du trajet pour l'automobiliste : "+timeAuto+" min (+"+(timeAuto-normalTimeAuto)*100/normalTimeAuto+" %)");
	    	
	    	printChemin(origineAuto, indexMin, labelsAuto, Color.blue);
	    	printChemin(originePieton, indexMin, labelsPieton, Color.green);
	    	printChemin(destination, indexMin, labelsDestination, Color.pink);
    	} else {
    		System.out.println("Aucun covoiturage n'a ete trouve.");
    	}
    	System.out.println("********************************************************************************************************************");
    }
    
    private Label[] DijPower(int origine, boolean isPieton) {
    	Label[] labels = new Label[graphe.getNoeuds().size()];
    	BinaryHeap<Label> heap = new BinaryHeap<Label>() ;
    	Label label, lebal;
    	float temps;
    	for (int i = 0; i < labels.length; i++) {
    		labels[i]= new Label(false, Float.POSITIVE_INFINITY, 0, null, graphe.getNoeuds().get(i)) ;
    	}
    	labels[origine].setCout(0);
		heap.insert(labels[origine]) ;
    	while (!heap.isEmpty()) {
    		label = heap.deleteMin();
			for (int i = 0; i < label.getCourant().getNbSuccesseurs(); i++) {
				Route route = label.getCourant().getRoutes().get(i);
				if (isPieton && !useBus) {
					temps = route.getDescripteur().vitesseMax() >= 110 ? Float.POSITIVE_INFINITY : label.getCout()+60*route.getDistance()/(1000*vitessePieton);
				} else {
					temps = label.getCout()+60*route.getDistance()/(1000*route.getDescripteur().vitesseMax());
				}
				lebal = labels[route.getDestination().getId()];
				if (lebal.getCout() > temps && (!isPieton || temps <= dureeMax || useBus)) {
					lebal.setCout(temps);
					lebal.setPere(label.getCourant());
					if (heap.contains(lebal))
						heap.reorganizeFrom(lebal);
					else
						heap.insert(lebal);
				}
			}
    	}
    	return labels;
    }
    
    public void printChemin(int origine,int destination, Label[] labels, Color col) {
    	Chemin chemin = new Chemin();
    	int e = destination;
		while (e != origine) {
			chemin.addNoeudFirst(labels[e].getCourant());
			labels[e].setMarquage(true);
			e = labels[e].getPere().getId();
		}
		chemin.addNoeudFirst(labels[e].getCourant());
		chemin.trace(graphe.getDessin(),col);
    }
}
