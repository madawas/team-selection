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

import org.apache.log4j.Logger;
import org.genetics.team.selection.configuration.Configuration;
import org.genetics.team.selection.configuration.ConfigurationManager;
import org.genetics.team.selection.util.CommonConstants;

import javax.swing.*;
import java.io.IOException;

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
            return ConfigurationManager.getConfiguration(CommonConstants.DEFAULT_CONFIG_PATH);
        } catch (IOException e) {
            log.error("Error occurred while reading the system configuration ", e);
            System.exit(-1);
        }
        return null;
    }

    public static void main(String[] args) {


        JFrame jFrame = new JFrame("Team Selection - v1.0.0");
        jFrame.setContentPane(new TeamSelectionForm().mainPanel);
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
