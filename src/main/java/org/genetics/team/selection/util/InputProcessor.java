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
import org.genetics.team.selection.beans.Employee;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InputProcessor {
    private static InputProcessor inputProcessor;
    private final String[] INPUT_HEADER;
    private final Map<String, String> HEADER_MAPPING;
    private final int attributeCount;

    private InputProcessor(String[] inputHeaders, Map<String, String> HEADER_MAPPING, int attributeCount) {
        this.INPUT_HEADER = inputHeaders;
        this.HEADER_MAPPING = HEADER_MAPPING;
        this.attributeCount = attributeCount;
    }

    public static InputProcessor getInputProcessor(int attributeCount, Map<String, String> headerMap) {
        if(inputProcessor == null) {
            String[] inputHeaders = new String[headerMap.size()];
            inputHeaders[0] = headerMap.get(CommonConstants.HEADER_ID);
            inputHeaders[1] = headerMap.get(CommonConstants.HEADER_NAME);
            inputHeaders[2] = headerMap.get(CommonConstants.HEADER_TYPE);

            for(int i = 1; i < 1 + attributeCount; ++i) {
                inputHeaders[i+2] = headerMap.get("attribute" + i);
            }
            inputProcessor = new InputProcessor(inputHeaders, headerMap, attributeCount);
        }
        return inputProcessor;
    }

    /**
     * Returns the header of the input file as a list of Strings
     *
     * @return header of the input file
     */
    public List<String> getHeader() {
        return new ArrayList<>(Arrays.asList(this.INPUT_HEADER));
    }

    /**
     * Removes given column headers and returns the header of the input file as a list of Strings
     *
     * @return header of the input file
     */
    public List<String> getHeader(List<String> excluded) {
        List<String> header = new ArrayList<>(Arrays.asList(this.INPUT_HEADER));
        if(header.size() > 0) {
            excluded.forEach(header::remove);
        }
        return header;
    }

    /**
     *
     * @param path path of the input CSV file
     * @return list of {@link Employee}
     * @throws IOException
     */
    public List<Employee> readPopulation(String path) throws IOException {
        List<Employee> employeeList = new ArrayList<>();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(INPUT_HEADER);
        FileReader fileReader = new FileReader(path);
        CSVParser csvFileParser = new CSVParser(fileReader, csvFormat);

        List<CSVRecord> csvRecords = csvFileParser.getRecords();
        for(CSVRecord csvRecord: csvRecords) {
            int id = Integer.parseInt(csvRecord.get(HEADER_MAPPING.get(CommonConstants.HEADER_ID)));
            String type = csvRecord.get(HEADER_MAPPING.get(CommonConstants.HEADER_TYPE));
            String name = csvRecord.get(HEADER_MAPPING.get(CommonConstants.HEADER_NAME));

            Employee employee = new Employee(id, type, name);
            employee.setGene(generateEmployeeGene());
        }
        return employeeList;
    }

    private byte[] generateEmployeeGene() {
        //TODO: implement gene generation
        return null;
    }

}
