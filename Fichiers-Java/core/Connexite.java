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
	
	private float normalTimeAuto;
	private float maxTimeAuto;
	
	private Label[] labelsAuto;
	private Label[] labelsPieton;
	private Label[] labelsDestination;
	private BinaryHeap<Label> heapAuto;
	private BinaryHeap<Label> heapPieton;
	private BinaryHeap<Label> heapDestination;
	private int nbExplores;
    public Connexite(Graphe gr, PrintStream sortie, Readarg readarg) {
    	super(gr, sortie, readarg) ;
    	origineAuto = readarg.lireInt ("Numero du sommet d'origine de l'auto ? ") ;
    	originePieton = readarg.lireInt ("Numero du sommet d'origine du pieton ? ") ;
    	destination = readarg.lireInt ("Numero du sommet de destination ? ") ;
    	dureeMax = readarg.lireFloat ("Duree max de marche ? ") ;
    	variationAuto = readarg.lireFloat ("Variation max du temps de trajet de l'auto (en %) ? ") ;
    }

    public void run() {
    	/*long startTime = System.currentTimeMillis();
    	labelsPieton = DijPower(originePieton,true);
    	labelsAuto = DijPower(origineAuto,false);
    	labelsDestination = DijPower(destination,false);
    	
    	normalTimeAuto = labelsAuto[destination].getCout();
    	maxTimeAuto = normalTimeAuto+normalTimeAuto*variationAuto/100;
    	float coutMin = Float.POSITIVE_INFINITY;
    	int indexMin = 0;
    	boolean okay = false;
    	
    	for (int i=0; i<graphe.getNoeuds().size(); i++) {
    		float cout =labelsPieton[i].getCout()+labelsAuto[i].getCout()+labelsDestination[i].getCout();
    		float coutAuto = labelsAuto[i].getCout()+labelsDestination[i].getCout();
    		nbExplores++;
    		if (cout<coutMin && coutAuto < maxTimeAuto){
    			coutMin = cout;
    			indexMin = i;
    			okay = true;
			}
		}
    	long endTime = System.currentTimeMillis() - startTime;
    	System.out.println("********************************************************************************************************************");
    	if (useBus) {
    		System.out.println("LE PIETON PREND lE BUS");
    	} else {
    		System.out.println("LE PIETON EST A PIED");
    	}
    	System.out.println("Duree de l'operation : "+(endTime)+" ms");
    	System.out.println("Noeuds explores : "+nbExplores);
    	if (okay) {
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
    	*/
    	System.out.println("********************************************************************************************************************");
    	if (useBus) {
    		System.out.println("LE PIETON PREND lE BUS");
    	} else {
    		System.out.println("LE PIETON EST A PIED");
    	}
    	nbExplores = 0;
    	long startTime = System.currentTimeMillis();
    	int result = best();
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
	    	//printChemin(result, destination, labelsDestination, Color.ORANGE);
	    	printChemin(destination, result, labelsDestination, Color.ORANGE);
    	} else {
    		System.out.println("Aucun covoiturage n'a ete trouve.");
    	}
    	System.out.println("********************************************************************************************************************");
    }
    
    private int best() {
    	normalTimeAuto = DijPower(origineAuto);
    	maxTimeAuto = normalTimeAuto+normalTimeAuto*variationAuto/100;
    	labelsAuto = new Label[graphe.getNoeuds().size()];
    	labelsPieton = new Label[graphe.getNoeuds().size()];
    	labelsDestination = new Label[graphe.getNoeuds().size()];
    	heapAuto = new BinaryHeap<Label>() ;
    	heapPieton = new BinaryHeap<Label>() ;
    	heapDestination = new BinaryHeap<Label>() ;
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
    	Label labelPieton = heapPieton.findMin(), labelAuto = heapAuto.findMin(), labelDestination = heapDestination.findMin();
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
	    		boucle(labelPieton, heapPieton, labelsPieton, true);
    			boucle(labelAuto, heapAuto, labelsAuto, false);
    			boucle(labelDestination, heapDestination, labelsDestination, false);
    		}
    		returnValue = -1;
    	}
    	return returnValue;
    }
    
    private Label[] DijPower(int origine, boolean isPieton) {
    	if (origine == originePieton) {
    		System.out.println("Start Pieton !");
			graphe.getDessin().setColor(Color.MAGENTA);
    	} else if (origine == origineAuto) {
    		System.out.println("Start Auto !");
			graphe.getDessin().setColor(Color.GREEN);
    	} else {
    		System.out.println("Start Destination !");
			graphe.getDessin().setColor(Color.RED);
		}
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
    		nbExplores++;
    		graphe.getDessin().drawPoint(label.getCourant().getLongitude(),label.getCourant().getLatitude(), 3);
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
    
    private float DijPower(int origine) {
		graphe.getDessin().setColor(Color.LIGHT_GRAY);
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
    		nbExplores++;
    		if (label.getCourant().getId() == destination) { break ;}
    		graphe.getDessin().drawPoint(label.getCourant().getLongitude(),label.getCourant().getLatitude(), 3);
			for (int i = 0; i < label.getCourant().getNbSuccesseurs(); i++) {
				Route route = label.getCourant().getRoutes().get(i);
				temps = label.getCout()+60*route.getDistance()/(1000*route.getDescripteur().vitesseMax());
				lebal = labels[route.getDestination().getId()];
				if (lebal.getCout() > temps) {
					lebal.setCout(temps);
					lebal.setPere(label.getCourant());
					if (heap.contains(lebal))
						heap.reorganizeFrom(lebal);
					else
						heap.insert(lebal);
				}
			}
    	}
    	return labels[destination].getCout();
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
    
    private void boucle(Label label, BinaryHeap<Label> heap, Label[] labels, boolean isPieton) {
    	Label lebal;
    	float temps;
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
}
