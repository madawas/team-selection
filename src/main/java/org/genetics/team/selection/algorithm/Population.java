package org.genetics.team.selection.algorithm;

import org.apache.log4j.Logger;
import org.genetics.team.selection.beans.Employee;
import org.genetics.team.selection.beans.Team;
import org.genetics.team.selection.configuration.Configuration;
import org.genetics.team.selection.util.CommonConstants;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * This class has the behaviour to generate and maintain Population
 */
public class Population {
    private static Logger log = Logger.getLogger(Population.class);
    private Configuration configuration;
    private Map<String, List<Employee>> population;
    private List<Team> initialPopulation;
    private Map<String, Integer> teamDefinition;
    private Map<String, Double> attributeWeights;
    private Random random;

    public Population(Configuration configuration) {
        this.configuration = configuration;
        this.initialPopulation = new ArrayList<>();
        this.random = new Random();
    }

    public void setPopulation(Map<String, List<Employee>> population) {
        this.population = population;
    }

    /**
     * Generates initial population (Chromosomes)
     */
    public void generateInitialPopulation() {
        int initialPopSize = this.configuration.getInitialPopulationSize();
        log.info("Generating initial population of size: "+ initialPopSize);
        if (this.initialPopulation.size() > 0) {
            this.initialPopulation.clear();
        }
        while (--initialPopSize >= 0) {
            this.initialPopulation.add(generateTeam());
        }
    }

    /**
     * Generates unique set of random integers between given two integers.
     *
     * @param min   minimum value
     * @param max   maximum value
     * @param count number of unique integers to generate.
     * @return Set of unique integers
     */
    private Set<Integer> getRandomNumbers(int min, int max, int count) {
        Set<Integer> generated = new LinkedHashSet<>();
        while (generated.size() < count) {
            Integer next = this.random.nextInt((max - min) + 1) + min;
            generated.add(next);
        }
        return generated;
    }

    /**
     * Calculates the fitness value of a given Chromosome ({@link Team})
     *
     * @param team {@link Team}
     * @return fitness value
     */
    private double calculateFitness(Team team) {
        Map<String, String> headerMapping = this.configuration.getHeaderMapping();
        int attributeCount = this.configuration.getAttributeCount();
        double fitness = 0;
        List<Employee> employees = team.getEmployees();
        for (int i = 1; i < attributeCount + 1; ++i) {
            String attribute = headerMapping.get(CommonConstants.ATTRIBUTE_PREFIX + i);
            double temp = 0;
            for (Employee employee : employees) {
                temp += employee.getAttributeValues().get(attribute);
            }
            fitness += temp * attributeWeights.get(attribute) / employees.size();
        }
        return fitness / attributeCount;
    }

    /**
     * Generates a Chromosome {@link Team} from the population.
     *
     * @return {@link Team}
     */
    Team generateTeam() {
        String[] types = this.configuration.getTypes();
        List<Employee> employeeList = new ArrayList<>();
        for (String type : types) {
            int count = teamDefinition.get(type);
            List<Employee> candidates = population.get(type);
            Set<Integer> rnd = getRandomNumbers(0, candidates.size() - 1, count);
            employeeList.addAll(rnd.stream().map(candidates::get).collect(Collectors.toList()));
        }
        return generateTeam(employeeList);
    }

    /**
     * Generates a Chromosome {@link Team} by a given employee list.
     *
     * @return {@link Team}
     */
    Team generateTeam(List<Employee> employeeList) {
        Team team = new Team(employeeList);
        double fitness = isUnique(team) ? calculateFitness(team) : 0;
        team.setFitness(fitness);
        return team;
    }

    /**
     * Generates an Individual {@link Employee} of a given type.
     *
     * @param type               type of the employee to generate.
     * @param availableEmployees list of individual id's that are already in the team
     * @return {@link Team}
     */
    Employee generateEmployee(String type, List<Integer> availableEmployees) {
        int index = this.random.nextInt(population.get(type).size());
        Employee employee = population.get(type).get(index);
        if (availableEmployees.contains(employee.getId())) {
            return generateEmployee(type, availableEmployees);
        } else {
            return employee;
        }
    }

    public void setTeamDefinition(Map<String, Integer> teamDefinition) {
        this.teamDefinition = teamDefinition;
    }

    public void setAttributeWeights(Map<String, Double> attributeWeights) {
        this.attributeWeights = attributeWeights;
    }

    List<Team> getInitialPopulation() {
        return this.initialPopulation;
    }

    Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Checks whether the team has unique employees.
     *
     * @param team {@link Team}
     * @return whether the team is unique.
     */
    private boolean isUnique(Team team) {
        List<Employee> unique = team.getEmployees().stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(Employee::getId))), ArrayList::new));
        return unique.size() == team.getEmployees().size();
    }
}
