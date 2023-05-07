package Katha.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentListener;
import aima.core.agent.impl.SimpleEnvironmentView;
import aima.core.environment.vacuum.VacuumPercept;

public class KathaTrivialVacuumDemo {

    public static void main(String[] args) {
        // create environment with random state of cleaning.
        Environment<VacuumPercept, Action> env = new VacuumEnvironment();
        EnvironmentListener<Object, Object> view = new SimpleEnvironmentView();
        env.addEnvironmentListener(view);

        Agent<VacuumPercept, Action> agent;
        agent = new KathaModelBasedReflexVacuumAgent();
        // agent = new ReflexVacuumAgent();
        // agent = new SimpleReflexVacuumAgent();
        // agent = new TableDrivenVacuumAgent();

        env.addAgent(agent);
        env.step(16);
        env.notify("Performance=" + env.getPerformanceMeasure(agent));
    }

}
