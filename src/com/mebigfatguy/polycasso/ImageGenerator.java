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
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * interface that generates test images iteratively looking for the best image that matches a target.
 */

public interface ImageGenerator {
    
    void startGenerating();
    
    void stopGenerating();
    
    BufferedImage getTargetImage();
    
    Dimension getImageSize();  
    
    PolygonData[] getBestData();
    
    void complete();
    
    void addImageGeneratedListener(ImageGeneratedListener listener);
    
    void removeImageGeneratedListener(ImageGeneratedListener listener);
    
    void fireImageGenerated(Image image);
}
