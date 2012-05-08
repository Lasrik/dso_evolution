package de.tle.dso.evolution;

import de.tle.dso.resources.ResourceCost;
import de.tle.dso.sim.Simulation;
import de.tle.dso.sim.SimulationResult;
import de.tle.dso.sim.battle.InvalidArmyException;
import de.tle.dso.units.Army;
import de.tle.dso.units.Unit;
import de.tle.dso.units.util.UnitPatternHelper;
import de.tle.evolution.FitnessFunction;
import de.tle.evolution.Individual;
import de.tle.evolution.Population;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class DSOFitnessFunction extends FitnessFunction {

  public static final int INVALID_GENOM_MALUS = 1000000;
  protected DSOConfig config;

  public DSOFitnessFunction(DSOConfig config) {
    this.config = config;
  }

  @Override
  public void evaluate(Population population) {
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    for (Individual individual : population) {
      DSOFitnessRunner runner = new DSOFitnessRunner(individual);
      executor.submit(runner);
    }

    executor.shutdown();
    try {
      executor.awaitTermination(1, TimeUnit.DAYS);
    } catch (InterruptedException ex) {
      Logger.getLogger(getClass()).warn("DSO Runner liefen nicht zu Ende.", ex);
    }
  }

  @Override
  public void evaluate(Individual individual) {
    if (fitnessAlreadyCalculated(individual)) {
      return;
    }

    int fitness = 0;

    try {
      fitness = calculateFitness(individual);
    } catch (Exception ex) {
      // Ungültige Armee - maximaler Fitness Malus. Individuum ist nicht lebensfähig
      fitness = INVALID_GENOM_MALUS;
    }

    individual.setFitness(fitness);
  }

  private int calculateFitness(Individual individual) throws InvalidArmyException {
    SimulationResult simResult = simulateBattle(individual);
    individual.setCustomPayload(simResult);

    int fitness = resourceCostsForUnitsLostInBattle(simResult);

    if (fitness < 1000) {
      fitness += Math.log(buildCosts(individual));
    }

    return fitness;
  }

  private SimulationResult simulateBattle(Individual individual) throws InvalidArmyException {
    String attackingArmyPattern = config.getMapper().getPatternFromIndividual(individual);
    Simulation simulation = new Simulation(attackingArmyPattern, DSOConfig.PATTERN);
    simulation.setNumberOfRounds(DSOConfig.SIMULATE_ROUNDS);

    SimulationResult simResult = simulation.simulate();

    return simResult;
  }

  private boolean fitnessAlreadyCalculated(Individual individual) {
    return individual.isFitnessSet();
  }

  private int resourceCostsForUnitsLostInBattle(SimulationResult simResult) throws InvalidArmyException {
    int resourceCost = simResult.getMaxResourceCosts().totalWeightPoints();


    if (!simResult.isAlwaysWin()) {
      resourceCost += DSOConfig.BATTLE_LOST_MALUS;
    }

    return resourceCost;
  }

  private int buildCosts(Individual individual) {
    String pattern = config.getMapper().getPatternFromIndividual(individual);
    Army army = UnitPatternHelper.createArmyFromPattern(pattern);
    ResourceCost costsToBuildArmy = ResourceCost.fromArmy(army);
    return costsToBuildArmy.totalWeightPoints();
  }

  private class DSOFitnessRunner implements Runnable {

    private Individual individual;

    public DSOFitnessRunner(Individual individual) {
      this.individual = individual;
    }

    @Override
    public void run() {
      evaluate(individual);
    }
  }
}