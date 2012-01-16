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

/**
 * an interface that interested parties should implement if they wish to be
 * informed when a new best image has been found.
 */
public interface ImageGeneratedListener {

	/**
	 * a method to be implement that will give the interested party what image
	 * is now considered to be the best
	 * 
	 * @param event the event object describing the new best image
	 */
	public void imageGenerated(ImageGeneratedEvent event);
}
