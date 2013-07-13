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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * a class that applies various improvement attempts to a polygon, attempts to prioritize
 * which algorithms to pick based on what has worked in the past, as well as priorities which
 * polygons have had success being transformed.
 */
public class Improver {
    private final Settings settings;
    private final GenerationHandler generationHandler;
    private final Dimension imageSize;
    private final Random r;
    private List<PolygonData> polygons = null;
    private Rectangle changedArea;
    private GenerationMember changedMember;
    private final ImprovementTypeStats stats;

    /**
     * create an improver using a specified image size
     * @param confSettings the settings to be used
     * @param genHandler the generation handler
     * @param size the size of the image
     */
    public Improver(Settings confSettings, GenerationHandler genHandler, Dimension size) {
        settings = confSettings;
        generationHandler = genHandler;
        imageSize = size;
        stats = new ImprovementTypeStats();
        r = new Random();
    }

    /**
     * get the list of polygons usually after attempted to be improved
     * 
     * @return the list of polygons
     */
    public List<PolygonData> getData() {
        if (polygons == null) {
            return new ArrayList<PolygonData>();
        }
        return Collections.<PolygonData>unmodifiableList(polygons);
    }

    /**
     * updates the stats for types that successfully improved the image
     * 
     * @param type the improvement type that was successful
     * @param successful whether the improvement was successful
     */
    public void typeWasSuccessful(ImprovementType type, boolean successful) {
        stats.typeWasSuccessful(type, successful);
    }

    /**
     * attempts to improve on one polygon randomly by adjusting it according to a randomly
     * selected improvement type
     * 
     * @return the improvement type used to alter the data
     */
    public ImprovementType improveRandomly() {
        changedMember = generationHandler.getRandomMember(false);
        if (changedMember != null) {
            polygons = new ArrayList<PolygonData>(Arrays.<PolygonData>asList(changedMember.getData().clone()));
        } else {
            polygons = new ArrayList<PolygonData>();
        }

        ImprovementType type = (polygons.isEmpty()) ? ImprovementType.AddPolygon : stats.getRandomImprovementType();

        switch (type) {
            case AddPolygon: {
                if (polygons.size() < settings.getMaxPolygons()) {
                    PolygonData pd = PolygonData.randomPoly(imageSize, settings.getMaxPoints());
                    polygons.add(pd);
                    changedArea = pd.getPolygon().getBounds();
                } else {
                    randomCompleteChange();
                    type = ImprovementType.CompleteChange;
                    typeWasSuccessful(ImprovementType.AddPolygon, false);
                }
            }
            break;

            case RemovePolygon: {
                if (polygons.size() > 0) {
                    int idx = r.nextInt(polygons.size());
                    changedArea = polygons.get(idx).getPolygon().getBounds();
                    polygons.remove(idx);
                }
            }
            break;

            case AddPoint: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                Polygon polygon = pd.getPolygon();
                changedArea = polygon.getBounds();
                if (polygon.npoints < settings.getMaxPoints()) {
                    polygon.addPoint(0, 0);
                    int insPos = r.nextInt(polygon.npoints);
                    int lastPt = ((insPos + polygon.npoints) - 1) % polygon.npoints;
                    int maxMovement = settings.getMaxPtMovement();
                    int maxX = Math.max(maxMovement, Math.abs(polygon.xpoints[lastPt] - polygon.xpoints[insPos]));
                    int maxY = Math.max(maxMovement, Math.abs(polygon.ypoints[lastPt] - polygon.ypoints[insPos]));

                    int x = r.nextInt(maxX) + Math.min(polygon.xpoints[lastPt], polygon.xpoints[insPos]);
                    int y = r.nextInt(maxY) + Math.min(polygon.ypoints[lastPt], polygon.ypoints[insPos]);

                    int numCopyPts = polygon.npoints - insPos - 1;
                    System.arraycopy(polygon.xpoints, insPos, polygon.xpoints, insPos + 1, numCopyPts);
                    polygon.xpoints[insPos] = x;
                    System.arraycopy(polygon.ypoints, insPos, polygon.ypoints, insPos + 1, numCopyPts);
                    polygon.ypoints[insPos] = y;
                    polygon.invalidate();
                    changedArea = changedArea.union(polygon.getBounds());
                    polygons.set(idx, pd);

                } else {
                    randomCompleteChange();
                    type = ImprovementType.CompleteChange;
                    typeWasSuccessful(ImprovementType.AddPoint, false);
                }
            }
            break;

            case RemovePoint: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                Polygon polygon = pd.getPolygon();
                changedArea = polygon.getBounds();
                if (polygon.npoints > 3) {
                    int delPos = r.nextInt(polygon.npoints);

                    int numPtCopy = polygon.npoints - delPos - 1;
                    System.arraycopy(polygon.xpoints, delPos+1, polygon.xpoints, delPos, numPtCopy);
                    System.arraycopy(polygon.ypoints, delPos+1, polygon.ypoints, delPos, numPtCopy);
                    polygon.npoints--;
                    polygon.invalidate();
                    changedArea = changedArea.union(polygon.getBounds());
                    polygons.set(idx, pd);
                } else {
                    randomCompleteChange();
                    type = ImprovementType.CompleteChange;
                    typeWasSuccessful(ImprovementType.RemovePoint, false);
                }
            }
            break;

            case MovePoint: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                Polygon polygon = pd.getPolygon();
                changedArea = polygon.getBounds();
                int movePos = r.nextInt(polygon.npoints);
                int maxMovement = settings.getMaxPtMovement();
                int dblMax = maxMovement << 1;
                int moveX = r.nextInt(dblMax) - maxMovement;
                int moveY = r.nextInt(dblMax) - maxMovement;
                polygon.xpoints[movePos] += moveX;
                polygon.ypoints[movePos] += moveY;
                clipToRange(0, imageSize.width, polygon.xpoints[movePos]);
                clipToRange(0, imageSize.height, polygon.ypoints[movePos]);
                polygon.invalidate();
                changedArea = changedArea.union(polygon.getBounds());
                polygons.set(idx, pd);
            }
            break;

            case RectifyPoint: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                Polygon polygon = pd.getPolygon();
                changedArea = polygon.getBounds();
                int rectifyPos = r.nextInt(polygon.npoints);
                int targetPos = (rectifyPos == 0) ? polygon.npoints - 1 : (rectifyPos - 1);

                if (Math.abs(polygon.xpoints[rectifyPos] - polygon.xpoints[targetPos]) <
                        Math.abs(polygon.ypoints[rectifyPos] - polygon.ypoints[targetPos])) {
                    polygon.xpoints[rectifyPos] = polygon.xpoints[targetPos];
                } else {
                    polygon.ypoints[rectifyPos] = polygon.ypoints[targetPos];
                }
                polygon.invalidate();
                changedArea = changedArea.union(polygon.getBounds());
                polygons.set(idx, pd);
            }
            break;

            case ReorderPoly: {
                if (polygons.size() > 2) {
                    PolygonData pd = polygons.remove(r.nextInt(polygons.size()));
                    changedArea = pd.getPolygon().getBounds();
                    polygons.add(r.nextInt(polygons.size()), pd);
                } else {
                    randomCompleteChange();
                    type = ImprovementType.CompleteChange;
                    typeWasSuccessful(ImprovementType.ReorderPoly, false);
                }
            }
            break;

            case ShrinkPoly: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                Polygon polygon = pd.getPolygon();
                changedArea = polygon.getBounds();
                Rectangle bbox = polygon.getBounds();

                double midX = bbox.getCenterX();
                double midY = bbox.getCenterY();

                int shrinkFactor = r.nextInt(settings.getMaxPtMovement());
                for (int i = 0; i < polygon.npoints; i++) {
                    polygon.xpoints[i] += (polygon.xpoints[i] < midX) ? shrinkFactor : -shrinkFactor;
                    polygon.ypoints[i] += (polygon.ypoints[i] < midY) ? shrinkFactor : -shrinkFactor;
                }
                polygon.invalidate();
                changedArea = changedArea.union(polygon.getBounds());
                polygons.set(idx, pd);
            }
            break;

            case EnlargePoly: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                Polygon polygon = pd.getPolygon();
                changedArea = polygon.getBounds();
                Rectangle bbox = polygon.getBounds();
                double midX = bbox.getCenterX();
                double midY = bbox.getCenterY();

                int expandFactor = r.nextInt(settings.getMaxPtMovement());
                for (int i = 0; i < polygon.npoints; i++) {
                    polygon.xpoints[i] += (polygon.xpoints[i] < midX) ? -expandFactor : expandFactor;
                    polygon.ypoints[i] += (polygon.ypoints[i] < midY) ? -expandFactor : expandFactor;
                    polygon.xpoints[i] = clipToRange(0, imageSize.width, polygon.xpoints[i]);
                    polygon.ypoints[i] = clipToRange(0, imageSize.height, polygon.ypoints[i]);
                }
                polygon.invalidate();
                changedArea = changedArea.union(polygon.getBounds());
                polygons.set(idx, pd);
            }
            break;

            case ShiftPoly: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                Polygon polygon = pd.getPolygon();
                changedArea = polygon.getBounds();
                int maxMovement = settings.getMaxPtMovement();
                int dblMax = maxMovement << 1;
                int shiftX = r.nextInt(dblMax) + maxMovement;
                int shiftY = r.nextInt(dblMax) + maxMovement;
                for (int i = 0; i < polygon.npoints; i++) {
                    polygon.xpoints[i] += shiftX;
                    polygon.ypoints[i] += shiftY;
                    polygon.xpoints[i] = clipToRange(0, imageSize.width, polygon.xpoints[i]);
                    polygon.ypoints[i] = clipToRange(0, imageSize.height, polygon.ypoints[i]);
                }
                polygon.invalidate();
                changedArea = changedArea.union(polygon.getBounds());
                polygons.set(idx, pd);
            }
            break;

            case ChangeColor: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                changedArea = pd.getPolygon().getBounds();
                Color color = pd.getColor();
                int comp = r.nextInt(3);
                int maxChange = settings.getMaxColorChange();
                int dblChange = maxChange << 1;
                switch (comp) {
                    case 0: {
                        int newColor = color.getRed() + (r.nextInt(dblChange) - maxChange);
                        newColor = clipToRange(0, 255, newColor);
                        pd.setColor(new Color(newColor, color.getGreen(), color.getBlue()));
                    }
                    break;

                    case 1: {
                        int newColor = color.getGreen() + (r.nextInt(dblChange) - maxChange);
                        newColor = clipToRange(0, 255, newColor);
                        pd.setColor(new Color(color.getRed(), newColor, color.getBlue()));
                    }
                    break;

                    case 2: {
                        int newColor = color.getBlue() + (r.nextInt(dblChange) - maxChange);
                        newColor = clipToRange(0, 255, newColor);
                        pd.setColor(new Color(color.getRed(), color.getGreen(), newColor));
                    }
                    break;
                }
                polygons.set(idx, pd);
            }
            break;

            case White: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                changedArea = pd.getPolygon().getBounds();
                pd.setColor(Color.WHITE);
                pd.setAlpha(1);
                polygons.set(idx, pd);
            }
            break;

            case Black: {
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                changedArea = pd.getPolygon().getBounds();
                pd.setColor(Color.BLACK);
                pd.setAlpha(1);
                polygons.set(idx, pd);
            }
            break;

            case ChangeAlpha:
                int idx = r.nextInt(polygons.size());
                PolygonData pd = (PolygonData)polygons.get(idx).clone();
                changedArea = pd.getPolygon().getBounds();
                pd.setAlpha(r.nextFloat());
                polygons.set(idx, pd);
                break;

            case Breed: {
                GenerationMember copyMember = generationHandler.getRandomMember(false);
                if ((copyMember == null) || (copyMember.getData().length == 0)) {
                    randomCompleteChange();
                } else {
                    PolygonData[] copyData = copyMember.getData();
                    idx = r.nextInt(copyData.length);
                    if (idx >= polygons.size()) {
                        polygons.add(copyData[idx]);
                        changedArea = copyData[idx].getPolygon().getBounds();
                    } else {
                        changedArea = polygons.get(idx).getPolygon().getBounds();
                        polygons.set(idx, copyData[idx]);
                        changedArea = changedArea.union(polygons.get(idx).getPolygon().getBounds());
                    }
                }
            }
            break;

            case BreedElite: {
                GenerationMember copyMember = generationHandler.getRandomMember(true);
                if ((copyMember == null) || (copyMember.getData().length == 0)) {
                    randomCompleteChange();
                } else {
                    PolygonData[] copyData = copyMember.getData();
                    idx = r.nextInt(copyData.length);
                    if (idx >= polygons.size()) {
                        polygons.add(copyData[idx]);
                        changedArea = copyData[idx].getPolygon().getBounds();
                    } else {
                        changedArea = polygons.get(idx).getPolygon().getBounds();
                        polygons.set(idx, copyData[idx]);
                        changedArea = changedArea.union(polygons.get(idx).getPolygon().getBounds());
                    }
                }
            }
            break;

            case CompleteChange: {
                randomCompleteChange();
            }
            break;
        }

        return type;
    }

    /**
     * returns the member image from which this generated image was created
     * 
     * @return the parent generated image member
     */
    public GenerationMember getParentGenerationMember() {
        return changedMember;
    }

    /**
     * returns the area that was changed in this image
     * 
     * @return the rectangular bounds that was changed, or null for a complete change
     */
    public Rectangle getChangedArea() {
        return changedArea;
    }

    /**
     * generates a random polygon change (all values)
     */
    private void randomCompleteChange() {
        int idx = r.nextInt(polygons.size());
        changedArea = polygons.get(idx).getPolygon().getBounds();
        PolygonData randomPoly = PolygonData.randomPoly(imageSize, settings.getMaxPoints());
        changedArea = changedArea.union(randomPoly.getPolygon().getBounds());
        polygons.set(idx, randomPoly);
    }

    /**
     * clip a value between a min and max value
     * 
     * @param min the min value
     * @param max the max value
     * @param value the value to clip
     * 
     * @return the clipped value
     */
    private static int clipToRange(int min, int max, int value) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

}
