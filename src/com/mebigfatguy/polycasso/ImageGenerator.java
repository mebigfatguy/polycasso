/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2013 MeBigFatGuy.com
 * Copyright 2009-2013 Dave Brosius
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
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * interface that generates test images iteratively looking for the best image that matches a target.
 */

public interface ImageGenerator {
    
    /**
     * starts up threads to start looking for images that are closest to the target
     */
    void startGenerating();
    
    /**
     * shuts down threads that were looking for images
     */
    void stopGenerating();
    
    /**
     * retrieves the scaled target iamge
     * 
     * @return the target image
     */
    BufferedImage getTargetImage();
    
    /**
     * returns the image size that is being generated. This size might be different the original image
     * if the size is bigger then the max setting.
     * 
     * @return the image size
     */
    Dimension getImageSize();  
    
    /**
     * retrieves the best set of polygons for drawing the image so far
     * 
     * @return the best set of polygons
     */
    PolygonData[] getBestData();
    
    /**
     * completes the image by transforming the polygon image to the real image
     */
    void complete();
    
    /**
     * allows interested parties to register to receive events when a new best image has been
     * found.
     * 
     * @param listener the listener that is interested in events
     */
    void addImageGeneratedListener(ImageGeneratedListener listener);
    
    /**
     * allows uninterested parties to unregister to receive events when a new best image is 
     * found
     * 
     * @param listener the listener that is no longer needed
     */
    void removeImageGeneratedListener(ImageGeneratedListener listener);
    
    /**
     * informs all listeners that a new best image has been found
     * 
     * @param image the new best image
     */
    void fireImageGenerated(Image image);
}
