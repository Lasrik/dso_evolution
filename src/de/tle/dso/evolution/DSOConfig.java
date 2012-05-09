package de.tle.dso.evolution;

import de.tle.dso.evolution.mutations.AddOneChromosomToAnotherMutation;
import de.tle.dso.evolution.mutations.EraseChromosomMutation;
import de.tle.evolution.*;
import de.tle.evolution.mutation.DecrementMutation;
import de.tle.evolution.mutation.IncrementMutation;
import de.tle.evolution.mutation.Mutation;
import de.tle.evolution.mutation.SwitchMutation;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class DSOConfig extends Configuration {

  public static final String PATTERN = "60SL 100WL";
  public static final int NUMBER_OF_CHILDREN = 100;
  public static final int NUMBER_OF_PARENTS = 2;
  public static final int PROPABILITY_OF_MUTATION = 60;
  public static final int POPULATION_SIZE = 200;
  public static final int BATTLE_LOST_MALUS = 10000;
  public static final int MAX_PLAYER_ARMY_SIZE = 250;
  public static final int SIMULATE_ROUNDS = 500;
  public static final int GENOM_SIZE = 7;
  public static final int MIN_RUNS = 50;
  public static final int MAX_RUNS = 1000;
  protected Individual bestCandidateSoFar = null;
  protected int numberOfSuccessiveRunsWithoutImprovement = 0;
  protected DSOFactory factory = new DSOFactory(this);
  protected DSOFitnessFunction fitnessFunction = new DSOFitnessFunction(this);
  protected Selector selector = new FitnessSelector(this);
  protected GeneticOperator geneticOperator = new CrossOverOperator();
  protected List<Mutation> mutations = new LinkedList<Mutation>();
  protected Validator validator = new DSOValidator(this);
  protected Comparator<Individual> comparator = new FitnessComparator();
  private Mapper mapper = new Mapper();
  private Logger log;

  public DSOConfig() {
    mutations.add(new IncrementMutation());
    mutations.add(new DecrementMutation());
    mutations.add(new SwitchMutation());
    mutations.add(new SwitchMutation());
    mutations.add(new SwitchMutation());
    mutations.add(new SwitchMutation());
    mutations.add(new AddOneChromosomToAnotherMutation(this));
    mutations.add(new EraseChromosomMutation(this));

    log = Logger.getLogger(getClass());
  }

  @Override
  public int getNumberOfChildren() {
    return NUMBER_OF_CHILDREN;
  }

  @Override
  public int getNumberOfParents() {
    return NUMBER_OF_PARENTS;
  }

  @Override
  public int getPopulationSize() {
    return POPULATION_SIZE;
  }

  @Override
  public int getPropabilityOfMutation() {
    return PROPABILITY_OF_MUTATION;
  }

  @Override
  public FitnessFunction getFitnessFunction() {
    return fitnessFunction;
  }

  @Override
  public Selector getSelector() {
    return selector;
  }

  @Override
  public GeneticOperator getRecombinationOperator() {
    return geneticOperator;
  }

  @Override
  public List<Mutation> getMutations() {
    return mutations;
  }

  @Override
  public Factory getFactory() {
    return factory;
  }

  @Override
  public Comparator<Individual> getFitnessComparator() {
    return comparator;
  }

  @Override
  public boolean terminationCriteriaMet(Population population) {
    Individual currentBestCandidate = population.getIndividuals().get(0);
    if (currentBestCandidate.equals(bestCandidateSoFar)) {
      numberOfSuccessiveRunsWithoutImprovement++;
    } else {
      bestCandidateSoFar = currentBestCandidate;
      numberOfSuccessiveRunsWithoutImprovement = 0;
    }

    log.info(population.getAge() + " " + population);

    return numberOfSuccessiveRunsWithoutImprovement > MIN_RUNS || population.getAge() >= MAX_RUNS;
  }

  public Mapper getMapper() {
    return this.mapper;
  }
}