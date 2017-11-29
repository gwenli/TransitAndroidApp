package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when JSON data does not have expected format
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is not an array </li>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        JSONArray jStops = new JSONArray(jsonResponse);
        for (int t = 0; t < jStops.length(); t++) {
            JSONObject jStop = jStops.getJSONObject(t);
            parseStop(jStop);
        }
    }

    public void parseStop(JSONObject jStop) throws JSONException, StopDataMissingException{
        if (!jStop.has("StopNo"))
            throw new StopDataMissingException("No Stop Number!!");
        int stopNumber=jStop.getInt("StopNo");
        if (!jStop.has("Name"))
            throw new StopDataMissingException("No Stop Name!!");
        String name = jStop.getString("Name");
        if (!jStop.has("Latitude"))
            throw new StopDataMissingException("No Stop Latitude!!");
        double lat=jStop.getDouble("Latitude");
        if (!jStop.has("Longitude"))
            throw new StopDataMissingException("No Stop Longitude!!");
        double lon = jStop.getDouble("Longitude");
        LatLon location = new LatLon(lat,lon);
        if (!jStop.has("Routes"))
            throw new StopDataMissingException("No Stop Routes!!");
        storeStop(stopNumber,name,location);
        Stop s=StopManager.getInstance().getStopWithId(stopNumber);
        String sRoutes = jStop.getString("Routes");
        String[] seperatedSRoutes = sRoutes.split(", ");
        // Set<Route> routes= new HashSet<>();
        for (int t=0; t<seperatedSRoutes.length;t++){
            Route r=RouteManager.getInstance().getRouteWithNumber(seperatedSRoutes[t]);
            //    routes.add(r);
            r.addStop(s);
            s.addRoute(r);
        }



    }
    public void storeStop(int number, String name, LatLon location){
        Stop s=StopManager.getInstance().getStopWithId(number,name,location);



    }
}
