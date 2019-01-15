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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Random;

/**
 * a class for holding the information for one polygon, including points, color and alpha level.
 */
public class PolygonData implements Cloneable {

    private Color color;
    private float alpha;
    private Polygon polygon;

    /**
     * creates a polygon data structure with required information
     *
     * @param c
     *            the color of the polygon
     * @param xpar
     *            the alpha level of the polygon
     * @param poly
     *            the points of the polygon
     */
    public PolygonData(Color c, float xpar, Polygon poly) {
        color = c;
        alpha = xpar;
        polygon = poly;
    }

    /**
     * retrieve the polygon (points) of this polygondata
     *
     * @return a polygon
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * gets the transparency of this polygon: 0.0 is transparent, 1.0 is opaque
     * 
     * @return the transparency value
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * sets the transparency of this polygon: 0.0 is transparent, 1.0 is opaque
     * 
     * @param xpar
     *            the transparency value
     */
    public void setAlpha(float xpar) {
        alpha = xpar;
    }

    /**
     * retrieves the color of this polygon
     *
     * @return the polygon color
     */
    public Color getColor() {
        return color;
    }

    /**
     * sets the color of this polygon
     *
     * @param newColor
     *            the new color to use
     */
    public void setColor(Color newColor) {
        color = newColor;
    }

    /**
     * creates a totally random polygon that is limited by the specified size
     *
     * @param size
     *            the maximum size of the bounding box of the polygon
     * @param maxPoints
     *            the maximum number of points to generate
     *
     * @return a random polygon
     */
    public static PolygonData randomPoly(Dimension size, int maxPoints) {
        Random r = new Random();
        Polygon polygon = new Polygon();
        Rectangle polyRect = getPolyBounds(r, size);

        int numPoints = r.nextInt(maxPoints - 3) + 3;
        for (int i = 0; i < numPoints; i++) {
            polygon.addPoint(polyRect.x + r.nextInt(polyRect.width), polyRect.y + r.nextInt(polyRect.height));
        }
        polygon.invalidate();

        Color c = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());

        return new PolygonData(c, r.nextFloat(), polygon);
    }

    /**
     * returns a rectangle that new polygons should points out of. It attempts to allow for creating localized polygons, so that small areas will be generated
     * more likely.
     *
     * @param r
     *            a random generator object
     * @param maxSize
     *            the max size of the image
     *
     * @return a rectangle to pick polygon points out of
     */
    private static Rectangle getPolyBounds(Random r, Dimension maxSize) {

        Rectangle bounds = new Rectangle();

        int polySize = r.nextInt(12);
        if (polySize < 3) {
            bounds.width = 2 + (maxSize.width / 7);
            bounds.height = 2 + (maxSize.height / 7);
        } else if (polySize < 6) {
            bounds.width = 2 + (maxSize.width / 5);
            bounds.height = 2 + (maxSize.height / 5);
        } else if (polySize < 9) {
            bounds.width = 2 + (maxSize.width / 3);
            bounds.height = 2 + (maxSize.height / 3);
        } else {
            bounds.width = maxSize.width;
            bounds.height = maxSize.height;
        }

        bounds.x = r.nextInt((maxSize.width - bounds.width) + 1) - 1;
        bounds.y = r.nextInt((maxSize.height - bounds.height) + 1) - 1;
        return bounds;
    }

    /**
     * clones this polygon data data
     *
     * @return a copy of the polygon data
     */
    @Override
    public PolygonData clone() {
        try {
            PolygonData clone = (PolygonData) super.clone();
            clone.color = new Color(color.getRed(), color.getGreen(), color.getBlue());
            clone.polygon = new Polygon(polygon.xpoints, polygon.ypoints, polygon.npoints);

            return clone;
        } catch (CloneNotSupportedException cnse) {
            return randomPoly(new Dimension(100, 100), 3);
        }
    }

    /**
     * draws this polygondata on a specified graphics object
     *
     * @param g
     *            the graphics object on which to draw this polygon
     */
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.fillPolygon(polygon);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String sep = "";
        for (int i = 0; i < polygon.npoints; i++) {
            sb.append(sep);
            sep = "|";
            sb.append(polygon.xpoints[i]);
            sb.append(",");
            sb.append(polygon.ypoints[i]);
        }
        sb.append(")");
        return sb.toString();
    }
}