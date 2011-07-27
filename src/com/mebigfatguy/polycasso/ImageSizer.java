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

import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 * ensures that the fetching of the width or height of an image will always return a non negative number
 */
public class ImageSizer implements ImageObserver {

	private Object lock = new Object();
	private int imageWidth = -1;
	private int imageHeight = -1;
	
	/**
	 * constructs an image sizer for the specified image
	 * 
	 * @param loadedImage the image to get the width and height of
	 */
	public ImageSizer(Image loadedImage) {
		imageWidth = loadedImage.getWidth(this);
		imageHeight = loadedImage.getHeight(this);
	}

	/**
	 * implements the callback to collect the width and height of the image
	 * 
	 * @param img the image that is being loaded
	 * @param infoflags flags specifying what has changed
	 * @param x the horizontal position
	 * @param y the vertical position
	 * @param width the width of the image
	 * @param height the height of the image
	 * 
	 * @return whether further processing is desired
	 */
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		synchronized(lock) {
			if ((infoflags & ImageObserver.WIDTH) != 0)
				imageWidth = width;
			if ((infoflags & ImageObserver.HEIGHT) != 0)
				imageHeight = height;
			
			lock.notifyAll();
			return (imageWidth < 0) || (imageHeight < 0);
		}
	}
	
	/**
	 * get the width of the image, waiting if necessary
	 * 
	 * @return the width of the image
	 */
	public int getWidth() {
		synchronized(lock) {
			try {
				while (imageWidth < 0) {
					lock.wait();
				}
			} catch (InterruptedException ie) {
			}
			return imageWidth;
		}
	}
	
	/**
	 * get the height of the image, waiting if necessary
	 * 
	 * @return the height of the image
	 */
	public int getHeight() {
		synchronized(lock) {
			try {
				while (imageHeight < 0) {
					lock.wait();
				}
			} catch (InterruptedException ie) {
			}
			return imageHeight;
		}
	}
}
