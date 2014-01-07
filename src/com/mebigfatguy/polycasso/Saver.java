/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2014 MeBigFatGuy.com
 * Copyright 2009-2014 Dave Brosius
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
import java.io.IOException;

/**
 * describes how the polygon data is to be save to a file in any one of a number of formats
 */
public interface Saver {
	
	/**
	 * saves the set of polygons to some file
	 *  
	 * @param fileName the name of the file to write to
	 * @param imageSize the dimension of the image
	 * @param data the polygons to draw
	 * 
	 * @throws IOException if the file can't be saved
	 */
	void save(String fileName, Dimension imageSize, PolygonData[] data) throws IOException;
}
