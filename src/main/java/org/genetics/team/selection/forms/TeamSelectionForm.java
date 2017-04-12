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

package org.genetics.team.selection.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import org.apache.log4j.Logger;
import org.genetics.team.selection.configuration.Configuration;
import org.genetics.team.selection.configuration.ConfigurationManager;
import org.genetics.team.selection.util.InputProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TeamSelectionForm {
    private static Logger log = Logger.getLogger(TeamSelectionForm.class.getName());
    private Configuration appConfiguration;
    private JPanel mainPanel;
    private JScrollPane configurationPanel;

    public TeamSelectionForm () {
        this.appConfiguration = this.readAppConfiguration();
    }

    /**
     * This method reads the system configuration.
     * @return {@link Configuration}
     */
    private Configuration readAppConfiguration() {
        try {
            return ConfigurationManager.getConfiguration("config.yaml");
        } catch (IOException e) {
            log.error("Error occurred while reading the system configuration ", e);
            System.exit(-1);
        }
        return null;
    }
    private void initComponents(TeamSelectionForm teamSelectionForm, Map<String, Integer> header) {
        JFrame jFrame = new JFrame("Team Selection - v1.0.0");
        jFrame.setContentPane(teamSelectionForm.mainPanel);
        jFrame.setSize(800, 800);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel configPanel = new JPanel(new GridLayout(header.size(), 2));
        for (Map.Entry<String, Integer> entry : header.entrySet()) {
            JTextField label = new JTextField(entry.getKey());
            JTextField textField = new JTextField();
            configPanel.add(label);
            configPanel.add(textField);
        }
        this.configurationPanel = new JScrollPane(configPanel);
        jFrame.add(configurationPanel, new GridConstraints());
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        log.info("Starting App");
        TeamSelectionForm teamSelectionForm = new TeamSelectionForm();
        try {
            Map<String, Integer> header = InputProcessor.readHeader(teamSelectionForm.appConfiguration.getPopulationData(),
                    teamSelectionForm.appConfiguration.getExcluded());
            teamSelectionForm.initComponents(teamSelectionForm, header);
        } catch (IOException e) {
            log.error("Error occurred when reading the input file. ", e);
        }
    }
}
