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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * class that generates test images iteratively looking for the best image that matches a target.
 * The images are generated from semi-transparent polygons that are improved upon over time.
 * This class generates multiple images in parallel to keep multicore processors busy.
 */
public class DefaultImageGenerator implements ImageGenerator, Runnable {
    private final Set<ImageGeneratedListener> listeners = new HashSet<ImageGeneratedListener>();
    private final Settings settings;
    private final BufferedImage targetImage;
    private GenerationHandler generationHandler;
    private final Dimension imageSize;
    private Feedback feedback;
    private Thread[] t = null;
    private final Object startStopLock = new Object();

    /**
     * creates an ImageGenerator for the given target image, and size
     * @param confSettings the configuration settings
     * @param image the target image
     * @param size the dimension of the image
     */
    public DefaultImageGenerator(Settings confSettings, Image image, Dimension size) {
        settings = confSettings;
        imageSize = trimSize(size, settings.getMaxImageSize());
        targetImage = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics g = targetImage.getGraphics();
        try {
            g.drawImage(image, 0, 0, imageSize.width, imageSize.height, Color.WHITE, null);
            generationHandler = new GenerationHandler(settings, imageSize);
            feedback = new DefaultFeedback();
            feedback.setTargetImage(targetImage);
        } finally {
            g.dispose();
        }
    }

    /**
     * retrieves the scaled target iamge
     * 
     * @return the target image
     */
    @Override
    public BufferedImage getTargetImage() {
        return targetImage;
    }

    /**
     * returns the image size that is being generated. This size might be different the original image
     * if the size is bigger then the max setting.
     * 
     * @return the image size
     */
    @Override
    public Dimension getImageSize() {
        return imageSize;
    }

    /**
     * allows interested parties to register to receive events when a new best image has been
     * found.
     * 
     * @param listener the listener that is interested in events
     */
    @Override
    public void addImageGeneratedListener(ImageGeneratedListener listener) {
        listeners.add(listener);
    }

    /**
     * allows uninterested parties to unregister to receive events when a new best image is
     * found
     * 
     * @param listener the listener that is no longer needed
     */
    @Override
    public void removeImageGeneratedListener(ImageGeneratedListener listener) {
        listeners.remove(listener);
    }

    /**
     * informs all listeners that a new best image has been found
     * 
     * @param image the new best image
     */
    @Override
    public void fireImageGenerated(Image image) {
        ImageGeneratedEvent event = new ImageGeneratedEvent(this, image);
        for (ImageGeneratedListener listener : listeners) {
            listener.imageGenerated(event);
        }
    }

    /**
     * starts up threads to start looking for images that are closest to the target
     */
    @Override
    public void startGenerating() {
        synchronized(startStopLock) {
            if (t == null) {

                populateGenerationZeroElite();
                t = new Thread[Runtime.getRuntime().availableProcessors() + 1];
                for (int i = 0; i < t.length; i++) {
                    t[i] = new Thread(this);
                    t[i].setName("Improver : " + i);
                    t[i].start();
                }
            }
        }
    }

    /**
     * shuts down threads that were looking for images
     */
    @Override
    public void stopGenerating() {
        synchronized(startStopLock) {
            if (t != null) {
                try {
                    for (Thread element : t) {
                        element.interrupt();
                    }
                    for (Thread element : t) {
                        element.join();
                    }
                } catch (InterruptedException ie) {
                } finally {
                    t = null;
                }
            }
        }
    }

    /**
     * completes the image by transforming the polygon image to the real image
     */
    @Override
    public void complete() {
        synchronized(startStopLock) {
            if (t != null) {
                stopGenerating();
                t = new Thread[1];
                t[0] = new Thread(new ImageCompleter(this, targetImage, generationHandler.getBestMember().getData(), imageSize));
                t[0].start();
            }
        }
    }

    /**
     * retrieves the best set of polygons for drawing the image so far
     * 
     * @return the best set of polygons
     */
    @Override
    public PolygonData[] getBestData() {
        return generationHandler.getBestMember().getData();
    }
    /**
     * the runnable interface implementation to repeatedly improve upon the image and check to
     * see if it is closer to the target image. Images are created in batches of settings.numCompetingImages
     * and the best one (if better than the parent) is selected as the new best.
     */
    @Override
    public void run() {
        try {
            BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = (Graphics2D)image.getGraphics();
            try {
                Composite srcOpaque = AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
                Improver improver = new Improver(settings, generationHandler, imageSize);

                while (!Thread.interrupted()) {
                    ImprovementType type = improver.improveRandomly();

                    List<PolygonData> data = improver.getData();
                    imagePolygonData(g2d, data, srcOpaque);

                    GenerationMember parentMember = improver.getParentGenerationMember();
                    Score delta = feedback.calculateScore(image, (parentMember != null) ? parentMember.getScore() : null, improver.getChangedArea());

                    boolean wasSuccessful;

                    ImprovementResult result = generationHandler.addPolygonData(delta, data.toArray(new PolygonData[data.size()]));
                    switch (result) {
                        case BEST:
                            fireImageGenerated(image);
                            wasSuccessful = true;
                            image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);
                            g2d.dispose();
                            g2d = (Graphics2D)image.getGraphics();
                            break;

                        case ELITE:
                            wasSuccessful = true;
                            break;

                        default:
                            wasSuccessful = false;
                    }

                    improver.typeWasSuccessful(type, wasSuccessful);
                }
            } finally {
                g2d.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imagePolygonData(Graphics2D g2d, List<PolygonData> polygonData, Composite srcOpaque) {
        g2d.setColor(Color.BLACK);
        g2d.setComposite(srcOpaque);
        g2d.fillRect(0, 0, imageSize.width, imageSize.height);

        for (PolygonData pd : polygonData) {
            pd.draw(g2d);
        }
    }

    private void populateGenerationZeroElite() {
        Composite srcOpaque = AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
        for (int i = 0; i < settings.getEliteSize(); i++) {
            BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_4BYTE_ABGR);
            List<PolygonData> polygons = new ArrayList<PolygonData>();
            PolygonData pd = PolygonData.randomPoly(imageSize, settings.getMaxPoints());
            polygons.add(pd);
            Graphics2D g2d = (Graphics2D)image.getGraphics();
            try {
                imagePolygonData(g2d, polygons, srcOpaque);
                Score delta = feedback.calculateScore(image, null, null);
                generationHandler.addPolygonData(delta, polygons.toArray(new PolygonData[polygons.size()]));
            } finally {
                g2d.dispose();
            }
        }
    }

    private Dimension trimSize(Dimension origSize, Dimension maxSize) {
        if ((origSize.width < maxSize.width) && (origSize.height < maxSize.height)) {
            return origSize;
        }

        double hFrac = (double)maxSize.width / (double)origSize.width;
        double vFrac = (double)maxSize.height/ (double)origSize.height;

        double frac = (hFrac < vFrac) ? hFrac : vFrac;

        return new Dimension((int)(frac * origSize.width), (int)(frac * origSize.height));
    }
}
