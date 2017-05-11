package org.genetics.team.selection.beans;

import java.util.Map;

/**
 * This class represents an Individual (Employee in the context)
 */
public class Employee {
    private int id;
    private String name;
    private String employeeType;
    private Map<String, Integer> attributeValues;

    public Employee(int id, String employeeType, String name) {
        this.id = id;
        this.employeeType = employeeType;
        this.name = name;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(Map<String, Integer> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
