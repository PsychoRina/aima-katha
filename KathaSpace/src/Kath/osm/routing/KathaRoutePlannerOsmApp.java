package Kath.osm.routing;

import aimax.osm.gui.fx.applications.RoutePlannerOsmApp;
import aimax.osm.routing.RouteCalculator;

public class KathaRoutePlannerOsmApp extends RoutePlannerOsmApp {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public String getTitle() {
        return "Katha OSM Route Planner App";
    }

    @Override
    protected RouteCalculator createRouteCalculator() {
        return new KathaRouteCalculator();
    }
}
