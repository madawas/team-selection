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
import org.genetics.team.selection.configuration.Configuration;
import org.genetics.team.selection.util.CommonConstants;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Population {
    private Configuration configuration;
    private Map<String, List<Employee>> population;
    private List<Team> initialPopulation;
    private Map<String, Integer> teamDefinition;
    private Map<String, Double> attributeWeights;

    public Population(Configuration configuration) {
        this.configuration = configuration;
        this.initialPopulation = new ArrayList<>();
    }

    public void setPopulation(Map<String, List<Employee>> population) {
        this.population = population;
    }

    public void generateInitialPopulation() {
        int initialPopSize = this.configuration.getInitialPopulationSize();
        while(--initialPopSize >= 0) {
            initialPopulation.add(generateTeam());
        }
    }

    private Set<Integer> getRandomNumbers(int min, int max, int count) {
        Random random = new Random();
        Set<Integer> generated = new LinkedHashSet<>();
        while (generated.size() < count) {
            Integer next = random.nextInt((max - min) + 1) + min;
            generated.add(next);
        }
        return generated;
    }

    public double calculateFitness(Team team) {
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

    public Team generateTeam() {
        String[] types = this.configuration.getTypes();
        List<Employee> employeeList = new ArrayList<>();
        for(String type: types) {
            int count = teamDefinition.get(type);
            List<Employee> candidates = population.get(type);
            Set<Integer> rnd = getRandomNumbers(0, candidates.size()-1, count);
            employeeList.addAll(rnd.stream().map(candidates::get).collect(Collectors.toList()));
        }
        Team team = new Team(employeeList);
        team.setFitness(calculateFitness(team));
        return team;
    }

    public Team generateTeam(List<Employee> employeeList) {
        Team team = new Team(employeeList);
        team.setFitness(calculateFitness(team));
        return team;
    }

    public Map<String, Integer> getTeamDefinition() {
        return teamDefinition;
    }

    public void setTeamDefinition(Map<String, Integer> teamDefinition) {
        this.teamDefinition = teamDefinition;
    }

    public Map<String, Double> getAttributeWeights() {
        return attributeWeights;
    }

    public void setAttributeWeights(Map<String, Double> attributeWeights) {
        this.attributeWeights = attributeWeights;
    }

    public List<Team> getInitialPopulation() {
        return this.initialPopulation;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
