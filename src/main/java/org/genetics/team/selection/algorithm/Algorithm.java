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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Algorithm {
    private Population population;
    private List<Team> generation;
    private Team currentFittest;
    private final Double crossoverRate;
    private final Double mutationRate;
    private final Integer maxGenerations;
    Random random;

    public Algorithm(Population population) {
        this.population = population;
        this.generation = population.getInitialPopulation();
        this.crossoverRate = population.getConfiguration().getCrossoverRate();
        this.mutationRate = population.getConfiguration().getMutationRate();
        this.maxGenerations = population.getConfiguration().getGenerations();
        this.random = new Random();
    }

    public void runGA() {
        int gen = maxGenerations;
        //        while (--gen >= 0) {
        evaluateCurrentFittest();
        selection();
        crossover();
        mutate();
        selectFittest();
        for (int i = 0; i < 3; ++i) {
            this.generation.add(this.population.generateTeam());
        }
        //        }
        for (Employee employee : this.currentFittest.getEmployees()) {
            System.out.println(employee.getName());
        }
    }

    private void evaluateCurrentFittest() {
        this.currentFittest = this.generation.get(0);
        generation.stream().filter(team -> team.getFitness() > this.currentFittest.getFitness())
                .forEach(team -> this.currentFittest = team);
    }

    private void selection() {
        List<Team> shuffledPopulation = new ArrayList<>();
        int popSize = this.population.getConfiguration().getInitialPopulationSize();
        double sumFitness = 0;
        for (Team team : generation) {
            sumFitness += team.getFitness();
        }
        while (--popSize >= 0) {
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
        this.generation = shuffledPopulation;
    }

    private void crossover() {
        List<Team> crossedPopulation = new ArrayList<>();
        int length = this.generation.get(0).getEmployees().size() - 1;
        for (int i = 0; i < this.generation.size(); i = i + 2) {
            if (random.nextDouble() <= this.crossoverRate) {
                int crossoverPoint = random.nextInt(this.generation.size());
                if (crossoverPoint > 0 && crossoverPoint < length) {
                    List<Employee> team1_list = this.generation.get(i).getEmployees();
                    List<Employee> team2_list = this.generation.get(i + 1).getEmployees();

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
            crossedPopulation.add(this.generation.get(i));
            crossedPopulation.add(this.generation.get(i + 1));
        }

        this.generation = crossedPopulation;
    }

    private void mutate() {
        List<Team> toRemove = new ArrayList<>(this.generation.size());
        List<List<Employee>> toAdd = new ArrayList<>(this.generation.size());
        for (Team team : this.generation) {
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
            this.generation.remove(team);
        }

        this.generation.addAll(toAdd.stream().map(employees -> this.population.generateTeam(employees))
                .collect(Collectors.toList()));
    }

    private void selectFittest() {
        for (int i = 0; i < 3; ++i) {
            Team min = null;
            for (Team element : this.generation) {
                if (min == null) {
                    min = element;
                    continue;
                }
                if (element.compareTo(min) < 0) {
                    min = element;
                }
            }
            this.generation.remove(min);
        }
    }
}
