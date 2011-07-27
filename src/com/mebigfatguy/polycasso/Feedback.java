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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * an immutable class for processing a test image against target image for closeness.
 */
public class Feedback {

	private byte[] targetBuffer;
	private int size;
	
	/**
	 * creates a feedback object with a given targetImage. Caches the image bytes in 
	 * member variables.
	 * 
	 * @param targetImage the target image that will be the judge of test images
	 */
	public Feedback(BufferedImage targetImage) {
		WritableRaster raster = targetImage.getRaster();
		DataBufferByte dbb = (DataBufferByte)raster.getDataBuffer();
		targetBuffer = dbb.getData();
		size = dbb.getSize();
	}
	
	/**
	 * returns a score of how close the test image is to the target
	 * which is the square of the error to the target image
	 * 
	 * @param testImage the image to score
	 * @return a value that represents its closeness to ideal
	 */
	public long calculateDelta(BufferedImage testImage) {
		WritableRaster raster = testImage.getRaster();
		DataBufferByte dbb = (DataBufferByte)raster.getDataBuffer();
		byte[] testBuffer = dbb.getData();
		
		long error = 0L;
		
		//index 0 is alpha, start at 1 (blue)
		for (int i = 1; i < size; i++) {
			int blue1 = targetBuffer[i] & 0x0FF;
			int blue2 = testBuffer[i++] & 0x0FF;
			long blueError = blue1 - blue2;
			blueError *= blueError;
			
			int green1 = targetBuffer[i] & 0x0FF;
			int green2 = testBuffer[i++] & 0x0FF;
			long greenError = green1 - green2;
			greenError *= greenError;
			
			int red1 = targetBuffer[i] & 0x0FF;
			int red2 = testBuffer[i++] & 0x0FF;
			long redError = red1 - red2;
			redError *= redError;
			
			error += redError + greenError  + blueError;
		}
		
		return error;
	}
}
