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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * a simple dialog that's a typical application about box information
 */
public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -686787510942505991L;

	/**
	 * creates the dialog
	 */
	public AboutDialog() {
		setTitle(PolycassoBundle.getString(PolycassoBundle.Key.AboutPolycasso));
		initComponents();
		pack();
	}
	
	/**
	 * initializes the components and lays them out
	 */
	private void initComponents() {
		Container cp = getContentPane();
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createEtchedBorder()));
		mainPanel.setLayout(new BorderLayout(4, 4));
		
		JTextArea info = new JTextArea(PolycassoBundle.getString(PolycassoBundle.Key.Info));
		info.setEditable(false);
		info.setBackground(mainPanel.getBackground());
		mainPanel.add(info, BorderLayout.CENTER);
		
		JPanel cpPanel = new JPanel();
		cpPanel.setLayout(new BoxLayout(cpPanel, BoxLayout.Y_AXIS));
		cpPanel.add(new JLabel("©MeBigFatGuy.com"));
		cpPanel.add(new JLabel("©Dave Brosius"));
		mainPanel.add(cpPanel, BorderLayout.SOUTH);
		cp.add(mainPanel);
	}
}
