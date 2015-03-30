/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import base.*;

/**
 *
 * @author gdelorme
 */
public class Route {
    
    private Descripteur descripteur;
    private Noeud source;
    private Noeud destination;
    private int distance;
    private int nbSegments;
    
    public static int nbRoutes = 0;
    public static int lgTotale = 0;
    
    public Route (Noeud source, Noeud destination, int distance, int nbSegments, Descripteur descripteur){
        this.descripteur = descripteur;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.nbSegments = nbSegments;
        lgTotale += distance;
        nbRoutes++;
    }

    /**60.000
     * @return the descripteur
     */
    public Descripteur getDescripteur() {
        return descripteur;
    }

    /**
     * @param descripteur the descripteur to set
     */
    public void setDescripteur(Descripteur descripteur) {
        this.descripteur = descripteur;
    }

    /**
     * @return the source
     */
    public Noeud getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Noeud source) {
        this.source = source;
    }

    /**
     * @return the destination
     */
    public Noeud getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(Noeud destination) {
        this.destination = destination;
    }

    /**
     * @return the distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * @return the nbSegments
     */
    public int getNbSegments() {
        return nbSegments;
    }

    /**
     * @param nbSegments the nbSegments to set
     */
    public void setNbSegments(int nbSegments) {
        this.nbSegments = nbSegments;
    }
}
