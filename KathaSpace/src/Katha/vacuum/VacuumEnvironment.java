package Katha.vacuum;

import java.util.Arrays;
import java.util.List;

public class VacuumEnvironment extends aima.core.environment.vacuum.VacuumEnvironment {
    //Orientierung Zeile 73
    public static final List<String> location = Arrays.asList("Loc1","Loc2","Loc3","Loc4","Loc5","Loc6","Loc7","Loc8");
    public static final LocationState[] states = {LocationState.Clean, LocationState.Dirty, LocationState.Clean, LocationState.Dirty, LocationState.Clean, LocationState.Dirty, LocationState.Clean, LocationState.Dirty}; //we define when its dirty or not for each location
    public VacuumEnvironment(){
        super(location, states);
    }
}
