package Katha.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Model;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.SimpleAgent;
import aima.core.agent.impl.aprog.ModelBasedReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.ANDCondition;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.NOTCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;
import aima.core.environment.vacuum.VacuumPercept;


import java.util.LinkedHashSet;
import java.util.Set;

import static aima.core.environment.vacuum.VacuumEnvironment.*;
import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_MOVE_LEFT;

public class KathaModelBasedReflexVacuumAgent extends SimpleAgent<VacuumPercept, Action> {
    private static final String ATT_CURRENT_LOCATION = "currentLocation";
    private static final String ATT_CURRENT_STATE = "currentState";
    private static final String ATT_REACHEDRIGHTEND = "reachedrightend";
    private static final String ATT_REACHEDLEFTEND = "reachedleftend";

    public KathaModelBasedReflexVacuumAgent(){
        super(new ModelBasedReflexAgentProgram<VacuumPercept, Action>() {
            @Override
            protected void init() {
                setState(new DynamicState());
                setRules(getRuleSet());
            }

            @Override
            protected DynamicState updateState(DynamicState state, Action action, VacuumPercept percept, Model model) {
                Object currLocation = percept.getCurrLocation();
                Object currState = percept.getCurrState();
                state.setAttribute(ATT_CURRENT_LOCATION, currLocation);
                state.setAttribute(ATT_CURRENT_STATE, currState);
                if(currLocation.equals(VacuumEnvironment.location.get(VacuumEnvironment.location.size()-1))){
                    state.setAttribute(ATT_REACHEDRIGHTEND, true);
                }else if(currLocation.equals(VacuumEnvironment.location.get(0))){
                    state.setAttribute(ATT_REACHEDLEFTEND, true);
                }
                return state;
            }
        });
    }
    private static Set<Rule<Action>> getRuleSet() {
        Set<Rule<Action>> rules = new LinkedHashSet<>();
        rules.add(new Rule<>(new EQUALCondition(ATT_CURRENT_STATE, LocationState.Dirty), ACTION_SUCK));
        rules.add(new Rule<>(new ANDCondition(new EQUALCondition(ATT_REACHEDRIGHTEND, true),new EQUALCondition(ATT_REACHEDLEFTEND, true)), null));
        rules.add(new Rule<>(new EQUALCondition(ATT_REACHEDRIGHTEND, true), ACTION_MOVE_LEFT));
        rules.add(new Rule<>(new NOTCondition(new EQUALCondition(ATT_REACHEDRIGHTEND, true)), ACTION_MOVE_RIGHT));
        return rules;
    }
}


