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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * an immutable calculater of a generated images score compared to a target image
 */
public interface Feedback {

    /**
     * sets the target image to compare against
     * @param targetImage the real target image
     */
    void setTargetImage(BufferedImage targetImage);
    
    /**
     * calculates the score of a generated image against a target image, as 
     * the sum of the square of the pixel error
     * 
     * @param testImage the generated image to test
     * @param sourceScore the score of the parent test image from which this test image was generated
     * @param changedArea the area of changed between the parent generated image and this one
     * 
     * @return the score of this generated image
     */
    Score calculateScore(BufferedImage testImage, Score sourceScore, Rectangle changedArea);
}
