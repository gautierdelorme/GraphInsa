package core ;

import java.io.* ;
import base.Readarg ;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
    
    protected Label[] labels;
    protected BinaryHeap<Label> heap;
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;
	labels = new Label[gr.getNoeuds().size()];
	
	heap = new BinaryHeap<Label>() ;

	this.zoneOrigine = gr.getZone () ;
	this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

	// Demander la zone et le sommet destination.
	this.zoneOrigine = gr.getZone () ;
	this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }

    public void run() {
    	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		// A vous d'implementer la recherche de plus court chemin.
	
    	Dijkstra();
    }
    
    private void Dijkstra() {
    	Label label, lebal;
    	float distance;
    	for (int i = 0; i < labels.length; i++) {
    		labels[i]= new Label(false, Integer.MAX_VALUE, 0, null, graphe.getNoeuds().get(i)) ;
    		if (i == origine) {
    			labels[i].setCout(0);
    		}
    		heap.insert(labels[i]) ;
    	}
    	for (int i = 0; i < graphe.getRoutes().size(); i++) {
    		if (!graphe.getRoutes().get(i).getDescripteur().isSensUnique()) {
    			System.out.print("(DOUBLE SENS)");
    		}else {
    			System.out.print("(SENS UNIQUE)");
    		}
    		System.out.println(graphe.getRoutes().get(i));
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
