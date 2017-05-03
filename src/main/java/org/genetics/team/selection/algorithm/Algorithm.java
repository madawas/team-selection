/*
 * Copyright 2017 Madawa Soysa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.genetics.team.selection.algorithm;

import org.genetics.team.selection.beans.Employee;
import org.genetics.team.selection.beans.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class contains the genetic algorithm related methods and exposes a public method runGA which will run genetic
 * operations based on given configurations.
 */
public class Algorithm {
    private Population population;
    private List<Team> generation;
    private List<Team> currentGeneration;
    private Team currentFittest;
    private final Double crossoverRate;
    private final Double mutationRate;
    private final Integer maxGenerations;
    private Random random;

    /**
     * Constructs an {@link Algorithm} object.
     *
     * @param population {@link Population}
     */
    public Algorithm(Population population) {
        this.population = population;
        this.generation = population.getInitialPopulation();
        this.crossoverRate = population.getConfiguration().getCrossoverRate();
        this.mutationRate = population.getConfiguration().getMutationRate();
        this.maxGenerations = population.getConfiguration().getGenerations();
        this.random = new Random();
    }

    /**
     * This method will run the genetic algorithm.
     */
    public void runGA() {
        int gen = maxGenerations;
        int populationSize = this.population.getConfiguration().getInitialPopulationSize();
        int selectionSize = Math.round(populationSize * 0.7f);
        evaluateCurrentFittest();
        while (--gen >= 0) {
            selection(populationSize);
            crossover();
            mutate();
            selectFittest(selectionSize);
            int currentGenerationSize = this.generation.size();
            for (int i = 0; i < populationSize - currentGenerationSize; ++i) {
                this.generation.add(this.population.generateTeam());
            }
            evaluateCurrentFittest();
        }
        for (Employee employee : this.currentFittest.getEmployees()) {
            System.out.println(employee.getName());
        }
        System.out.println(this.currentFittest.getFitness());
    }

    /**
     * Evaluates current fittest chromosome from the current population.
     */
    private void evaluateCurrentFittest() {
        if (currentFittest == null) {
            this.currentFittest = this.generation.get(0);
        }
        generation.stream().filter(team -> team.getFitness() > this.currentFittest.getFitness())
                .forEach(team -> this.currentFittest = team);
    }

    /**
     * This method selects individuals based on Roulette Wheel selection mechanism and updates the current generation
     * with selected chromosomes.
     *
     * @param populationSize size of the population.
     */
    private void selection(int populationSize) {
        List<Team> shuffledPopulation = new ArrayList<>();
        double sumFitness = 0;
        for (Team team : generation) {
            sumFitness += team.getFitness();
        }
        while (--populationSize >= 0) {
            double temp = 0;
            double rand = random.nextDouble() * sumFitness;
            for (Team team : generation) {
                temp += team.getFitness();
                if (temp >= rand) {
                    shuffledPopulation.add(team);
                    break;
                }
            }
        }
        this.currentGeneration = shuffledPopulation;
    }

    /**
     * This method does single point crossover operation on selected chromosomes and updates the current generation with
     * crossed population.
     */
    private void crossover() {
        List<Team> crossedPopulation = new ArrayList<>();
        int length = this.currentGeneration.get(0).getEmployees().size() - 1;
        for (int i = 0; i < this.currentGeneration.size(); i = i + 2) {
            if (random.nextDouble() <= this.crossoverRate) {
                int crossoverPoint = random.nextInt(length);
                if (crossoverPoint > 0 && crossoverPoint < length) {
                    List<Employee> team1_list = this.currentGeneration.get(i).getEmployees();
                    List<Employee> team2_list = this.currentGeneration.get(i + 1).getEmployees();

                    List<Employee> child1_list = new ArrayList<>();
                    List<Employee> child2_list = new ArrayList<>();

                    for (int j = 0; j < crossoverPoint; ++j) {
                        child1_list.add(team1_list.get(j));
                        child2_list.add(team2_list.get(j));
                    }

                    for (int j = crossoverPoint; j < team1_list.size(); ++j) {
                        child1_list.add(team1_list.get(j));
                        child2_list.add(team2_list.get(j));
                    }
                    crossedPopulation.add(this.population.generateTeam(child1_list));
                    crossedPopulation.add(this.population.generateTeam(child2_list));
                    continue;
                }
            }
            crossedPopulation.add(this.currentGeneration.get(i));
            crossedPopulation.add(this.currentGeneration.get(i + 1));
        }

        this.currentGeneration = crossedPopulation;
    }

    private void mutate() {
        List<Team> toRemove = new ArrayList<>(this.currentGeneration.size());
        List<List<Employee>> toAdd = new ArrayList<>(this.currentGeneration.size());
        for (Team team : this.currentGeneration) {
            boolean isMutated = false;
            List<Employee> employeeList = team.getEmployees();
            List<Integer> removeIndexes = new ArrayList<>(employeeList.size());
            for (int i = 0; i < employeeList.size(); ++i) {
                if (random.nextDouble() <= this.mutationRate) {
                    isMutated = true;
                    removeIndexes.add(i);
                }
            }

            for (int index : removeIndexes) {
                String type = employeeList.get(index).getEmployeeType();
                employeeList.remove(index);
                employeeList.add(index, this.population.generateEmployee(type));
            }
            if (isMutated) {
                toRemove.add(team);
                toAdd.add(employeeList);
            }
        }

        for (Team team : toRemove) {
            this.currentGeneration.remove(team);
        }

        this.currentGeneration.addAll(toAdd.stream().map(employees -> this.population.generateTeam(employees))
                .collect(Collectors.toList()));
    }

    private void selectFittest(int selectionSize) {
        List<Team> combinedGen = Stream.concat(this.generation.stream(), this.currentGeneration.stream())
                .collect(Collectors.toList());
        Collections.sort(combinedGen, Team::compareTo);
        this.generation.clear();

        for (int i = combinedGen.size() - 1; i >= combinedGen.size() - selectionSize; --i) {
            if(combinedGen.get(i).getFitness() > 0) {
                this.generation.add(combinedGen.get(i));
            }
        }
    }
}
