/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2015 MeBigFatGuy.com
 * Copyright 2009-2015 Dave Brosius
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

import java.io.Serializable;

/**
 * the score of an generated image compared to the target image
 */
public interface Score extends Cloneable, Serializable {
    /**
     * returns the square of the sum of errors of all pixels
     *
     * @return the pixel error
     */
    long getDelta();

    /**
     * clones this score
     *
     * @return a clone of this score
     */
    Score clone();
}
