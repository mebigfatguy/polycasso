/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2011 MeBigFatGuy.com
 * Copyright 2009-2011 Dave Brosius
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

import java.awt.Dimension;
import java.io.Serializable;

/**
 * a simple java bean holding settings that are used to control how
 * Polycasso runs
 */
public class Settings implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 8021543124907733235L;
	
	private int generationSize;
	private int eliteSize;
	private boolean useAnnealing;
	private double startTemperature;
	private double coolingRate;
	private Dimension maxImageSize;
	private int maxPolygons;
	private int maxPoints;
	private int maxPtMovement;
	private int maxColorChange;
	
	/**
	 * constructs a settings object with rational defaults
	 */
	public Settings() {
		generationSize = 40;
		eliteSize = 10;
		useAnnealing = true;
		startTemperature = 10;
		coolingRate = 0.01;
		maxImageSize = new Dimension(800, 600);
		maxPolygons = 100;
		maxPoints = 7;
		maxPtMovement = 20;
		maxColorChange = 40;
	}
	
	/**
	 * clones the Settings object
	 * 
	 * @return a new copy of the settings object
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException cnse) {
			return new Settings();
		}
	}

	/**
	 * sets the generation size
	 * 
	 * @param generationSz the size of each generation
	 */
	public void setGenerationSize(int generationSz) {
		generationSize = generationSz;
	}
	
	/**
	 * gets the generation size
	 * 
	 * @return the generation size
	 */
	public int getGenerationSize() {
		return generationSize;
	}
	
	/**
	 * sets the elite size, the number of members that are copied from one generation to another
	 * 
	 * @param eliteSz the elite size
	 */
	public void setEliteSize(int eliteSz) {
		eliteSize = eliteSz;
	}
	
	/**
	 * gets the elite size, the number of members that are copied from one generation to another
	 * 
	 * @return the elite size
	 */
	public int getEliteSize() {
		return eliteSize;
	}
	
	/**
	 * sets whether to use simulated annealing
	 * 
	 * @param annealing whether to use annealing
	 */
	public void setUseAnnealing(boolean annealing) {
		useAnnealing = annealing;
	}
	
	/**
	 * gets whether to use simulating annealing
	 * 
	 * @return whether to use simulating annealing
	 */
	public boolean isUseAnnealing() {
		return useAnnealing;
	}

	/**
	 * sets the error below which polygon samples can be included in a generation, even if not the best
	 * 
	 * @param startTemp the pixel error cutoff
	 */
	public void setStartTemperature(double startTemp) {
		startTemperature = startTemp;
	}
	
	/**
	 * gets the error below wich polygon samples can be included in a generation, even if not the best
	 * 
	 * @return the pixel error cutoff
	 */
	public double getStartTemperature() {
		return startTemperature;
	}

	/**
	 * sets how quickly the error cutoff decrements each generation
	 * 
	 * @param coolRate the cutoff decrementor value
	 */
	public void setCoolingRate(double coolRate) {
		coolingRate = coolRate;
	}
	
	/**
	 * gets how quickly the error cutoff decrements each generation
	 * 
	 * @return the cutoff decrementor value
	 */
	public double getCoolingRate() {
		return coolingRate;
	}

	/**
	 * sets the maximum image size
	 * 
	 * @param imageSize the image size
	 */
	public void setMaxImageSize(Dimension imageSize) {
		maxImageSize = imageSize;
	}

	/**
	 * gets the maximum image size
	 * 
	 * @return the image size
	 */
	public Dimension getMaxImageSize() {
		return maxImageSize;
	}

	/**
	 * sets the maximum polygons that can be used to image the picture
	 * 
	 * @param maxPolys the maximum number of polygons
	 */
	public void setMaxPolygons(int maxPolys) {
		maxPolygons = maxPolys;
	}

	/**
	 * gets the maximum polygons that can be used to image the picture
	 * 
	 * @return the maximum number of polygons
	 */
	public int getMaxPolygons() {
		return maxPolygons;
	}

	/**
	 * sets the maximum number of points per polygon
	 * 
	 * @param maxPts the maximum polygon points
	 */
	public void setMaxPoints(int maxPts) {
		maxPoints = maxPts;
	}

	/**
	 * gets the maximum number of points per polygon
	 * 
	 * @return the maximum polygon points
	 */
	public int getMaxPoints() {
		return maxPoints;
	}

	/**
	 * sets the largest movement that any polygon point can make in one
	 * improvement attempt
	 * 
	 * @param maxPointMovement the maximum allowed movement
	 */
	public void setMaxPtMovement(int maxPointMovement) {
		maxPtMovement = maxPointMovement;
	}

	/**
	 * gets the largest movement that any polygon point can make in one
	 * improvement attempt
	 * 
	 * @return the maximum allowed movement
	 */
	public int getMaxPtMovement() {
		return maxPtMovement;
	}

	/**
	 * sets the maximum color component change
	 * 
	 * @param maxColorChg the max color change
	 */
	public void setMaxColorChange(int maxColorChg) {
		maxColorChange = maxColorChg;
	}

	/**
	 * gets the maximum color component change
	 * 
	 * @return the max color change
	 */
	public int getMaxColorChange() {
		return maxColorChange;
	}
}
