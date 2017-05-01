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

public class Algorithm {
    private Population population;
    private List<Team> generation;
    private Team currentFittest;
    private Double crossoverRate;
    private Double mutationRate;
    private Integer maxGenerations;
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
        while (--maxGenerations >= 0) {
            evaluateCurrentFittest();
            selection();
            crossover();
            mutate();
            evaluateCurrentFittest();
        }
    }

    private void evaluateCurrentFittest() {
        this.currentFittest = this.generation.get(0);
        generation.stream().filter(team -> team.getFitness() > this.currentFittest.getFitness())
                .forEach(team -> this.currentFittest = team);
    }

    private void selection() {
        List<Team> shuffledPopulation = new ArrayList<>();
        double sumFitness = 0;
        for (Team team : generation) {
            sumFitness += team.getFitness();
        }
        double temp = 0;
        for (Team team : generation) {
            double rand = random.nextDouble() * sumFitness;
            temp += team.getFitness();
            if (temp >= rand) {
                shuffledPopulation.add(team);
            }
            temp = 0;
        }
        this.generation = shuffledPopulation;
    }

    private void crossover() {
        List<Team> crossedPopulation = new ArrayList<>();
        for (int i = 0; i < this.generation.size(); i = i + 2) {
            double rand = random.nextDouble();
            if (rand <= this.crossoverRate) {
                int crossoverPoint = random.nextInt(this.generation.size());
                if (crossoverPoint != 0 || crossoverPoint != this.generation.size() - 1) {
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

    }
}
