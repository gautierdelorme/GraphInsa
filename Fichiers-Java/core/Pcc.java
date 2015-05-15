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
    public static boolean inTime ;
        
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		labels = new Label[gr.getNoeuds().size()];
		heap = new BinaryHeap<Label>() ;
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
		Pcc.inTime = (1 == readarg.lireInt ("En distance ou temps ? (0 ou 1) ? "));
    }

    public void run() {
    	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;	
    	Dijkstra();
    }
    
    private void Dijkstra() {
    	long startTime = System.currentTimeMillis();
    	Label label, lebal;
    	int nbExplores = 0;
    	float cout;
    	for (int i = 0; i < labels.length; i++) {
    		labels[i]= new Label(false, Float.POSITIVE_INFINITY, 0, null, graphe.getNoeuds().get(i)) ;
    	}
		labels[origine].setCout(0);
		heap.insert(labels[origine]) ;
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
					if (heap.contains(lebal))
						heap.reorganizeFrom(lebal);
					else
						heap.insert(lebal);
				}
			}
		}
    	long endTime = System.currentTimeMillis() - startTime;
    	printResult(nbExplores, endTime);
    }
    
    protected void printResult(int nbExplores, long endTime) {
    	System.out.println("********************************************************************************************************************");
    	int nbMarques = 0;
    	if (labels[destination].getCout() != Float.POSITIVE_INFINITY) {
    		int e = destination;
    		System.out.print("Trajet le plus court (cout total en");
    		if (inTime)
    			System.out.print(" min : ");
    		else
    			System.out.print(" m : ");
    		System.out.print(labels[destination].getCout()+")");// : "+labels[e].getCourant().getId());
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
    		if (inTime)
    			System.out.println("cout en min du chemin : "+chemin.coutTemps());
    		else
    			System.out.println("cout en m du chemin : "+chemin.coutDistance());
    	} else {
    		System.out.println("Les noeuds "+origine+" et "+destination+" ne sont pas relies !");
    	}
    	System.out.println("Nb explores : "+nbExplores);
    	System.out.println("Nb marques : "+nbMarques);
    	System.out.println("Duree de l'operation : "+(endTime)+" ms");
    	System.out.println("********************************************************************************************************************");
    }
}
