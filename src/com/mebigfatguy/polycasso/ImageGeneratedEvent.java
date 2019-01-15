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

import java.awt.Image;
import java.util.EventObject;

/**
 * an event object that is fired when a new test image has been found that is 
 * the best so far.
 */
public class ImageGeneratedEvent extends EventObject {

	private static final long serialVersionUID = -7067803452935840915L;
	private transient Image bestImage;
	
	/**
	 * creates the event object with the source of the event as well as the image
	 * that is now the best image found.
	 * 
	 * @param source the object that generated this event (an image generator)
	 * @param image the best image found so far
	 */
	public ImageGeneratedEvent(Object source, Image image) {
		super(source);
		bestImage = image;
	}
	
	/**
	 * retrieve the best image as described by this event
	 * 
	 * @return the best image
	 */
	public Image getImage() {
		return bestImage;
	}
}
