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

    public Population(Configuration configuration) {
        this.configuration = configuration;
        this.initialPopulation = new ArrayList<>();
    }

    public void setPopulation(Map<String, List<Employee>> population) {
        this.population = population;
    }

    public void generateInitialPopulation(Map<String, Integer> teamDefinition) {
        int initialPopSize = this.configuration.getInitialPopulationSize();
        String[] types = this.configuration.getTypes();
        while(--initialPopSize >= 0) {
            List<Employee> employeeList = new ArrayList<>();
            for(String type: types) {
                int count = teamDefinition.get(type);
                List<Employee> candidates = population.get(type);
                Set<Integer> rnd = getRandomNumbers(0, candidates.size()-1, count);
                employeeList.addAll(rnd.stream().map(candidates::get).collect(Collectors.toList()));
            }
            Team team = new Team(employeeList);
            initialPopulation.add(team);
        }
        System.out.println(initialPopulation.size());
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
}
