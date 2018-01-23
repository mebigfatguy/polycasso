/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2018 MeBigFatGuy.com
 * Copyright 2009-2018 Dave Brosius
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

/**
 * an enum that represents possible save-as types
 */
public enum FileType {

	/**
	 * A portable network graphics file
	 */
	PNG(".png", PolycassoBundle.Key.PNGDescription) {
		@Override
		public Saver getSaver() {
			return new PNGSaver();
		}
	},
	/**
	 * a scalable vector graphics file
	 */
	SVG(".svg", PolycassoBundle.Key.SVGDescription) {
		@Override
		public Saver getSaver() {
			return new SVGSaver();
		}
	},
	/**
	 * a java file
	 */
	Java(".java", PolycassoBundle.Key.JAVADescription) {
		@Override
		public Saver getSaver() {
			return new JavaSaver();
		}
	};
	
	private String extension;
	private PolycassoBundle.Key descriptionKey;
	
	/**
	 * internal constructor for setting the extension and description bundle key
	 * 
	 * @param ext the extension for this file type
	 * @param descKey the bundle description key for this type
	 */
	FileType(String ext, PolycassoBundle.Key descKey) {
		extension = ext;
		descriptionKey = descKey;
	}
	
	/**
	 * returns the extension for this type
	 * 
	 * @return the file name extension
	 */
	public String getExtension() {
		return extension;
	}
	
	/**
	 * returns the description for this file type
	 * 
	 * @return the file type description for the save dialog
	 */
	public String getDescription() {
		return PolycassoBundle.getString(descriptionKey);
	}
	
	/**
	 * the action to save the file
	 * 
	 * @return a saver object that will save the object in the appropriate foramt
	 */
	public abstract Saver getSaver();
}
