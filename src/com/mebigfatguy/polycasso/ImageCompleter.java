/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2012 MeBigFatGuy.com
 * Copyright 2009-2012 Dave Brosius
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * a class that transforms the current best polygon representation to the real image thru
 * a time lapse blend transformation of the images
 */
public class ImageCompleter implements Runnable {
	private static final int NUMTRANSITIONS = 500;
	private static final int TRANSITION_DELAY = 80;
	
	private ImageGenerator imageGenerator;
	private BufferedImage targetImage;
	private BufferedImage srcImage;
	private Dimension imageSize;
	
	/**
	 * create a completer to move the image from polygon form to real form
	 * 
	 * @param generator the image generator that this completer is working for
	 * @param image the target image that will eventually be drawn
	 * @param bestData the best approximation the program reached
	 * @param size the image size
	 */
	
	public ImageCompleter(ImageGenerator generator, BufferedImage image, PolygonData[] bestData, Dimension size) {
		imageGenerator = generator;
		targetImage = image;
		imageSize = size;
		
		srcImage = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = (Graphics2D)srcImage.getGraphics();
		try {
    		Composite srcOpaque = AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
    		g2d.setColor(Color.BLACK);
    		g2d.setComposite(srcOpaque);
    		g2d.fillRect(0, 0, imageSize.width, imageSize.height);
    		
    		for (PolygonData pd : bestData) {
    			pd.draw(g2d);
    		}
		} finally {
		    g2d.dispose();
		}
	}
	
	/**
	 * implements the Runnable interface to gradually fade in the real image over time
	 */
	@Override
	public void run() {
		try {
			WritableRaster sRaster = srcImage.getRaster();
			DataBufferByte sDBB = (DataBufferByte)sRaster.getDataBuffer();
			byte[] sData = sDBB.getData();
			
			WritableRaster tRaster = targetImage.getRaster();
			DataBufferByte tDBB = (DataBufferByte)tRaster.getDataBuffer();
			byte[] tData = tDBB.getData();
			
			int transition = 0;
			do {
				Thread.sleep(TRANSITION_DELAY);
				transition++;
				
				BufferedImage intermediateImage = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);
				WritableRaster iRaster=  intermediateImage.getRaster();
				DataBufferByte iDBB = (DataBufferByte)iRaster.getDataBuffer();
				byte[] iData = iDBB.getData();
				
				for (int i = 0; i < sData.length; ) {
					iData[i++] = (byte)0xFF;
					for (int c = 0; c < 3; c++) {
						int sC = sData[i] & 0x00FF;
						int tC = tData[i] & 0x00FF;
						int iC = sC + (((tC - sC) * transition) / NUMTRANSITIONS);
						iData[i] = (byte)iC;
						++i;
					}
				}
				imageGenerator.fireImageGenerated(intermediateImage);

			} while ((transition < NUMTRANSITIONS) && !Thread.interrupted());
		} catch (InterruptedException ie) {
		}
	}
}
