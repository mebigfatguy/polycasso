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

/**
 * an enumeration of all the different improvement types that an ImageGenerator can attempt
 * to make to an image.
 */
public enum ImprovementType { 
	/**
	 * add a new polygon to the list being rendered
	 */
	AddPolygon,
	/**
	 * remove a polygon from the list being rendered
	 */
	RemovePolygon,
	/**
	 * add a point to a random existing polygon
	 */
	AddPoint, 
	/**
	 * remove a point from a random existing polygon
	 */
	RemovePoint, 
	/**
	 * move a point from a random existing polygon
	 */
	MovePoint,
	/**
	 * pick a point at random and align it horizontally or vertically with a
	 * neighboring point for an existing polygon
	 */
	RectifyPoint,
	/**
	 * change the z order of a random existing polygon
	 */
	ReorderPoly, 
	/**
	 * adjust the points of the polygon so they move towards the centroid of
	 * the polygon
	 */
	ShrinkPoly, 
	/**
	 * adjust the points of the polygon so they move away from the centroid of
	 * the polygon
	 */
	EnlargePoly, 
	/**
	 * translate the points of the polygon in an x or y direction
	 */
	ShiftPoly,
	/**
	 * adjust a component of the color of a random existing polygon
	 */
	ChangeColor,
	/**
	 * adjust the transparency of a random existing polygon
	 */
	ChangeAlpha, 
	/**
	 * change the color to white
	 */
	White,
	/**
	 * change the color to black
	 */
	Black,
	/**
	 * combine two  sets of polygons
	 */
	Breed,
	/**
	 * combine two sets of polygons one from an elite
	 */
	BreedElite,
	/**
	 * completely change all attributes of a random existing polygon
	 */
	CompleteChange;

}
