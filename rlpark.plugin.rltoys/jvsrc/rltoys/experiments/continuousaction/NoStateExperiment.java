package rltoys.experiments.continuousaction;

import rltoys.algorithms.representations.actions.Action;
import rltoys.environments.envio.control.ControlLearner;
import rltoys.environments.envio.observations.TRStep;
import rltoys.environments.envio.problems.RLProblem;
import rltoys.math.normalization.IncMeanVarNormalizer;
import rltoys.math.vector.implementations.PVector;
import zephyr.plugin.core.api.monitoring.annotations.Monitor;

public class NoStateExperiment {
  @Monitor
  public final ControlLearner control;
  private TRStep step;
  private PVector x_t = null;
  @Monitor
  protected double reward;
  @Monitor
  protected final IncMeanVarNormalizer averageReward;
  private final RLProblem environment;

  public NoStateExperiment(RLProblem environnment, ControlLearner control) {
    this.control = control;
    averageReward = new IncMeanVarNormalizer(1);
    this.environment = environnment;
    step = environnment.initialize();
  }

  public TRStep step() {
    reward = step.r_tp1;
    PVector x_tp1 = new PVector(step.o_tp1);
    Action a_tp1 = control.step(x_t, step.a_t, x_tp1, step.r_tp1);
    step = environment.step(a_tp1);
    averageReward.update(step.r_tp1);
    x_t = x_tp1;
    return step;
  }

  static public double evaluateActorCritic(int nbTimeSteps, RLProblem environment, ControlLearner actorCritic) {
    NoStateExperiment experiment = new NoStateExperiment(environment, actorCritic);
    for (int t = 0; t < nbTimeSteps; t++)
      experiment.step();
    return experiment.averageReward.mean();
  }
}
