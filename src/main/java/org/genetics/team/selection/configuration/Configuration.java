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

package org.genetics.team.selection.configuration;

import java.util.List;
import java.util.Map;

/**
 * This class holds the configuration
 */
public final class Configuration {

    private String populationData;
    private String[] employeeTypes;
    private Integer attributeCount;
    private Map<String, String> headerMapping;
    private Map<String, List<Integer>> attributeRanges;
    private List<String> excluded;
    private Double mutationRate;
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

    public String[] getEmployeeTypes() {
        return employeeTypes;
    }

    public void setEmployeeTypes(String[] employeeTypes) {
        this.employeeTypes = employeeTypes;
    }

    public Map<String, List<Integer>> getAttributeRanges() {
        return attributeRanges;
    }

    public void setAttributeRanges(Map<String, List<Integer>> attributeRanges) {
        this.attributeRanges = attributeRanges;
    }
}
