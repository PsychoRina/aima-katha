package Katha.vacuum;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleAgent;
import aima.core.environment.vacuum.*;
import aima.core.search.agent.NondeterministicSearchAgent;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.util.Tasks;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
import aima.gui.fx.views.SimpleEnvironmentViewCtrl;
import aima.gui.fx.views.VacuumEnvironmentViewCtrl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;

public class KathaVacuumAgentApp extends IntegrableApplication{

    public static void main(String[] args) {
        launch(args);
    }

    public final static String PARAM_ENV = "environment";
    public final static String PARAM_AGENT = "agent";

    protected TaskExecutionPaneCtrl taskPaneCtrl;
    protected SimpleEnvironmentViewCtrl<VacuumPercept, Action> envViewCtrl;
    protected aima.core.environment.vacuum.VacuumEnvironment env = null;
    protected SimpleAgent<VacuumPercept, Action> agent = null;

    @Override
    public String getTitle() {
        return "Vacuum Agent App";
    }

    /**
     * Defines state view, parameters, and call-back functions and calls the
     * simulation pane builder to create layout and controller objects.
     */
    @Override
    public Pane createRootPane() {
        BorderPane root = new BorderPane();

        StackPane envView = new StackPane();
        envViewCtrl = new VacuumEnvironmentViewCtrl(envView, action -> {
            if (action == aima.core.environment.vacuum.VacuumEnvironment.ACTION_MOVE_LEFT) return 270.0;
            else if (action == aima.core.environment.vacuum.VacuumEnvironment.ACTION_MOVE_RIGHT) return 90.0;
            else if (action == MazeVacuumEnvironment.ACTION_MOVE_UP) return 0.0;
            else if (action == MazeVacuumEnvironment.ACTION_MOVE_DOWN) return 180.0;
            else return null;
        });

        List<Parameter> params = createParameters();

        TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
        builder.defineParameters(params);
        builder.defineStateView(envView);
        builder.defineInitMethod(this::initialize);
        builder.defineTaskMethod(this::startExperiment);
        taskPaneCtrl = builder.getResultFor(root);

        return root;
    }

    protected List<Parameter> createParameters() {
        Parameter p1 = new Parameter(PARAM_ENV, "Katha Environment");
        Parameter p2 = new Parameter(PARAM_AGENT,
                "Katha Vacuum Agent");
        return Arrays.asList(p1, p2);
    }

    /**
     * Is called after each parameter selection change.
     */
    @Override
    public void initialize() {
        switch (taskPaneCtrl.getParamValueIndex(PARAM_ENV)) {
            case 0:
                env = new VacuumEnvironment();
                break;
        }
        switch (taskPaneCtrl.getParamValueIndex(PARAM_AGENT)) {
            case 0:
                agent = new KathaModelBasedReflexVacuumAgent();
                break;
        }
        if (env != null && agent != null) {
            envViewCtrl.initialize(env);
            env.addEnvironmentListener(envViewCtrl);
            env.addAgent(agent);
        }
    }

    /**
     * Starts the experiment.
     */
    public void startExperiment() {
        if (agent instanceof NondeterministicSearchAgent) {
            NondeterministicProblem<VacuumEnvironmentState, Action> problem =
                    new NondeterministicProblem<>(env.getCurrentState(),
                            VacuumWorldFunctions::getActions, VacuumWorldFunctions.createResultsFunctionFor(agent),
                            VacuumWorldFunctions::testGoal, (s, a, sPrimed) -> 1.0);
            // Set the problem now for this kind of agent
            ((NondeterministicSearchAgent<VacuumPercept, VacuumEnvironmentState, Action>) agent).makePlan(problem);
        }
        while (!env.isDone() && !Tasks.currIsCancelled()) {
            env.step();
            taskPaneCtrl.setStatus("Performance=" + env.getPerformanceMeasure(agent));
            taskPaneCtrl.waitAfterStep();
        }
        envViewCtrl.notify("Performance=" + env.getPerformanceMeasure(agent));
    }

    @Override
    public void cleanup() {
        taskPaneCtrl.cancelExecution();
    }
}
