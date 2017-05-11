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

package org.genetics.team.selection.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.genetics.team.selection.algorithm.Algorithm;
import org.genetics.team.selection.algorithm.Population;
import org.genetics.team.selection.configuration.Configuration;
import org.genetics.team.selection.configuration.ConfigurationManager;
import org.genetics.team.selection.util.CommonConstants;
import org.genetics.team.selection.util.InputProcessor;
import org.genetics.team.selection.util.LogAppender;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main class and this generates the swing form
 */
public class GUIForm {
    private static Logger log = Logger.getLogger(GUIForm.class);
    private Configuration appConfiguration;
    private InputProcessor inputProcessor;
    private Map<String, JTextField> teamConfigComponentMap;
    private Map<String, JTextField> attributeConfigComponentMap;
    private Population population;

    private JFrame frame;
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel teamConfigPanel;
    private JScrollPane attributeConfigPane;
    private JPanel attributeConfigPanel;
    private JPanel consolePanel;
    private JScrollPane textAreaScrollPane;
    private JTextArea console;
    private JPanel buttonBar;
    private JButton runButton;

    public GUIForm() {
        this.appConfiguration = readAppConfiguration();
        this.inputProcessor = generateInputProcessor();
        this.population = new Population(this.appConfiguration);
        initComponents();
    }

    /**
     * This method reads the system configuration.
     *
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

    /**
     * This method generates the {@link InputProcessor}
     *
     * @return {@link InputProcessor}
     */
    private InputProcessor generateInputProcessor() {
        if (this.appConfiguration == null) {
            throw new IllegalStateException("Application Configuration is empty. Unable to process");
        }

        return InputProcessor.getInputProcessor(this.appConfiguration);
    }

    /**
     * This method listens to the click event of "Run" button in the GUI.
     *
     * @param e click {@link ActionEvent}
     */
    private void runButtonActionPerformed(ActionEvent e) {
        this.console.setText("");
        try {
            if (teamConfigComponentMap != null) {
                Map<String, Integer> teamDefinition = new HashMap<>();
                for (Object o : teamConfigComponentMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    teamDefinition
                            .put((String) entry.getKey(), Integer.parseInt(((JTextField) entry.getValue()).getText()));
                }
                this.population.setTeamDefinition(teamDefinition);
            }

            if (attributeConfigComponentMap != null) {
                Map<String, Double> attributeWeights = new HashMap<>();
                for (Object o : attributeConfigComponentMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    attributeWeights.put((String) entry.getKey(),
                            Double.parseDouble(((JTextField) entry.getValue()).getText()));
                }
                this.population.setAttributeWeights(attributeWeights);
            }
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(new JFrame(), e1.getClass() + e1.getMessage() +
                            ", There is an error in values you provided, please correct them and try again. ", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.population.generateInitialPopulation();

        Algorithm algorithm = new Algorithm(this.population);
        algorithm.runGA();
    }

    /**
     * Creates dynamic UI components.
     */
    private void createUIComponents() {
        createConfigPanel();
        createTeamPanel();
    }

    /**
     * Initializes team definition panel.
     */
    private void createTeamPanel() {
        String[] types = this.appConfiguration.getTypes();
        if (types == null) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Types is a required configuration. Unable tp proceed with type being empty.", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException(
                    "Types is a required configuration. Unable tp proceed with type being empty.");
        }
        teamConfigComponentMap = new HashMap<>();
        int cols = types.length > 5 ? 10 : types.length * 2;
        int rows = types.length > 5 ? (int) Math.ceil((double) types.length / 5) : 1;
        teamConfigPanel = new JPanel(new GridLayout(rows, cols, 10, 5));
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        for (String type : types) {
            JLabel label = new JLabel(type + ":");
            label.setBorder(new EmptyBorder(0, 10, 0, 5));
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            JFormattedTextField textField = new JFormattedTextField(formatter);
            teamConfigComponentMap.put(type, textField);
            teamConfigPanel.add(label);
            teamConfigPanel.add(textField);
        }
    }

    /**
     * Initializes attribute weight configuration panel.
     */
    private void createConfigPanel() {
        Map<String, String> headerMapping = this.appConfiguration.getHeaderMapping();
        if (headerMapping == null) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "HeaderMapping is a required configuration. Unable tp proceed with header mapping being empty.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException(
                    "HeaderMapping is a required configuration. Unable tp proceed with header mapping being empty.");
        }
        List<String> excluded = this.appConfiguration.getExcluded();
        if (excluded == null) {
            excluded = new ArrayList<>();
        }
        excluded.add(headerMapping.get(CommonConstants.HEADER_ID));
        excluded.add(headerMapping.get(CommonConstants.HEADER_NAME));
        excluded.add(headerMapping.get(CommonConstants.HEADER_TYPE));

        List<String> header = this.inputProcessor.getHeader(excluded);
        int cols = header.size() > 5 ? 10 : header.size() * 2;
        int rows = header.size() > 5 ? (int) Math.ceil((double) header.size() / 5) : 1;
        attributeConfigPanel = new JPanel(new GridLayout(rows, cols, 10, 5));
        attributeConfigComponentMap = new HashMap<>();
        for (String entry : header) {
            JLabel label = new JLabel(entry + ": ");
            label.setBorder(new EmptyBorder(0, 10, 0, 5));
            JTextField textField = new JTextField();
            attributeConfigComponentMap.put(entry, textField);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            attributeConfigPanel.add(label);
            attributeConfigPanel.add(textField);
        }
    }

    private void initComponents() {
        createUIComponents();

        frame = new JFrame("Team Selection - v1.0.0");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        attributeConfigPane = new JScrollPane();
        attributeConfigPane.add(attributeConfigPanel);
        consolePanel = new JPanel();
        textAreaScrollPane = new JScrollPane();
        console = new JTextArea();
        buttonBar = new JPanel();
        runButton = new JButton();

        //======== frame ========
        {
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setAlwaysOnTop(false);
            Container frameContentPane = frame.getContentPane();
            frameContentPane.setLayout(new BorderLayout());

            //======== dialogPane ========
            {
                dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
                dialogPane.setLayout(new BorderLayout());

                //======== contentPanel ========
                {
                    contentPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));

                    //======== teamConfigPanel ========
                    {
                        teamConfigPanel.setBorder(new TitledBorder("Define Team Combination"));
                    }
                    contentPanel.add(teamConfigPanel,
                            new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                                    null, null));

                    //======== attributeConfigPane ========
                    {
                        attributeConfigPane
                                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                        attributeConfigPane
                                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        attributeConfigPane.setViewportBorder(null);
                        attributeConfigPane.setBorder(null);

                        //======== attributeConfigPanel ========
                        {
                            attributeConfigPanel.setBorder(new TitledBorder("Attribute Configuration"));
                            attributeConfigPanel.setLayout(new GridLayout());
                        }
                        attributeConfigPane.setViewportView(attributeConfigPanel);
                    }
                    contentPanel.add(attributeConfigPane,
                            new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                                    null, null));

                    //======== consolePanel ========
                    {
                        consolePanel.setBorder(new TitledBorder("Console"));
                        consolePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

                        //======== textAreaScrollPane ========
                        {

                            //---- console ----
                            console.setTabSize(4);
                            textAreaScrollPane.setViewportView(console);
                            textAreaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                            textAreaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                            console.setEditable(false);
                            createLogAppender(console);
                            DefaultCaret caret = (DefaultCaret)console.getCaret();
                            caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

                        }
                        consolePanel.add(textAreaScrollPane,
                                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                                        GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED,
                                        GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 300),
                                        new Dimension(300, 300), null, 0, true));
                    }
                    contentPanel.add(consolePanel,
                            new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                                    null, null));
                }
                dialogPane.add(contentPanel, BorderLayout.CENTER);

                //======== buttonBar ========
                {
                    buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                    buttonBar.setLayout(new GridBagLayout());
                    ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] { 0, 80 };
                    ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] { 1.0, 0.0 };

                    //---- runButton ----
                    runButton.setText("Run");
                    runButton.addActionListener(this::runButtonActionPerformed);
                    buttonBar.add(runButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                }
                dialogPane.add(buttonBar, BorderLayout.SOUTH);
            }
            frameContentPane.add(dialogPane, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(frame.getOwner());
        }
    }

    /**
     * Initializes UI log appender.
     *
     * @param console {@link JTextArea}
     */
    private void createLogAppender(JTextArea console) {
        LogAppender logAppender = new LogAppender(console);
        LogManager.getRootLogger().addAppender(logAppender);
    }

    public static void main(String[] args) {
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        GUIForm mainForm = new GUIForm();
        try {
            mainForm.population.setPopulation(
                    mainForm.inputProcessor.readPopulation(mainForm.appConfiguration.getPopulationData()));
        } catch (IOException e) {
            log.error("Error occurred when reading the input file.", e);
        }
        mainForm.frame.setVisible(true);
    }
}
