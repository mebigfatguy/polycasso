/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2015 MeBigFatGuy.com
 * Copyright 2009-2015 Dave Brosius
 * Inspired by work by Roger Alsing
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mebigfatguy.polycasso;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * a simple dialog to allow for the editing of various settings used to
 * control how Polycasso works.
 */
public class SettingsDialog extends JDialog {

    private static final long serialVersionUID = 5044661806014080056L;

    Settings dlgSettings;
    private JButton resetButton;
    private JButton okButton;
    private JButton cancelButton;
    private JTextField generationSizeField;
    private JTextField eliteSizeField;
    private JCheckBox useAnnealingButton;
    private JTextField startTemperatureField;
    private JTextField coolingRateField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField maxPolygonField;
    private JTextField maxPolygonPointsField;
    private JTextField maxPtMoveField;
    private JTextField maxColorChangeField;
    private SelectAllFocuser focuser;
    private boolean isOK;


    /**
     * constructs the dialog using the passed in settings to set default values
     * 
     * @param settings the default values for settings
     */
    public SettingsDialog(Settings settings) {
        setTitle(PolycassoBundle.getString(PolycassoBundle.Key.Settings));
        dlgSettings = (Settings)settings.clone();
        initComponents();
        initListeners();
        isOK = false;
    }

    /**
     * did the user click the ok button
     * 
     * @return if the ok button was clicked
     */
    public boolean isOK() {
        return isOK;
    }

    /**
     * retrieves the settings set in the dialog by the user
     * 
     * @return the updated settings
     */
    public Settings getSettings() {
        return dlgSettings;
    }

    /**
     * creates and layouts the components
     */
    private void initComponents() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(4, 4));

        focuser = new SelectAllFocuser();

        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(createGeneticsPanel());
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(createOptionsPanel());

        cp.add(centerPanel, BorderLayout.CENTER);
        cp.add(createControlPanel(), BorderLayout.SOUTH);
        pack();
    }

    /**
     * creates the options (settings) panel
     * 
     * @return the options panel
     */
    private JPanel createOptionsPanel() {
        JPanel optPanel = new JPanel();
        optPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(PolycassoBundle.getString(PolycassoBundle.Key.ImageOptions)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        optPanel.setLayout(new FormLayout("pref, 3dlu, 100px, 5dlu, pref, 3dlu, 100px", "pref, 1dlu, pref, 15dlu, pref, 1dlu, pref, 1dlu, pref, 1dlu, pref"));
        CellConstraints cc = new CellConstraints();

        JLabel maxSizeLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.MaxImageSize));
        optPanel.add(maxSizeLabel, cc.xyw(1, 1, 7));

        JLabel widthLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.Width));
        optPanel.add(widthLabel, cc.xy(1, 3));
        widthField = new JTextField(4);
        widthField.setDocument(new IntegerDocument());
        widthLabel.setLabelFor(widthField);
        optPanel.add(widthField, cc.xy(3, 3));
        widthField.addFocusListener(focuser);

        JLabel heightLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.Height));
        optPanel.add(heightLabel, cc.xy(5, 3));
        heightField = new JTextField(4);
        heightField.setDocument(new IntegerDocument());
        heightLabel.setLabelFor(heightField);
        optPanel.add(heightField, cc.xy(7, 3));
        heightField.addFocusListener(focuser);

        JLabel maxPolyLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.MaximumPolygons));
        optPanel.add(maxPolyLabel, cc.xyw(1, 5, 5));
        maxPolygonField = new JTextField(4);
        maxPolygonField.setDocument(new IntegerDocument());
        maxPolyLabel.setLabelFor(maxPolygonField);
        optPanel.add(maxPolygonField, cc.xy(7, 5));
        maxPolygonField.addFocusListener(focuser);

        JLabel maxPolyPointLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.MaximumPolygonPoints));
        optPanel.add(maxPolyPointLabel, cc.xyw(1, 7, 7));
        maxPolygonPointsField = new JTextField(4);
        maxPolygonPointsField.setDocument(new IntegerDocument());
        maxPolyPointLabel.setLabelFor(maxPolygonPointsField);
        optPanel.add(maxPolygonPointsField, cc.xy(7, 7));
        maxPolygonPointsField.addFocusListener(focuser);

        JLabel maxPtMoveLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.MaximumPointMovement));
        optPanel.add(maxPtMoveLabel, cc.xyw(1, 9, 5));
        maxPtMoveField = new JTextField(4);
        maxPtMoveField.setDocument(new IntegerDocument());
        maxPtMoveLabel.setLabelFor(maxPtMoveField);
        optPanel.add(maxPtMoveField, cc.xy(7, 9));
        maxPtMoveField.addFocusListener(focuser);

        JLabel maxColorChangeLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.MaximumColorChange));
        optPanel.add(maxColorChangeLabel, cc.xyw(1, 11, 5));
        maxColorChangeField = new JTextField(4);
        maxColorChangeField.setDocument(new IntegerDocument());
        maxColorChangeLabel.setLabelFor(maxColorChangeField);
        optPanel.add(maxColorChangeField, cc.xy(7, 11));
        maxColorChangeField.addFocusListener(focuser);

        populateValues();

        return optPanel;
    }

    private JPanel createGeneticsPanel() {

        JPanel geneticsPanel  = new JPanel();
        geneticsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(PolycassoBundle.getString(PolycassoBundle.Key.GeneticsOptions)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        geneticsPanel.setLayout(new FormLayout("6dlu, pref, 3dlu, 100px, 3dlu", "pref, 1dlu, pref, 1dlu, pref, 1dlu, pref, 1dlu, pref, 1dlu, pref, 1dlu, pref"));
        CellConstraints cc = new CellConstraints();

        JLabel generationSizeLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.GenerationSize));
        geneticsPanel.add(generationSizeLabel, cc.xyw(1, 1, 2));

        generationSizeField = new JTextField(4);
        generationSizeField.setToolTipText(PolycassoBundle.getString(PolycassoBundle.Key.GenerationSizeToolTip));
        generationSizeField.setDocument(new IntegerDocument());
        generationSizeLabel.setLabelFor(generationSizeField);
        geneticsPanel.add(generationSizeField, cc.xy(4, 1));
        generationSizeField.addFocusListener(focuser);

        JLabel eliteSizeLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.EliteSize));
        geneticsPanel.add(eliteSizeLabel, cc.xyw(1, 3, 2));

        eliteSizeField = new JTextField(4);
        eliteSizeField.setToolTipText(PolycassoBundle.getString(PolycassoBundle.Key.EliteSizeToolTip));
        eliteSizeField.setDocument(new IntegerDocument());
        eliteSizeLabel.setLabelFor(eliteSizeField);
        geneticsPanel.add(eliteSizeField, cc.xy(4, 3));
        eliteSizeField.addFocusListener(focuser);

        useAnnealingButton = new JCheckBox(PolycassoBundle.getString(PolycassoBundle.Key.UseAnnealing));
        useAnnealingButton.setToolTipText(PolycassoBundle.getString(PolycassoBundle.Key.UseAnnealingToolTip));
        geneticsPanel.add(useAnnealingButton, cc.xyw(1, 5, 5));

        JLabel startTemperatureLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.StartTemperature));
        geneticsPanel.add(startTemperatureLabel, cc.xy(2, 7));

        startTemperatureField = new JTextField(4);
        startTemperatureField.setToolTipText(PolycassoBundle.getString(PolycassoBundle.Key.StartTemperatureToolTip));
        startTemperatureField.setDocument(new DoubleDocument());
        startTemperatureLabel.setLabelFor(startTemperatureField);
        geneticsPanel.add(startTemperatureField, cc.xy(4, 7));
        startTemperatureField.addFocusListener(focuser);

        JLabel coolingRateLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.CoolingRate));
        geneticsPanel.add(coolingRateLabel, cc.xy(2, 9));

        coolingRateField = new JTextField(4);
        coolingRateField.setToolTipText(PolycassoBundle.getString(PolycassoBundle.Key.CoolingRateToolTip));
        coolingRateField.setDocument(new DoubleDocument());
        coolingRateLabel.setLabelFor(coolingRateField);
        geneticsPanel.add(coolingRateField, cc.xy(4, 9));
        coolingRateField.addFocusListener(focuser);

        return geneticsPanel;
    }

    private void populateValues() {
        generationSizeField.setText(String.valueOf(dlgSettings.getGenerationSize()));
        eliteSizeField.setText(String.valueOf(dlgSettings.getEliteSize()));
        boolean enable = dlgSettings.isUseAnnealing();
        useAnnealingButton.setSelected(enable);
        startTemperatureField.setEnabled(enable);
        startTemperatureField.setText(String.valueOf(dlgSettings.getStartTemperature()));
        coolingRateField.setEnabled(enable);
        coolingRateField.setText(String.valueOf(dlgSettings.getCoolingRate()));
        widthField.setText(String.valueOf(dlgSettings.getMaxImageSize().width));
        heightField.setText(String.valueOf(dlgSettings.getMaxImageSize().height));
        maxPolygonField.setText(String.valueOf(dlgSettings.getMaxPolygons()));
        maxPolygonPointsField.setText(String.valueOf(dlgSettings.getMaxPoints()));
        maxPtMoveField.setText(String.valueOf(dlgSettings.getMaxPtMovement()));
        maxColorChangeField.setText(String.valueOf(dlgSettings.getMaxColorChange()));
    }

    /**
     * creates the control panel
     * 
     * @return the control panel
     */
    private JPanel createControlPanel() {
        JPanel ctrlPanel = new JPanel();
        ctrlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.X_AXIS));

        resetButton = new JButton(PolycassoBundle.getString(PolycassoBundle.Key.Reset));
        okButton = new JButton(PolycassoBundle.getString(PolycassoBundle.Key.OK));
        cancelButton = new JButton(PolycassoBundle.getString(PolycassoBundle.Key.Cancel));

        SwingUtils.sizeUniformly(resetButton, okButton, cancelButton);

        ctrlPanel.add(Box.createHorizontalStrut(10));
        ctrlPanel.add(resetButton);
        ctrlPanel.add(Box.createHorizontalGlue());
        ctrlPanel.add(Box.createHorizontalStrut(10));
        ctrlPanel.add(okButton);
        ctrlPanel.add(Box.createHorizontalStrut(10));
        ctrlPanel.add(cancelButton);
        ctrlPanel.add(Box.createHorizontalStrut(10));
        return ctrlPanel;
    }

    /**
     * sets up all the control listeners for the dialog
     */
    private void initListeners() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        useAnnealingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean enable = useAnnealingButton.isSelected();
                startTemperatureField.setEnabled(enable);
                coolingRateField.setEnabled(enable);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dlgSettings = new Settings();
                populateValues();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dlgSettings.setGenerationSize(Integer.parseInt(generationSizeField.getText()));
                dlgSettings.setEliteSize(Integer.parseInt(eliteSizeField.getText()));
                dlgSettings.setUseAnnealing(useAnnealingButton.isSelected());
                dlgSettings.setStartTemperature(Double.parseDouble(startTemperatureField.getText()));
                dlgSettings.setCoolingRate(Double.parseDouble(coolingRateField.getText()));
                dlgSettings.setMaxImageSize(new Dimension(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText())));
                dlgSettings.setMaxPolygons(Integer.parseInt(maxPolygonField.getText()));
                dlgSettings.setMaxPoints(Integer.parseInt(maxPolygonPointsField.getText()));
                dlgSettings.setMaxPtMovement(Integer.parseInt(maxPtMoveField.getText()));
                dlgSettings.setMaxColorChange(Integer.parseInt(maxColorChangeField.getText()));
                if (validateSettings()) {
                    isOK = true;
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isOK = false;
                dispose();
            }
        });
    }

    /**
     * a class that selects the text component's text when focus is gained
     */
    private static class SelectAllFocuser implements FocusListener {

        /**
         * implements the listener to select all the text
         * 
         * @param fe the focus event
         */
        @Override
        public void focusGained(FocusEvent fe) {
            JTextComponent comp = (JTextComponent)fe.getSource();
            comp.selectAll();
        }

        /**
         * unused
         * 
         * @param fe the focus event
         */
        @Override
        public void focusLost(FocusEvent fe) {
        }
    }

    /**
     * makes sure that settings selected are rational, and warns otherwise
     * 
     * @return whether the settings are valid
     */
    private boolean validateSettings() {
        if (dlgSettings.getGenerationSize() < 10) {
            generationSizeField.setText("10");
            generationSizeField.requestFocus();
        } else if (dlgSettings.getEliteSize() < 2) {
            eliteSizeField.setText("2");
            eliteSizeField.requestFocus();
        } else if (dlgSettings.getStartTemperature() > 255) {
            startTemperatureField.setText("255.0");
            startTemperatureField.requestFocus();
        } else if (dlgSettings.getCoolingRate() > 40) {
            coolingRateField.setText("40.0");
            coolingRateField.requestFocus();
        } else if (dlgSettings.getMaxImageSize().width < 10) {
            widthField.setText("10");
            widthField.requestFocus();
        } else if (dlgSettings.getMaxImageSize().height < 10) {
            heightField.setText("10");
            heightField.requestFocus();
        } else if (dlgSettings.getMaxPolygons() < 10) {
            maxPolygonField.setText("10");
            maxPolygonField.requestFocus();
        } else if (dlgSettings.getMaxPoints() < 3) {
            maxPolygonPointsField.setText("3");
            maxPolygonPointsField.requestFocus();
        } else if (dlgSettings.getMaxPtMovement() < 5) {
            maxPtMoveField.setText("5");
            maxPtMoveField.requestFocus();
        } else if (dlgSettings.getMaxColorChange() < 5) {
            maxColorChangeField.setText("5");
            maxColorChangeField.requestFocus();
        } else {
            return true;
        }

        JOptionPane.showMessageDialog(this, PolycassoBundle.getString(PolycassoBundle.Key.BadSetting));
        return false;

    }
}
