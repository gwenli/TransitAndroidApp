package ca.ubc.cs.cpsc210.translink.model;

import java.util.*;
import java.util.List;

/**
 * Represents a bus route with a route number, name, list of stops, and list of RoutePatterns.
 * <p/>
 * Invariants:
 * - no duplicates in list of stops
 * - iterator iterates over stops in the order in which they were added to the route
 */



public class Route implements Iterable<Stop> {
    private String routeNumber;
    private String name;
    private List<Stop> stops;
    private List<RoutePattern> routePatterns;

    /**
     * Constructs a route with given number.
     * List of stops is empty.
     *
     * @param number the route number
     */
    public Route(String number) {
        routeNumber=number;
        stops=new ArrayList<>();
        routePatterns=new ArrayList<>();


    }

    /**
     * Return the number of the route
     *
     * @return the route number
     */
    public String getNumber() {
        return routeNumber;
    }

    /**
     * Set the name of the route
     * @param name  The name of the route
     */
    public void setName(String name) {
        this.name=name;

    }

    /**
     * Add the pattern to the route if it is not already there
     *
     * @param pattern
     */
    public void addPattern(RoutePattern pattern) {
        if (pattern==null)
        {}
        if (!routePatterns.contains(pattern))
            routePatterns.add(pattern);
        pattern.setRoute(this);

    }

    /**
     * Add stop to route.  Stops must not be duplicated in a route.
     *
     * @param stop the stop to add to this route
     */
    public void addStop(Stop stop) {
        if (stop==null)
        {}
        if (!stops.contains(stop)){
            stops.add(stop);
            stop.addRoute(this);}

    }

    /**
     * Remove stop from route
     *
     * @param stop the stop to remove from this route
     */
    public void removeStop(Stop stop) {
        stops.remove(stop);
        stop.removeRoute(this);

    }

    /**
     * Return all the stops in this route, in the order in which they were added
     *
     * @return      A list of all the stops
     */
    public List<Stop> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /**
     * Determines if this route has a stop at a given stop
     *
     * @param stop the stop
     * @return true if route has a stop at given stop
     */
    public boolean hasStop(Stop stop) {
        return stops.contains(stop);
    }

    /**
     * Two routes are equal if their numbers are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o==null)
            return false;
        if (getClass()!=o.getClass())
            return false;
        Route r=(Route) o;
        return (routeNumber.equals(r.routeNumber));


    }

    /**
     * Two routes are equal if their numbers are equal.
     * Therefore hashCode only pays attention to number.
     */
    @Override
    public int hashCode() {
        return routeNumber.hashCode()*31;
    }

    @Override
    public Iterator<Stop> iterator() {
        // Do not modify the implementation of this method!
        return stops.iterator();
    }

    /**
     * Return the name of this route
     *
     * @return      the name of the route
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Route " + getNumber();
    }

    /**
     * Return the pattern with the given name. If it does not exist, then create it and add it to the patterns.
     * In either case, update the destination and direction of the pattern.
     *
     * @param patternName       the name of the pattern
     * @param destination       the destination of the pattern
     * @param direction         the direction of the pattern
     * @return                  the pattern with the given name
     */
    public RoutePattern getPattern(String patternName, String destination, String direction) {
        //RoutePattern p=new RoutePattern(patternName, destination,direction,this);
        //return p;
        // List<String> names= new ArrayList<>();
        // for (RoutePattern r:routePatterns) {
        //   names.add(r.getName());

        //}
        //if (names.contains(patternName)){
        // for (RoutePattern r:routePatterns){
        //  if (r.getName()==patternName){
        //      r.setDirection(direction);
        //     r.setDestination(destination);
        //  }
        // }}

        //  return  new RoutePattern(patternName, destination,direction,this);
        RoutePattern r= new RoutePattern(patternName,destination,direction,this);
        if (routePatterns.contains(r))
        {int i=routePatterns.indexOf(r);
            routePatterns.get(i).setDestination(destination);
            routePatterns.get(i).setDirection(direction);
            return routePatterns.get(i);
        }
        else {routePatterns.add(r);
            return r;}



    }
    public void setRoutePatterns(List<RoutePattern> routePatterns){
        this.routePatterns=routePatterns;
        for (RoutePattern rp:routePatterns){
            if (rp.getRoute()==null)
                rp.setRoute(this);
        }
    }

    /**
     * Return the pattern with the given name. If it does not exist, then create it and add it to the patterns
     * with empty strings as the destination and direction for the pattern.
     *
     * @param patternName       the name of the pattern
     * @return                  the pattern with the given name
     */
    public RoutePattern getPattern(String patternName) {
        //List<String> names= new ArrayList<>();
        //for (RoutePattern r:routePatterns) {
        //  names.add(r.getName());
        // }
        //for (RoutePattern r:routePatterns) {
        //    if (r.getName()==patternName){
        RoutePattern r= new RoutePattern(patternName,"","",this);
        int t =routePatterns.indexOf(r);
        if (t>=0){
            RoutePattern ans=routePatterns.get(t);
            return ans;}
        else {
            routePatterns.add(r);
            return r;
        }

    }

    /**
     * Return all the patterns for this route as a list
     *
     * @return      a list of the patterns for this route
     */

    public List<RoutePattern> getPatterns() {
        return Collections.unmodifiableList(routePatterns);
    }
}
