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

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigurationManager {

    private static Configuration configuration;

    private static void readConfiguration(String configPath) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get(configPath))) {
            configuration = yaml.loadAs(in, Configuration.class);
        }
    }

    public static Configuration getConfiguration(String configPath) throws IOException {
        if (configuration == null) {
            readConfiguration(configPath);
        }

        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        ConfigurationManager.configuration = configuration;
    }
}