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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

public class MainForm {
    private static Logger log = Logger.getLogger(MainForm.class);
    private Configuration appConfiguration;
    private InputProcessor inputProcessor;

    public MainForm() {
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
            return ConfigurationManager.getConfiguration(CommonConstants.DEFAULT_CONFIG_PATH);
        } catch (IOException e) {
            log.error("Error occurred while reading the system configuration ", e);
            System.exit(-1);
        }
        return null;
    }

    private InputProcessor generateInputProcessor() {
        if(this.appConfiguration == null) {
            throw new IllegalStateException("Application Configuration is empty. Unable to process");
        }
        Map<String, String> headerMap = this.appConfiguration.getHeaderMapping();
        int attributeCount = this.appConfiguration.getAttributeCount();
        String[] inputHeaders = new String[headerMap.size()];
        inputHeaders[0] = headerMap.get("id");
        inputHeaders[1] = headerMap.get("name");
        inputHeaders[2] = headerMap.get("type");

        for(int i = 3; i < 3 + attributeCount; ++i) {
            inputHeaders[i] = headerMap.get("attribute" + i);
        }
        return InputProcessor.getInputProcessor(inputHeaders);
    }

    private void runButtonActionPerformed(ActionEvent e) {
        System.out.println("Hello");
    }

    private void createUIComponents() {
        createConfigPanel();
    }

    private void createConfigPanel() {
//        try {
//            Map<String, Integer> header = InputProcessor
//                    .readHeader(this.appConfiguration.getPopulationData(), this.appConfiguration.getExcluded());
//            attributeConfigPanel = new JPanel(new GridLayout(2, header.size(), 10, 5));
//            for (Map.Entry<String, Integer> entry : header.entrySet()) {
//                JLabel label = new JLabel(entry.getKey());
//                JTextField textField = new JTextField();
//                attributeConfigPanel.add(label);
//                attributeConfigPanel.add(textField);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Madawa Soysa
        createUIComponents();

        frame = new JFrame();
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        teamConfigPanel = new JPanel();
        engineersLabel = new JLabel();
        engineersValue = new JTextField();
        baLabel = new JLabel();
        baValue = new JTextField();
        qaLabel = new JLabel();
        qaValue = new JTextField();
        attributeConfigPane = new JScrollPane();
        attributeConfigPane.add(attributeConfigPanel);
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

                // JFormDesigner evaluation mark
                dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

                dialogPane.setLayout(new BorderLayout());

                //======== contentPanel ========
                {
                    contentPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));

                    //======== teamConfigPanel ========
                    {
                        teamConfigPanel.setLayout(new GridLayout(1, 6));

                        //---- engineersLabel ----
                        engineersLabel.setText("Engineers");
                        teamConfigPanel.add(engineersLabel);
                        teamConfigPanel.add(engineersValue);

                        //---- baLabel ----
                        baLabel.setText("Business Analysts");
                        teamConfigPanel.add(baLabel);
                        teamConfigPanel.add(baValue);

                        //---- qaLabel ----
                        qaLabel.setText("QA Engineers");
                        teamConfigPanel.add(qaLabel);
                        teamConfigPanel.add(qaValue);
                    }
                    contentPanel.add(teamConfigPanel, new GridConstraints(0, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //======== attributeConfigPane ========
                    {
                        attributeConfigPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

                        //======== attributeConfigPanel ========
                        {
                            attributeConfigPanel.setLayout(new GridLayout());
                        }
                        attributeConfigPane.setViewportView(attributeConfigPanel);
                    }
                    contentPanel.add(attributeConfigPane, new GridConstraints(1, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //======== textAreaScrollPane ========
                    {

                        //---- console ----
                        console.setTabSize(4);
                        console.setText("Console...");
                        textAreaScrollPane.setViewportView(console);
                    }
                    contentPanel.add(textAreaScrollPane, new GridConstraints(2, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED,
                        GridConstraints.SIZEPOLICY_CAN_GROW,
                        new Dimension(300, 300), new Dimension(300, 300), null, 0, true));
                }
                dialogPane.add(contentPanel, BorderLayout.CENTER);

                //======== buttonBar ========
                {
                    buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                    buttonBar.setLayout(new GridBagLayout());
                    ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                    ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                    //---- runButton ----
                    runButton.setText("Run");
                    runButton.addActionListener(e -> runButtonActionPerformed(e));
                    buttonBar.add(runButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                dialogPane.add(buttonBar, BorderLayout.SOUTH);
            }
            frameContentPane.add(dialogPane, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(frame.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public static void main(String[] args) {

        MainForm mainForm = new MainForm();
        mainForm.frame.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Madawa Soysa
    private JFrame frame;
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel teamConfigPanel;
    private JLabel engineersLabel;
    private JTextField engineersValue;
    private JLabel baLabel;
    private JTextField baValue;
    private JLabel qaLabel;
    private JTextField qaValue;
    private JScrollPane attributeConfigPane;
    private JPanel attributeConfigPanel;
    private JScrollPane textAreaScrollPane;
    private JTextArea console;
    private JPanel buttonBar;
    private JButton runButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
