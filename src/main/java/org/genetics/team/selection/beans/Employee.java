package org.genetics.team.selection.beans;

public class Employee {
    private int id;
    private byte[] gene;
    private EmployeeType type;

    public Employee(int id, EmployeeType type) {
        this.id = id;
        this.type = type;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getGene() {
        return gene;
    }

    public void setGene(byte[] gene) {
        this.gene = gene;
    }
}
