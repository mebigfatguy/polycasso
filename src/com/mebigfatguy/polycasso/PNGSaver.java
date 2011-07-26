/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2010 MeBigFatGuy.com
 * Copyright 2009-2010 Dave Brosius
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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

/**
 * generates a png file from the set of polygons
 */
public class PNGSaver implements Saver {

	/**
	 * saves the set of polygons in a png file
	 *  
	 * @param fileName the name of the file to write to
	 * @param imageSize the dimension of the image
	 * @param data the polygons to draw
	 */
	@Override
	public void save(String fileName, Dimension imageSize, PolygonData[] data)
			throws IOException {
		
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(fileName));
			
			BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, imageSize.width, imageSize.height);
			
			for (PolygonData pd : data) {
				pd.draw(g2d);
			}
			
			ImageIO.write(image, "png", bos);	
			
		} finally {
			IOUtils.closeQuietly(bos);
		}
	}

}
