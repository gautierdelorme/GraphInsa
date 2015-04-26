/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.awt.Color;
import java.util.ArrayList;

import base.Dessin;

/**
 *
 * @author gdelorme
 */
public class Chemin {
    private ArrayList<Noeud> noeuds;
    
    public Chemin() {
        noeuds = new ArrayList<Noeud>();
    }
    
    public void addNoeud(Noeud n) {
        noeuds.add(n);
    }
    
    public void addNoeudFirst(Noeud n) {
        noeuds.add(0,n);
    }
    
    public int coutDistance() {
        int returnCout = 0;
        for (int i = 0; i < noeuds.size()-1; i++) {            
            int min = Integer.MAX_VALUE;
            Route rMin = null;
            for (Route r : noeuds.get(i).getRoutes()) {
                if (r.getDestination() == noeuds.get(i+1) && r.getDistance() < min) {
                   min = r.getDistance();
                   rMin = r;
                }
            }
            if (rMin != null) {
                returnCout += rMin.getDistance();
            }
        }
        return returnCout;
    }
    
    public float coutTemps() {
        float returnCout = 0;
        for (int i = 0; i < noeuds.size()-1; i++) {            
            int min = Integer.MAX_VALUE;
            Route rMin = null;
            for (Route r : noeuds.get(i).getRoutes()) {
                if (r.getDestination() == noeuds.get(i+1) && r.getDistance() < min) {
                   min = r.getDistance();
                   rMin = r;
                }
            }
            if (rMin != null) {
                returnCout += 60.0*(float)rMin.getDistance()/(1000.0*(float)rMin.getDescripteur().vitesseMax());
            }
        }
        return returnCout;
    }
    
    public void trace(Dessin dessin) {
    	for (int i = 0; i < noeuds.size()-1; i++) {            
            int min = Integer.MAX_VALUE;
            Route rMin = null;
            for (Route r : noeuds.get(i).getRoutes()) {
                if (r.getDestination() == noeuds.get(i+1) && r.getDistance() < min) {
                   min = r.getDistance();
                   rMin = r;
                }
            }
            if (rMin != null) {
            	dessin.setColor(Color.blue);
            	dessin.drawLine(rMin.getSource().getLongitude(), rMin.getSource().getLatitude(), rMin.getDestination().getLongitude(), rMin.getDestination().getLatitude());
            }
        }
    }
}
