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
 * an enum that describes the result of an attempted improvement on an image
 */
public enum ImprovementResult {
    /**
     * this improvement is better than last generations best image
     */
	BEST,
	/**
	 * this improvement is better than the worst elite image from last generation
	 */
	ELITE,
	/**
	 * this improvement didn't improve any on the elite set from last generation
	 */
	FAIL;
}
