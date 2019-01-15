/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2019 MeBigFatGuy.com
 * Copyright 2009-2019 Dave Brosius
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * a simple dialog to allow for the editing of various settings used to control how urls are fetched thru a proxy.
 */
public class ProxyDialog extends JDialog {
    private static final long serialVersionUID = 7454644049634022854L;

    private final Settings dlgSettings;
    private JTextField proxyHostField;
    private JTextField proxyPortField;
    private JButton okButton;
    private JButton cancelButton;
    private boolean isOK;

    /**
     * constructs the dialog using the passed in settings to set default values
     *
     * @param settings
     *            the default values for settings
     */
    public ProxyDialog(Settings settings) {
        setTitle(PolycassoBundle.getString(PolycassoBundle.Key.Proxy));
        dlgSettings = settings.clone();
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
        cp.add(createProxyPanel(), BorderLayout.CENTER);
        cp.add(createControlPanel(), BorderLayout.SOUTH);
        pack();
    }

    /**
     * creates the proxy settings panel
     *
     * @return the proxy panel
     */
    private JPanel createProxyPanel() {
        JPanel proxyPanel = new JPanel();
        proxyPanel.setLayout(new FormLayout("6dlu, pref, 3dlu, 200px:grow, 6dlu", "5dlu, pref, 3dlu, pref, 5dlu"));
        CellConstraints cc = new CellConstraints();

        JLabel hostLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.ProxyHost));
        proxyPanel.add(hostLabel, cc.xy(2, 2));
        proxyHostField = new JTextField();
        proxyPanel.add(proxyHostField, cc.xy(4, 2));
        hostLabel.setLabelFor(proxyHostField);

        JLabel portLabel = new JLabel(PolycassoBundle.getString(PolycassoBundle.Key.ProxyPort));
        proxyPanel.add(portLabel, cc.xy(2, 4));
        proxyPortField = new JTextField();
        proxyPortField.setDocument(new IntegerDocument());
        proxyPanel.add(proxyPortField, cc.xy(4, 4));
        portLabel.setLabelFor(proxyPortField);

        populateValues();

        return proxyPanel;
    }

    private void populateValues() {
        proxyHostField.setText(dlgSettings.getProxyHost() == null ? "" : dlgSettings.getProxyHost());
        proxyPortField.setText(dlgSettings.getProxyPort() == 0 ? "" : String.valueOf(dlgSettings.getProxyPort()));
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

        okButton = new JButton(PolycassoBundle.getString(PolycassoBundle.Key.OK));
        cancelButton = new JButton(PolycassoBundle.getString(PolycassoBundle.Key.Cancel));

        SwingUtils.sizeUniformly(okButton, cancelButton);

        ctrlPanel.add(Box.createHorizontalGlue());
        ctrlPanel.add(okButton);
        ctrlPanel.add(Box.createHorizontalStrut(10));
        ctrlPanel.add(cancelButton);
        ctrlPanel.add(Box.createHorizontalStrut(10));
        return ctrlPanel;
    }

    private void initListeners() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String host = proxyHostField.getText().trim();
                dlgSettings.setProxyHost(host.length() == 0 ? null : host);
                String port = proxyPortField.getText().trim();
                dlgSettings.setProxyPort((port.length() == 0) ? 0 : Integer.parseInt(port));
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
     * makes sure that settings selected are rational, and warns otherwise
     *
     * @return whether the settings are valid
     */
    private boolean validateSettings() {

        if (dlgSettings.getProxyPort() > 65535) {
            proxyPortField.setText("80");
            proxyPortField.requestFocus();
        } else {
            return true;
        }

        JOptionPane.showMessageDialog(this, PolycassoBundle.getString(PolycassoBundle.Key.BadSetting));
        return false;
    }
}
