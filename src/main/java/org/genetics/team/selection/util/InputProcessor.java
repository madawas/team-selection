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

package org.genetics.team.selection.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.genetics.team.selection.beans.Employee;
import org.genetics.team.selection.configuration.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains methods related to processing of the input file.
 */
public class InputProcessor {
    private static Logger log = Logger.getLogger(InputProcessor.class);
    private static InputProcessor inputProcessor;
    private final String[] INPUT_HEADER;
    private Configuration configuration;

    private InputProcessor(String[] inputHeaders, Configuration configuration) {
        this.INPUT_HEADER = inputHeaders;
        this.configuration = configuration;
    }

    /**
     * Initializes input processor
     *
     * @param configuration {@link Configuration} object.
     * @return {@link InputProcessor}
     */
    public static InputProcessor getInputProcessor(Configuration configuration) {
        Map<String, String> headerMap = configuration.getHeaderMapping();
        Integer attributeCount = configuration.getAttributeCount();
        if(inputProcessor == null) {
            String[] inputHeaders = new String[headerMap.size()];
            inputHeaders[0] = headerMap.get(CommonConstants.HEADER_ID);
            inputHeaders[1] = headerMap.get(CommonConstants.HEADER_NAME);
            inputHeaders[2] = headerMap.get(CommonConstants.HEADER_TYPE);

            for(int i = 1; i < 1 + attributeCount; ++i) {
                inputHeaders[i+2] = headerMap.get(CommonConstants.ATTRIBUTE_PREFIX + i);
            }
            inputProcessor = new InputProcessor(inputHeaders, configuration);
        }
        return inputProcessor;
    }

    /**
     * Returns the header of the input file as a list of Strings
     *
     * @return header of the input file
     */
    private List<String> getHeader() {
        return new ArrayList<>(Arrays.asList(this.INPUT_HEADER));
    }

    /**
     * Removes given column headers and returns the header of the input file as a list of Strings
     *
     * @return header of the input file
     */
    public List<String> getHeader(List<String> excluded) {
        List<String> header = getHeader();
        if(header.size() > 0) {
            excluded.forEach(header::remove);
        }
        return header;
    }

    /**
     * Processes given CSV file and generates Employee objects and create the population.
     *
     * @param path path of the input CSV file
     * @return Map of {@link Employee}
     * @throws IOException
     */
    public Map<String, List<Employee>> readPopulation(String path) throws IOException {
        log.info("Reading Population Data");
        Map<String, String> headerMapping = configuration.getHeaderMapping();
        Integer attributeCount = configuration.getAttributeCount();
        Map<String, List<Employee>> employees = new HashMap<>();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        FileReader fileReader = new FileReader(path);
        CSVParser csvFileParser = new CSVParser(fileReader, csvFormat);

        List<CSVRecord> csvRecords = csvFileParser.getRecords();
        for(CSVRecord csvRecord: csvRecords) {
            int id = Integer.parseInt(csvRecord.get(headerMapping.get(CommonConstants.HEADER_ID)));
            String type = csvRecord.get(headerMapping.get(CommonConstants.HEADER_TYPE));
            String name = csvRecord.get(headerMapping.get(CommonConstants.HEADER_NAME));

            Employee employee = new Employee(id, type, name);
            Map<String, Integer> attributeMap = new HashMap<>();
            for (int i = 1; i < attributeCount + 1; ++i) {
                String attribute = headerMapping.get(CommonConstants.ATTRIBUTE_PREFIX + i);
                int attributeValue = Integer.parseInt(csvRecord.get(attribute));
                attributeMap.put(attribute, attributeValue);
            }
            employee.setAttributeValues(attributeMap);

            if(employees.get(employee.getEmployeeType()) != null) {
                employees.get(employee.getEmployeeType()).add(employee);
            } else {
                List<Employee> temp = new ArrayList<>();
                temp.add(employee);
                employees.put(employee.getEmployeeType(), temp);
            }
        }
        log.info("Generated " + csvRecords.size() + " employees.");
        return employees;
    }
}
