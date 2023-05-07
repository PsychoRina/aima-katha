package Kath.osm.routing;

import aima.core.search.framework.Node;
import aima.core.search.framework.problem.Problem;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.OsmMap;
import aimax.osm.data.entities.MapNode;
import aimax.osm.routing.OsmMoveAction;
import aimax.osm.routing.OsmSldHeuristicFunction;
import aimax.osm.routing.RouteCalculator;
import aimax.osm.routing.RouteFindingProblem;

import java.util.function.ToDoubleFunction;

public class KathaRouteCalculator extends RouteCalculator {
    /** Returns the names of all supported way selection options. */
    @Override
    public String[] getTaskSelectionOptions() {
        return new String[] { "Distance", "Distance (Car)", "Distance (Bike)", "Time (Car)", "Fun (Cyclist)" };
    }
    @Override
    protected MapWayFilter createMapWayFilter(OsmMap map, int taskSelection) {
        if (taskSelection == 1 || taskSelection == 3)
            return MapWayAttFilter.createCarWayFilter();
        else if (taskSelection == 2 || taskSelection == 4)
            return MapWayAttFilter.createBicycleWayFilter();
        else
            return MapWayAttFilter.createAnyWayFilter();
    }
    @Override
    protected Problem<MapNode, OsmMoveAction> createProblem(MapNode[] pNodes, OsmMap map,
                                                            MapWayFilter wayFilter, boolean ignoreOneways, int taskSelection) {
        if(taskSelection == 3) {
            return new RouteFindingProblem(pNodes[0], pNodes[1], wayFilter,
                    ignoreOneways, TimeStepCostFunction::getDistanceStepCostsCar);
        }else if(taskSelection == 4){
            return new RouteFindingProblem(pNodes[0], pNodes[1], wayFilter,
                    ignoreOneways, TimeStepCostFunction::getDistanceStepCostCycle);
        }else{
            return new RouteFindingProblem(pNodes[0], pNodes[1], wayFilter,
                    ignoreOneways);
        }
    }
    /** Factory method, responsible for heuristic function creation. */
    protected ToDoubleFunction<Node<MapNode, OsmMoveAction>> createHeuristicFunction(MapNode[] pNodes,
                                                                                     int taskSelection) {
        if(taskSelection == 3 || taskSelection == 4){
            return new TimeHeuristicFunction();
        }else{
            return new OsmSldHeuristicFunction(pNodes[1]);
        }
    }
    static class TimeHeuristicFunction implements ToDoubleFunction<Node<MapNode, OsmMoveAction>>{

        @Override
        public double applyAsDouble(Node<MapNode, OsmMoveAction> value) { //Node beinhaltet Variable pathCost, unser Node heisst value
            return value.getPathCost();
        }
    }
    static class TimeStepCostFunction{

        public static double getDistanceStepCostsCar(MapNode state, OsmMoveAction action, MapNode statePrimed) {
           String streettype = action.getWay().getAttributeValue("highway");
           int speed;
           if(streettype.equals("secondary") || streettype.equals("residential")){
               speed = 30;
           }
           else if(streettype.equals("primary")){
               speed = 50;
           }else{
               speed=100;
           }
            return action.getTravelDistance()/speed;
        }
        public static double getDistanceStepCostCycle(MapNode state, OsmMoveAction action, MapNode statePrimed){
            String streettype = action.getWay().getAttributeValue("highway");
            String bicycleallowed = action.getWay().getAttributeValue("bicycle");
            int fun;
            if(streettype == null){
                fun = 214390;
            }else if(streettype.equals("cycleway")){
                fun = 1;
            }else if(streettype.equals("footway")){
                fun = 8;
            }else if(streettype.equals("secondary")){
                fun = 16734;
            }else{
                fun = 20000000;
            }
            if(bicycleallowed != null){
                if(bicycleallowed.equals("no")){
                    fun = Integer.MAX_VALUE;
                }
            }
            return fun;
        }

    }

}
