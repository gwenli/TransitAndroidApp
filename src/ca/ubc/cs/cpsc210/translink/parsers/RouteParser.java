package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when JSON data does not have expected format,
     *                         JSON data is not an array
     * @throws RouteDataMissingException when
     * <ul>
     *  <li> JSON data is missing Name, StopNo, Routes or location elements for any route</li>
     * </ul>
     */

    public void parseRoutes(String jsonResponse) throws JSONException, RouteDataMissingException {
        JSONArray allRoutes = new JSONArray(jsonResponse);
        for (int t = 0; t < allRoutes.length(); t++) {
            JSONObject route = allRoutes.getJSONObject(t);
            parseRoute(route);
        }
    }

    public  void parseRoute(JSONObject route) throws JSONException, RouteDataMissingException{
        if (!route.has("RouteNo")||route.isNull("RouteNo"))
            throw new RouteDataMissingException("There is no RouteNo");
        String routeNumber=route.getString("RouteNo");
        if (!route.has("Name")||route.isNull("Name"))
            throw new RouteDataMissingException("There is no Name");
        String name=route.getString("Name");
        if (!route.has("Patterns")||route.isNull("Patterns"))
            throw new RouteDataMissingException("There is no Patterns");
        JSONArray patterns=route.getJSONArray("Patterns");
        List<RoutePattern> routePatterns=new ArrayList<>();
        for (int t=0;t<patterns.length();t++){
            JSONObject jPattern = patterns.getJSONObject(t);
            RoutePattern routePattern = parsePattern(jPattern);
            routePatterns.add(routePattern);
        }
        storeRoute(routeNumber,name,routePatterns);

    }
    public RoutePattern parsePattern(JSONObject pattern) throws JSONException,RouteDataMissingException{
        if (!pattern.has("PatternNo")||pattern.isNull("PatternNo"))
        { throw new RouteDataMissingException("There is no PatternNo/PatterName");}
        String name = pattern.getString("PatternNo");
        if (!pattern.has("Destination")||pattern.isNull("Destination"))
            throw new RouteDataMissingException("There is no Destination");
        String destination=pattern.getString("Destination");
        if (!pattern.has("Direction")||pattern.isNull("Direction"))
            throw new RouteDataMissingException("There is no Direction");
        String direction = pattern.getString("Direction");
        RoutePattern ans=new RoutePattern(name,destination,direction,null);
        return ans;

    }
    public void storeRoute(String routeNumber, String name, List<RoutePattern> routePatterns){
        Route r=RouteManager.getInstance().getRouteWithNumber(routeNumber,name);
        r.setRoutePatterns(routePatterns);

    }
    //public void storeRoutePattern(){

    //}
}
