package core ;

import java.io.* ;

import base.Readarg ;

public class PccStar extends Pcc {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;
    }

    public void run() {

	System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	// A vous d'implementer la recherche de plus court chemin A*
	aStar();
    }
    
    private void aStar() {
    	Label label, lebal;
    	float distance;
    	for (int i = 0; i < labels.length; i++) {
    		float diff = (float)Math.sqrt(Math.pow(graphe.getNoeuds().get(destination).getLatitude()-graphe.getNoeuds().get(i).getLatitude(),2)
    				+ Math.pow(graphe.getNoeuds().get(destination).getLongitude()-graphe.getNoeuds().get(i).getLongitude(),2));
    		//System.out.println("calcul : "+diff);
    		labels[i]= new Label(false, Integer.MAX_VALUE, diff, null, graphe.getNoeuds().get(i)) ;
    		if (i == origine) {
    			labels[i].setCout(0);
    		}
    		heap.insert(labels[i]) ;
    	}
    	while (!heap.isEmpty()) {
    		label = heap.deleteMin();
    		if (label.getCout() < Integer.MAX_VALUE) {
    			for (int i = 0; i < label.getCourant().getNbSuccesseurs(); i++) {
    				Route route = label.getCourant().getRoutes().get(i);
    				distance = label.getCout()+route.getDistance();
    				lebal = labels[route.getDestination().getId()];
    				if (lebal.getCout() > distance) {
    					lebal.setCout(distance);
    					lebal.setPere(label.getCourant());
    					heap.reorganizeSince(lebal);
    				}
				}
    		}
    	}
    	System.out.println("********************************************************************************************************************");
    	if (labels[destination].getCout() != Integer.MAX_VALUE) {
    		int e = destination;
    		System.out.print("Trajet le plus court (distance totale : "+labels[destination].getCout()+") : "+labels[e].getCourant().getId());
    		Chemin chemin = new Chemin();
    		while (e != origine) {
    			chemin.addNoeudFirst(labels[e].getCourant());
    			labels[e].setMarquage(true);
    			System.out.print(" <- "+labels[e].getPere().getId());
    			e = labels[e].getPere().getId();
    		}
    		chemin.addNoeudFirst(labels[e].getCourant());
    		chemin.trace(graphe.getDessin());
    		System.out.println();
    		System.out.println("cout en distance du chemin : "+chemin.coutDistance());
    	} else {
    		System.out.println("Les noeuds "+origine+" et "+destination+" ne sont pas relies !");
    	}
    	System.out.println("********************************************************************************************************************");
    }
}
