package de.tle.dso.evolution;

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

  public static final String PATTERN = "150PL, 1ST";
  public static final int NUMBER_OF_CHILDREN = 10;
  public static final int NUMBER_OF_PARENTS = 2;
  public static final int POPULATION_SIZE = 50;
  public static final int LOSS_MALUS = 10000;
  protected int bestFitnessSoFar = Integer.MAX_VALUE;
  protected int numberOfSuccessiveRunsWithoutImprovement = 0;
  protected DSOFactory factory = new DSOFactory();
  protected DSOFitnessFunction fitnessFunction = new DSOFitnessFunction(this);
  protected Selector selector = new FitnessSelector(this);
  protected GeneticOperator geneticOperator = new CrossOverOperator();
  protected List<Mutation> mutations = new LinkedList<Mutation>();
  protected Validator validator = new DSOValidator(this);
  private Mapper mapper = new Mapper();
  private Logger log;

  public DSOConfig() {
    mutations.add(new IncrementMutation());
    mutations.add(new DecrementMutation());
    mutations.add(new SwitchMutation());
    
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
    return 40;
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean terminationCriteriaMet(Population population) {
    Individual currentBestCandidate = population.getFittest();
    
    if (bestFitnessSoFar > currentBestCandidate.getFitness()) {
      bestFitnessSoFar = currentBestCandidate.getFitness();
      numberOfSuccessiveRunsWithoutImprovement = 0;
    } else {
      numberOfSuccessiveRunsWithoutImprovement++;
    }
    
    log.info(population);

    return numberOfSuccessiveRunsWithoutImprovement > 10 || population.getAge() >= 1000;
  }

  public Mapper getMapper() {
    return this.mapper;
  }
}