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
				if (inTime)
					cout = label.getCout()+(60*route.getDistance()/(1000*route.getDescripteur().vitesseMax()));
				else
					cout = label.getCout()+route.getDistance();
				lebal = labels[route.getDestination().getId()];
				if (lebal.getCout() > cout) {
					lebal.setCout(cout);
					lebal.setPere(label.getCourant());
					if (heap.contains(lebal)) {
						heap.reorganizeFrom(lebal);
					} else {
						lebal.setEstimation((float)Graphe.distance(lebal.getCourant().getLongitude(), lebal.getCourant().getLatitude(), graphe.getNoeuds().get(destination).getLongitude(), graphe.getNoeuds().get(destination).getLatitude()));
						heap.insert(lebal);
					}
				}
			}
    	}
    	long endTime = System.currentTimeMillis() - startTime;
    	System.out.println("********************************************************************************************************************");
    	if (labels[destination].getCout() != Float.POSITIVE_INFINITY) {
    		int e = destination;
    		System.out.print("Trajet le plus court (cout total en");
    		if (inTime)
    			System.out.print(" min : ");
    		else
    			System.out.print(" m : ");
    		System.out.print(labels[destination].getCout()+")"); // : "+labels[e].getCourant().getId());
    		Chemin chemin = new Chemin();
    		while (e != origine) {
    			chemin.addNoeudFirst(labels[e].getCourant());
    			labels[e].setMarquage(true);
    			nbMarques++;
    			//System.out.print(" <- "+labels[e].getPere().getId());
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
    	System.out.println("Duree de l'operation : "+(endTime)+" ms");
    	System.out.println("********************************************************************************************************************");
    }
}
