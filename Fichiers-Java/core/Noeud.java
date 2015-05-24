package core ;

/**
 *   Classe representant un noeud.
 */

import java.util.*;

public class Noeud {

    private float longitude;
    private float latitude;
    private int nbSuccesseurs;
    private ArrayList<Route> routes;
    private ArrayList<Route> routesReverse;
    private int id;
    
    public static int nbNoeuds = 0;

    public Noeud(int id, float longitude, float latitude, int nbSuccesseurs) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nbSuccesseurs = nbSuccesseurs;
        routes = new ArrayList<Route>();
        routesReverse = new ArrayList<Route>();
        nbNoeuds++;
    }
    
    private boolean routeIsExist(Route r) {
        return routes.contains(r);
    }
    
    private boolean routeReverseIsExist(Route r) {
        return routesReverse.contains(r);
    }

    public void addRoute(Route route) {
        if (!routeIsExist(route))
            routes.add(route);
    }
    
    public ArrayList<Route> getRoutes() {
        return routes;
    }
    
    public void addRouteReverse(Route route) {
        if (!routeReverseIsExist(route))
            routesReverse.add(route);
    }
    
    public ArrayList<Route> getRoutesReverse() {
        return routesReverse;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }     

    /**
     * @return the nbSuccesseurs
     */
    public int getNbSuccesseurs() {
        return nbSuccesseurs;
    }

    /**
     * @param nbSuccesseurs the nbSuccesseurs to set
     */
    public void setNbSuccesseurs(int nbSuccesseurs) {
        this.nbSuccesseurs = nbSuccesseurs;
    }

	@Override
	public String toString() {
		return "id=" + id;
	}
}
