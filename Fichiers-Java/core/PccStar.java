package core ;

import java.io.* ;

import base.Readarg ;

public class PccStar extends Pcc {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
    	super(gr, sortie, readarg) ;
    }

    public void run() {

	System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	aStar();
    }
    
    public void aStar() {
    	long startTime = System.currentTimeMillis();
    	Label label, lebal;
    	int nbMarques = 0, nbExplores = 0;
    	float temps;
    	for (int i = 0; i < labels.length; i++) {
    		float diff = (float)Math.sqrt(Math.pow(graphe.getNoeuds().get(destination).getLatitude()-graphe.getNoeuds().get(i).getLatitude(),2)
    				+ Math.pow(graphe.getNoeuds().get(destination).getLongitude()-graphe.getNoeuds().get(i).getLongitude(),2));
    		labels[i]= new Label(false, Float.MAX_VALUE, diff, null, graphe.getNoeuds().get(i)) ;
    		if (i == origine) {
    			labels[i].setCout(0);
    		}
    		heap.insert(labels[i]) ;
    	}
    	while (!heap.isEmpty()) {
    		label = heap.deleteMin();
    		if (label.getCourant().getId() == destination) { break ;}
    		if (label.getCout() < Float.MAX_VALUE) {
    			nbExplores++;
    			for (int i = 0; i < label.getCourant().getNbSuccesseurs(); i++) {
    				Route route = label.getCourant().getRoutes().get(i);
    				temps = label.getCout()+60*route.getDistance()/(1000*route.getDescripteur().vitesseMax());
    				lebal = labels[route.getDestination().getId()];
    				if (lebal.getCout() > temps) {
    					lebal.setCout(temps);
    					lebal.setPere(label.getCourant());
    					heap.reorganizeFrom(lebal);
    				}
				}
    		}
    	}
    	System.out.println("********************************************************************************************************************");
    	if (labels[destination].getCout() != Float.MAX_VALUE) {
    		int e = destination;
    		System.out.print("Trajet le plus court (temps total en min : "+labels[destination].getCout()+") : "+labels[e].getCourant().getId());
    		Chemin chemin = new Chemin();
    		while (e != origine) {
    			chemin.addNoeudFirst(labels[e].getCourant());
    			labels[e].setMarquage(true);
    			nbMarques++;
    			System.out.print(" <- "+labels[e].getPere().getId());
    			e = labels[e].getPere().getId();
    		}
    		chemin.addNoeudFirst(labels[e].getCourant());
    		chemin.trace(graphe.getDessin());
    		System.out.println();
    		System.out.println("cout en temps du chemin : "+chemin.coutTemps());
    	} else {
    		System.out.println("Les noeuds "+origine+" et "+destination+" ne sont pas relies !");
    	}
    	System.out.println("Nb explores : "+nbExplores);
    	System.out.println("Nb marques : "+nbMarques);
    	System.out.println("Duree de l'operation : "+(System.currentTimeMillis() - startTime)+" ms");
    	System.out.println("********************************************************************************************************************");
    }
}
