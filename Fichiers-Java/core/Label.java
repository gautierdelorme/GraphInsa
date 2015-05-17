package core;

public class Label implements Comparable<Label> {

	private boolean marquage;
	private float cout;
	private float estimation;
	private Noeud pere;
	private Noeud courant;
	
	public Label(boolean marquage, float cout, float estimation, Noeud pere, Noeud courant) {
		super();
		this.marquage = marquage;
		this.cout = cout;
		this.pere = pere;
		this.courant = courant;
		this.estimation = Pcc.inTime ? estimation/(1000*130/60) : estimation;
	}
	
	
	
	public boolean isMarquage() {
		return marquage;
	}



	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}



	public float getCout() {
		return cout;
	}


	public void setCout(float cout) {
		this.cout = cout;
	}
	
	public float getEstimation() {
		return estimation;
	}

	public void setEstimation(float estimation) {
		this.estimation = Pcc.inTime ? estimation/(1000*130/60) : estimation;
	}

	public void setEstimation(Noeud destination) {
		setEstimation((float)Graphe.distance(courant.getLongitude(), courant.getLatitude(), destination.getLongitude(), destination.getLatitude()));
	}

	public Noeud getPere() {
		return pere;
	}



	public void setPere(Noeud pere) {
		this.pere = pere;
	}



	public Noeud getCourant() {
		return courant;
	}



	public void setCourant(Noeud courant) {
		this.courant = courant;
	}



	@Override
	public int compareTo(Label label) {
		return (cout+estimation) - (label.cout+label.estimation) < 0 ? -1 : 1;
	}
	
	@Override
	public String toString() {
		return "Label [marquage=" + marquage + ", cout=" + cout +", courant=" + courant + "]";
	}
}
