package core ;

import java.awt.Color;
import java.io.* ;
import java.util.*;

import base.Readarg ;

public class Connexite extends Algo {
	
	private int origineAuto;
	private int originePieton;
	private int destination;
	private float dureeMax;
	private float variationAuto;
	private static boolean useBus = Math.random() > 0.5 ? true : false;
	private static int vitessePieton = 4;
	private boolean isBestAlgo;
	private float normalTimeAuto;
	private float maxTimeAuto;
	
	private Label[] labelsAuto;
	private Label[] labelsPieton;
	private Label[] labelsDestination;
	
	private int nbExplores;
	
    public Connexite(Graphe gr, PrintStream sortie, Readarg readarg) {
    	super(gr, sortie, readarg) ;
    	origineAuto = readarg.lireInt ("Numero du sommet d'origine de l'auto ? ") ;
    	originePieton = readarg.lireInt ("Numero du sommet d'origine du pieton ? ") ;
    	destination = readarg.lireInt ("Numero du sommet de destination ? ") ;
    	dureeMax = readarg.lireFloat ("Duree max de marche ? ") ;
    	variationAuto = readarg.lireFloat ("Variation max du temps de trajet de l'auto (en %) ? ") ;
    	isBestAlgo = (1 == readarg.lireInt ("Algo standard ou ameliore ? (0 ou 1) ? "));
    }

    public void run() {
    	nbExplores = 0;
    	System.out.println("********************************************************************************************************************");
    	if (useBus) {
    		System.out.println("LE PIETON PREND lE BUS");
    	} else {
    		System.out.println("LE PIETON EST A PIED");
    	}
    	long startTime = System.currentTimeMillis();
    	int result = isBestAlgo ? best() : standard() ;
    	long endTime = System.currentTimeMillis() - startTime;
    	
    	System.out.println("Duree de l'operation : "+endTime+" ms");
    	System.out.println("Noeuds explores : "+nbExplores);
    	if (result != -1) {
	    	System.out.println("Duree a pied (ou bus) du trajet pour le pieton : "+labelsPieton[result].getCout()+" min");
	    	System.out.println("Duree totale du trajet pour le pieton : "+(labelsPieton[result].getCout()+labelsDestination[result].getCout())+" min");
	    	System.out.println("Duree totale du trajet pour l'automobiliste sans covoiturage : "+normalTimeAuto+" min");
	    	float timeAuto = (labelsAuto[result].getCout()+labelsDestination[result].getCout());
	    	System.out.println("Duree totale du trajet pour l'automobiliste : "+timeAuto+" min (+"+(timeAuto-normalTimeAuto)*100/normalTimeAuto+" %)");
	    	printChemin(origineAuto, result, labelsAuto, Color.BLACK);
	    	printChemin(originePieton, result, labelsPieton, Color.BLUE);
	    	printCheminReverse(destination, result, labelsDestination, Color.ORANGE);
    	} else {
    		System.out.println("Aucun covoiturage n'a ete trouve.");
    	}
    	System.out.println("********************************************************************************************************************");
}
    
    private int standard() {
    	labelsPieton = DijPower(originePieton, false);
    	labelsAuto = DijPower(origineAuto, false);
    	labelsDestination = DijPower(destination, false);
    	normalTimeAuto = labelsAuto[destination].getCout();
    	maxTimeAuto = normalTimeAuto+normalTimeAuto*variationAuto/100;
    	float coutMin = Float.POSITIVE_INFINITY;
    	int indexMin = -1;
    	for (int i=0; i<graphe.getNoeuds().size(); i++) {
    		float cout =labelsPieton[i].getCout()+labelsAuto[i].getCout()+labelsDestination[i].getCout();
    		float coutAuto = labelsAuto[i].getCout()+labelsDestination[i].getCout();
    		nbExplores++;
    		if (cout<coutMin && coutAuto < maxTimeAuto){
    			coutMin = cout;
    			indexMin = i;
			}
		}
    	return indexMin;
    }
    
    private int best() {
    	normalTimeAuto = DijPower(origineAuto, true)[destination].getCout();
    	maxTimeAuto = normalTimeAuto+normalTimeAuto*variationAuto/100;
    	labelsAuto = new Label[graphe.getNoeuds().size()];
    	labelsPieton = new Label[graphe.getNoeuds().size()];
    	labelsDestination = new Label[graphe.getNoeuds().size()];
    	BinaryHeap<Label> heapAuto = new BinaryHeap<Label>() ;
    	BinaryHeap<Label> heapPieton = new BinaryHeap<Label>() ;
    	BinaryHeap<Label> heapDestination = new BinaryHeap<Label>() ;
    	int returnValue = -1;
    	for (int i = 0; i < graphe.getNoeuds().size(); i++) {
    		labelsAuto[i]= new Label(false, Float.POSITIVE_INFINITY, 0, null, graphe.getNoeuds().get(i)) ;
    		labelsPieton[i]= new Label(false, Float.POSITIVE_INFINITY, 0, null, graphe.getNoeuds().get(i)) ;
    		labelsDestination[i]= new Label(false, Float.POSITIVE_INFINITY, 0, null, graphe.getNoeuds().get(i)) ;
    	}
    	labelsAuto[origineAuto].setCout(0);
    	labelsPieton[originePieton].setCout(0);
    	labelsDestination[destination].setCout(0);
    	heapAuto.insert(labelsAuto[origineAuto]);
    	heapPieton.insert(labelsPieton[originePieton]);
    	heapDestination.insert(labelsDestination[destination]);
    	Label labelPieton = labelsPieton[originePieton], labelAuto = labelsAuto[origineAuto], labelDestination = labelsDestination[destination];
    	while (!heapAuto.isEmpty() || !heapPieton.isEmpty() || !heapDestination.isEmpty()) {
    		labelPieton = getLabel(labelPieton, heapPieton, Color.MAGENTA);
    		labelAuto = getLabel(labelAuto, heapAuto, Color.GREEN);
    		labelDestination = getLabel(labelDestination, heapDestination, Color.RED);
    		if (labelsAuto[labelPieton.getCourant().getId()].isMarquage() && labelsDestination[labelPieton.getCourant().getId()].isMarquage()) {
    			returnValue = labelPieton.getCourant().getId();
    			if (labelsAuto[returnValue].getCout()+labelsDestination[returnValue].getCout() < maxTimeAuto)
    				break;
    		} else if (labelsAuto[labelDestination.getCourant().getId()].isMarquage() && labelsPieton[labelDestination.getCourant().getId()].isMarquage()) {
    			returnValue = labelDestination.getCourant().getId();
    			if (labelsAuto[returnValue].getCout()+labelsDestination[returnValue].getCout() < maxTimeAuto)
    				break;
    		} else if (labelsPieton[labelAuto.getCourant().getId()].isMarquage() && labelsDestination[labelAuto.getCourant().getId()].isMarquage()) {
    			returnValue = labelAuto.getCourant().getId();
    			if (labelsAuto[returnValue].getCout()+labelsDestination[returnValue].getCout() < maxTimeAuto)
    				break;
    		} else {
	    		boucle(labelPieton, heapPieton, labelsPieton, originePieton);
    			boucle(labelAuto, heapAuto, labelsAuto, origineAuto);
    			boucle(labelDestination, heapDestination, labelsDestination, destination);
    		}
    		returnValue = -1;
    	}
    	return returnValue;
    }
    
    private Label[] DijPower(int origine, boolean withDest) {
    	boolean isPieton = (origine == originePieton);
    	boolean isAuto = (origine == origineAuto);
    	Color color;
    	if (isPieton) {
    		color = Color.MAGENTA;
    	} else if (isAuto) {
    		color = Color.GREEN;
    	} else {
    		color = Color.RED;
		}
    	Label[] labels = new Label[graphe.getNoeuds().size()];
    	BinaryHeap<Label> heap = new BinaryHeap<Label>() ;
    	for (int i = 0; i < labels.length; i++) {
    		labels[i]= new Label(false, Float.POSITIVE_INFINITY, 0, null, graphe.getNoeuds().get(i)) ;
    	}
    	labels[origine].setCout(0);
		heap.insert(labels[origine]) ;
		Label label = labels[origine];
    	while (!heap.isEmpty()) {
    		label = getLabel(label, heap, color);
    		if (withDest && label.getCourant().getId() == destination) { break ;}
    		boucle(label, heap, labels, origine);
    	}
    	return labels;
    }
    
    private Label getLabel(Label label, BinaryHeap<Label> heap, Color color) {
    	if (!heap.isEmpty()) {
    		nbExplores++;
    		label = heap.deleteMin();
    		label.setMarquage(true);
    		graphe.getDessin().setColor(color);
    		graphe.getDessin().drawPoint(label.getCourant().getLongitude(),label.getCourant().getLatitude(), 3);
		}
    	return label;
    }
    
    private void boucle(Label label, BinaryHeap<Label> heap, Label[] labels, int origine) {
    	boolean isPieton = (origine == originePieton);
    	boolean isDestination = (origine == destination);
    	Label lebal;
    	float temps;
    	ArrayList<Route> routes = isDestination ? label.getCourant().getRoutesReverse() : label.getCourant().getRoutes();
		for (Route route : routes) {
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
    
    public void printChemin(int origine,int destination, Label[] labels, Color col) {
    	Chemin chemin = new Chemin();
    	int e = destination;
		while (e != origine) {
			chemin.addNoeudFirst(labels[e].getCourant());
			e = labels[e].getPere().getId();
		}
		chemin.addNoeudFirst(labels[e].getCourant());
		chemin.trace(graphe.getDessin(),col);
    }
    
    public void printCheminReverse(int origine,int destination, Label[] labels, Color col) {
    	Chemin chemin = new Chemin();
    	int e = destination;
		while (e != origine) {
			chemin.addNoeud(labels[e].getCourant());
			e = labels[e].getPere().getId();
		}
		chemin.addNoeud(labels[e].getCourant());
		chemin.trace(graphe.getDessin(),col);
    }
}
