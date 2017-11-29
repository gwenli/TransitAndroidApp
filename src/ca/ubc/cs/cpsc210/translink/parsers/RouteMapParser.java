package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    String[] all = line.split(";");
                    try {double d=Double.parseDouble(all[0]);
                        posn=endposn+1;}
                    catch (NumberFormatException e){
                        if (!line.startsWith("N")||!all[0].contains("-")||
                                (all[0].contains("-")
                                        &&all[0].substring(all[0].indexOf("-")+1,all[0].indexOf("-")+2).equals(";")))
                        {
                            posn = endposn + 1;
                        }
                        else
                        {parseOnePattern(line);
                            posn = endposn + 1;
                        }}}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str
     */
    private void parseOnePattern(String str) {

        // try {double d=Double.parseDouble(all[0]);}
        //     catch (NumberFormatException e){



        String routeNumber;
        String patternName;
        List<LatLon> points=new ArrayList<>();
        int posnOfDash=str.indexOf("-");
        routeNumber=str.substring(1,posnOfDash);
        int firstSC=str.indexOf(";");
        if(firstSC<0){
            patternName=str.substring(posnOfDash+1);
            storeRouteMap(routeNumber,patternName,points);
        }
        else {
            patternName=str.substring(posnOfDash+1,firstSC);
            int lastSC=str.lastIndexOf(";");
            if (firstSC==lastSC){
                storeRouteMap(routeNumber,patternName,points);
            }

            else{
                String latLons=str.substring(firstSC+1);

                String[] speratedLatLons=latLons.split(";");
                for (int t=0;t<speratedLatLons.length;t=t+2){
                    LatLon singlePoint=
                            new LatLon(Double.parseDouble(speratedLatLons[t]),
                                    Double.parseDouble(speratedLatLons[t+1]));

                    points.add(singlePoint);

                }
                storeRouteMap(routeNumber,patternName,points);}}

    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber       the number of the route
     * @param patternName       the name of the pattern
     * @param elements          the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}
