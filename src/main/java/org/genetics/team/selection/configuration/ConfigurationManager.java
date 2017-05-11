package org.genetics.team.selection.configuration;

import org.genetics.team.selection.util.CommonConstants;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class reads the configuration and
 */
public class ConfigurationManager {

    private static Configuration configuration;

    private static void readConfiguration(String configPath) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get(configPath))) {
            configuration = yaml.loadAs(in, Configuration.class);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error occurred when reading the input config.", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        if(configuration.getPopulationData() == null) {
            configuration.setPopulationData(CommonConstants.DEFAULT_INPUT_PATH);
        }
    }

    /**
     * Reads configuration from the given path
     *
     * @param configPath path to config file.
     * @return {@link Configuration}
     * @throws IOException
     */
    public static Configuration getConfiguration(String configPath) throws IOException {
        if (configuration == null) {
            readConfiguration(configPath);
        }

        return configuration;
    }
}
