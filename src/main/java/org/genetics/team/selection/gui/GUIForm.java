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
import org.apache.log4j.Logger;
import org.genetics.team.selection.configuration.Configuration;
import org.genetics.team.selection.configuration.ConfigurationManager;
import org.genetics.team.selection.util.CommonConstants;
import org.genetics.team.selection.util.InputProcessor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIForm {
    private static Logger log = Logger.getLogger(GUIForm.class);
    private Configuration appConfiguration;
    private InputProcessor inputProcessor;

    public GUIForm() {
        this.appConfiguration = readAppConfiguration();
        this.inputProcessor = generateInputProcessor();
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

    private InputProcessor generateInputProcessor() {
        if (this.appConfiguration == null) {
            throw new IllegalStateException("Application Configuration is empty. Unable to process");
        }

        return InputProcessor
                .getInputProcessor(this.appConfiguration.getAttributeCount(), this.appConfiguration.getHeaderMapping());
    }

    private void runButtonActionPerformed(ActionEvent e) {
        System.out.println("Hello");
    }

    private void createUIComponents() {
        createConfigPanel();
    }

    private void createConfigPanel() {
        Map<String, String> headerMapping = this.appConfiguration.getHeaderMapping();
        List<String> excluded = this.appConfiguration.getExcluded();
        if (excluded == null) {
            excluded = new ArrayList<>();
        }
        excluded.add(headerMapping.get(CommonConstants.HEADER_ID));
        excluded.add(headerMapping.get(CommonConstants.HEADER_NAME));
        excluded.add(headerMapping.get(CommonConstants.HEADER_TYPE));

        List<String> header = this.inputProcessor.getHeader(excluded);
        int cols = header.size() > 5 ? 5 : header.size();
        int rows = header.size() > 5 ? (int) Math.ceil((double) header.size() / 5) : 1;
        attributeConfigPanel = new JPanel(new GridLayout(rows, cols, 10, 5));
        for (String entry : header) {
            JLabel label = new JLabel(entry + ":");
            JTextField textField = new JTextField();
            attributeConfigPanel.add(label);
            attributeConfigPanel.add(textField);
        }
    }

    private void initComponents() {
        createUIComponents();

        frame = new JFrame("Team Selection - v1.0.0");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        teamConfigPanel = new JPanel();
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
            frame.setAlwaysOnTop(true);
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
                        teamConfigPanel.setLayout(new GridLayout());
                    }
                    contentPanel.add(teamConfigPanel,
                            new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                                    null, null));

                    //======== attributeConfigPane ========
                    {
                        attributeConfigPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                        attributeConfigPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
                            console.setText("Console...");
                            textAreaScrollPane.setViewportView(console);
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
                    runButton.addActionListener(e -> runButtonActionPerformed(e));
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

    public static void main(String[] args) {
        GUIForm mainForm = new GUIForm();
        mainForm.frame.setVisible(true);
    }

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
}
