package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {


    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when JSON response does not have expected format
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        JSONArray jRoutes = new JSONArray(jsonResponse);
        int i=stop.getNumOfArrivals();
        for (int t = 0; t < jRoutes.length(); t++) {
            JSONObject jRoute = jRoutes.getJSONObject(t);
            if (jRoute.has("Schedules")){
                if (jRoute.getJSONArray("Schedules").length()==0) {

                } else {
                    parseArrivalsInEachRoute(jRoute,stop);
                }
            }
        }
        if (stop.getNumOfArrivals()==i)

            throw new ArrivalsDataMissingException();

    }
    public static void parseArrivalsInEachRoute(JSONObject jRoute,Stop stop)
            throws ArrivalsDataMissingException{

        try {
            Route route=RouteManager.getInstance().getRouteWithNumber(jRoute.getString("RouteNo"));
            JSONArray arrivals = jRoute.getJSONArray("Schedules");
            // if (arrivals.length()==0)
            for (int t = 0; t < arrivals.length(); t++) {
                JSONObject arrival = arrivals.getJSONObject(t);
                if (arrival.has("ExpectedCountdown") && arrival.has("Destination") && arrival.has("ScheduleStatus")) {
                    parseArrival(arrival, stop, route);
                }


            }
        }catch (JSONException e) {

        }


    }

    private static void parseArrival(JSONObject arrival, Stop stop, Route route)
            throws JSONException,ArrivalsDataMissingException {

        int timeToArrival=arrival.getInt("ExpectedCountdown");
        String destination = arrival.getString("Destination");
        String status= arrival.getString("ScheduleStatus");
        Arrival a =new Arrival(timeToArrival,destination,route);
        a.setStatus(status);
        Route r=RouteManager.getInstance().getRouteWithNumber(route.getNumber());
        stop.addRoute(r);
        a.setRoute(r);
        stop.addArrival(a);
        a.setRoute(r);
    }
}
