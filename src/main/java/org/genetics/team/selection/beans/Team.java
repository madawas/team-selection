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
