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

package org.genetics.team.selection.beans;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents a Chromosome (Team in this context)
 */
public class Team implements Comparable<Team> {
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int id;
    private final List<Employee> employees;
    private double fitness;

    public Team(List<Employee> employees) {
        this.id = count.incrementAndGet();
        this.employees = Collections.unmodifiableList(employees);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Team o) {
        return Double.compare(this.fitness, o.fitness);
    }
}
