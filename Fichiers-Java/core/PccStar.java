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
    	int nbExplores = 0;
    	float cout;
    	for (int i = 0; i < labels.length; i++) {
    		labels[i]= new Label(false, Float.POSITIVE_INFINITY, 0, null, graphe.getNoeuds().get(i)) ;
    	}
    	labels[origine].setCout(0);
    	heap.insert(labels[origine]);
    	while (!heap.isEmpty()) {
    		label = heap.deleteMin();
    		if (label.getCourant().getId() == destination) { break ;}
			nbExplores++;
			for (int i = 0; i < label.getCourant().getNbSuccesseurs(); i++) {
				Route route = label.getCourant().getRoutes().get(i);
				cout = label.getCout()+(inTime ? (60*route.getDistance()/(1000*route.getDescripteur().vitesseMax())) : route.getDistance());
				lebal = labels[route.getDestination().getId()];
				if (lebal.getCout() > cout) {
					lebal.setCout(cout);
					lebal.setPere(label.getCourant());
					if (heap.contains(lebal)) {
						heap.reorganizeFrom(lebal);
					} else {
						lebal.setEstimation(graphe.getNoeuds().get(destination));
						heap.insert(lebal);
					}
				}
			}
    	}
    	
    	long endTime = System.currentTimeMillis() - startTime;
    	printResult(nbExplores, endTime);
    }
}
