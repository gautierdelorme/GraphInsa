package core;

public class Label implements Comparable<Label> {

	private boolean marquage;
	private int cout;
	private Noeud pere;
	private Noeud courant;
	
	public Label(boolean marquage, int cout, Noeud pere, Noeud courant) {
		super();
		this.marquage = marquage;
		this.cout = cout;
		this.pere = pere;
		this.courant = courant;
	}
	
	
	
	public boolean isMarquage() {
		return marquage;
	}



	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}



	public int getCout() {
		return cout;
	}



	public void setCout(int cout) {
		this.cout = cout;
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
		Integer int1 = new Integer(cout);
		Integer int2 = new Integer(label.cout);
		return int1.compareTo(int2);
	}
	
	@Override
	public String toString() {
		return "Label [marquage=" + marquage + ", cout=" + cout +", courant=" + courant + "]";
	}
}
