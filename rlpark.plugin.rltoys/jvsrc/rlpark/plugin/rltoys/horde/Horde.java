package rlpark.plugin.rltoys.horde;

import java.util.ArrayList;
import java.util.List;

import rlpark.plugin.rltoys.envio.actions.Action;
import rlpark.plugin.rltoys.envio.observations.Observation;
import rlpark.plugin.rltoys.horde.demons.Demon;
import rlpark.plugin.rltoys.horde.demons.DemonScheduler;
import rlpark.plugin.rltoys.horde.functions.GammaFunction;
import rlpark.plugin.rltoys.horde.functions.HordeUpdatable;
import rlpark.plugin.rltoys.horde.functions.OutcomeFunction;
import rlpark.plugin.rltoys.horde.functions.RewardFunction;
import rlpark.plugin.rltoys.math.vector.RealVector;
import zephyr.plugin.core.api.labels.Labels;
import zephyr.plugin.core.api.monitoring.annotations.LabelProvider;
import zephyr.plugin.core.api.monitoring.annotations.Monitor;

@Monitor
public class Horde {
  final private List<HordeUpdatable> functions = new ArrayList<HordeUpdatable>();
  final private List<Demon> demons = new ArrayList<Demon>();
  private final DemonScheduler demonScheduler;

  public Horde() {
    this(new DemonScheduler());
  }

  public Horde(DemonScheduler demonScheduler) {
    this.demonScheduler = demonScheduler;
  }

  public Horde(List<Demon> demons, List<RewardFunction> rewardFunctions, List<OutcomeFunction> outcomeFunctions,
      List<GammaFunction> gammaFunctions) {
    this();
    this.demons.addAll(demons);
    addFunctions(rewardFunctions);
    addFunctions(outcomeFunctions);
    addFunctions(gammaFunctions);
  }

  @LabelProvider(ids = { "demons" })
  String demonLabel(int i) {
    return Labels.label(demons.get(i));
  }

  @LabelProvider(ids = { "functions" })
  String functionLabel(int i) {
    return Labels.label(functions.get(i));
  }

  private void addFunctions(List<?> functions) {
    if (functions == null)
      return;
    for (Object function : functions)
      this.functions.add((HordeUpdatable) function);
  }

  public void update(Observation o_tp1, RealVector x_t, Action a_t, RealVector x_tp1) {
    for (HordeUpdatable function : functions)
      function.update(o_tp1, x_t, a_t, x_tp1);
    demonScheduler.update(demons, x_t, a_t, x_tp1);
  }

  public List<HordeUpdatable> functions() {
    return functions;
  }

  public List<Demon> demons() {
    return demons;
  }

  public boolean addFunction(HordeUpdatable function) {
    return functions.add(function);
  }

  public boolean addDemon(Demon demon) {
    return demons.add(demon);
  }
}
