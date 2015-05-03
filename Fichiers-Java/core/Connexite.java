package core ;

import java.awt.Color;
import java.io.* ;

import base.Readarg ;

public class Connexite extends Algo {
	
	private int origineAuto;
	private int originePieton;
	private int destination;
			
    public Connexite(Graphe gr, PrintStream sortie, Readarg readarg) {
    	super(gr, sortie, readarg) ;
    	origineAuto = readarg.lireInt ("Numero du sommet d'origine de l'auto ? ") ;
    	originePieton = readarg.lireInt ("Numero du sommet d'origine du pieton ? ") ;
    	destination = readarg.lireInt ("Numero du sommet de destination ? ") ;
    }

    public void run() {
    	
    	covoit();
    }
    
    public void covoit() {
    	Label[] labelsAuto = Dijkstra(origineAuto,false);
    	Label[] labelsPieton = Dijkstra(originePieton,true);
    	Label[] labelsDestination = Dijkstra(destination,false);
    	
    	float coutMin = 0;
    	int indexMin = 0;
    	
    	for (int i=0; i<graphe.getNoeuds().size(); i++) {
    		float cout =labelsPieton[i].getCout()+labelsAuto[i].getCout()+labelsDestination[i].getCout();
    		if (i == 0 || cout<coutMin){
    			coutMin = cout;
    			indexMin = i;
			}
		}
    	getChemin(origineAuto, indexMin, labelsAuto, Color.blue);
    	getChemin(originePieton, indexMin, labelsPieton, Color.green);
    	getChemin(destination, indexMin, labelsDestination, Color.pink);
    	
    }
    
    private Label[] Dijkstra(int origine, boolean isPieton) {
    	Label[] labels = new Label[graphe.getNoeuds().size()];
    	BinaryHeap<Label> heap = new BinaryHeap<Label>() ;
    	Label label, lebal;
    	float temps;
    	for (int i = 0; i < labels.length; i++) {
    		labels[i]= new Label(false, Float.MAX_VALUE, 0, null, graphe.getNoeuds().get(i)) ;
    		if (i == origine) {
    			labels[i].setCout(0);
    		}
    		heap.insert(labels[i]) ;
    	}
    	while (!heap.isEmpty()) {
    		label = heap.deleteMin();
    		if (label.getCout() < Float.MAX_VALUE) {
    			for (int i = 0; i < label.getCourant().getNbSuccesseurs(); i++) {
    				Route route = label.getCourant().getRoutes().get(i);
    				if (isPieton) {
    					temps = route.getDescripteur().vitesseMax() >= 110 ? Float.MAX_VALUE : label.getCout()+60*route.getDistance()/(1000*30);
    				} else {
    					temps = label.getCout()+60*route.getDistance()/(1000*route.getDescripteur().vitesseMax());
					}
    				lebal = labels[route.getDestination().getId()];
    				if (lebal.getCout() > temps) {
    					lebal.setCout(temps);
    					lebal.setPere(label.getCourant());
    					heap.reorganizeFrom(lebal);
    				}
				}
    		}
    	}
    	return labels;
    }
    
    public void getChemin(int origine,int destination, Label[] labels, Color col) {
    	Chemin chemin = new Chemin();
    	int e = destination;
		while (e != origine) {
			chemin.addNoeudFirst(labels[e].getCourant());
			labels[e].setMarquage(true);
			System.out.print(" <- "+labels[e].getPere().getId());
			e = labels[e].getPere().getId();
		}
		chemin.addNoeudFirst(labels[e].getCourant());
		chemin.trace(graphe.getDessin(),col);
    }
}
