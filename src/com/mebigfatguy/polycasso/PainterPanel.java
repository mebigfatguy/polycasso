/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2017 MeBigFatGuy.com
 * Copyright 2009-2017 Dave Brosius
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * the panel that holds the image considered to be the best representation of the target
 * image so far.
 */
public class PainterPanel extends JPanel {

	private static final long serialVersionUID = -4005448126783525299L;
	private final Object lock = new Object();
	private Image targetImage;
	private Image bestImage;
	
	/**
	 * set the ideal image that we are trying to generate. It is only shown while in debug mode
	 * @see com.mebigfatguy.polycasso.Polycasso#DEBUG
	 * 
	 * @param image the target image
	 */
	public void setTarget(Image image) {
		synchronized(lock) {
			targetImage = image;
			setSize(targetImage.getWidth(PainterPanel.this), targetImage.getHeight(PainterPanel.this));				
		}
	}
	
	/**
	 * sets the best generated image thus far.
	 * 
	 * @param image the best generated image
	 */
	public void setImage(Image image) {
		synchronized(lock) {
			bestImage = image;
		}
		invalidate();
		repaint();
	}
	
	/**
	 * overrides the method to redraw this panel, so that the best generated image is shown
	 * 
	 * @param g the graphics object of the panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		synchronized(lock) {
			if (bestImage != null)
				g.drawImage(bestImage, 0, 0, bestImage.getWidth(this), bestImage.getHeight(this), Color.WHITE, this);
			if (Polycasso.DEBUG) { 
				if (targetImage != null)
					g.drawImage(targetImage, 0, targetImage.getHeight(this), targetImage.getWidth(this), targetImage.getHeight(this), Color.WHITE, this);
			}
		}
	}
}
