package org.genetics.team.selection.configuration;

import java.util.List;
import java.util.Map;

/**
 * This class holds all the configuration parameters of the application.
 */
public final class Configuration {

    private String populationData;
    private String[] types;
    private Integer attributeCount;
    private Map<String, String> headerMapping;
    private List<String> excluded;
    private Double mutationRate;
    private Double crossoverRate;
    private Integer generations;
    private Integer initialPopulationSize;

    public String getPopulationData() {
        return populationData;
    }

    public void setPopulationData(String populationData) {
        this.populationData = populationData;
    }

    public List<String> getExcluded() {
        return excluded;
    }

    public void setExcluded(List<String> excluded) {
        this.excluded = excluded;
    }

    public Double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(Double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Map<String, String> getHeaderMapping() {
        return headerMapping;
    }

    public void setHeaderMapping(Map<String, String> headerMapping) {
        this.headerMapping = headerMapping;
    }

    public Integer getGenerations() {
        return generations;
    }

    public void setGenerations(Integer generations) {
        this.generations = generations;
    }

    public Integer getInitialPopulationSize() {
        return initialPopulationSize;
    }

    public void setInitialPopulationSize(Integer initialPopulationSize) {
        this.initialPopulationSize = initialPopulationSize;
    }

    public Integer getAttributeCount() {
        return attributeCount;
    }

    public void setAttributeCount(Integer attributeCount) {
        this.attributeCount = attributeCount;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public Double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(Double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
}
