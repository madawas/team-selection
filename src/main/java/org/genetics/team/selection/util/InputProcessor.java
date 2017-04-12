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

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InputProcessor {

    /**
     * Reads header of a CSV file.
     *
     * @param path CSV file path
     * @return header mapping of the csv file
     * @throws IOException
     */
    public static Map<String, Integer> readHeader(String path) throws IOException {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        FileReader fileReader = new FileReader(path);
        CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
        return  csvFileParser.getHeaderMap();
    }

    /**
     * Returns header map of a CSV file excluding given columns.
     *
     * @param path CSV file path.
     * @param excluded excluded column names.
     * @return header mapping of the csv file
     * @throws IOException
     */
    public static Map<String, Integer> readHeader(String path, List<String> excluded) throws IOException {
        Map<String, Integer> headerMap = readHeader(path);
        if(headerMap != null && headerMap.size() > 0) {
            excluded.forEach(headerMap::remove);
        }
        return headerMap;
    }

}