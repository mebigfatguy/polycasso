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

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * class that maintains the set of polygon data for this generation of images
 */
public class GenerationHandler implements Serializable {

    private static final long serialVersionUID = 2375492293685052783L;

    private List<GenerationMember> generation;
    private final Random random;
    private final Settings settings;
    private int generationNumber;
    private double annealingValue;
    private GenerationMember bestMember;
    private double eliteCutOff;
    private int generationBests;
    private int generationElites;

    /**
     * constructs a handler for managing successive generations of image samples
     *
     * @param confSettings
     *            settings to use for generation and elite size
     * @param imageSize
     *            the size of the target image
     */
    public GenerationHandler(Settings confSettings, Dimension imageSize) {
        random = new Random();
        generationNumber = 0;
        settings = confSettings;
        bestMember = new GenerationMember(DefaultScore.MAX_SCORE, new PolygonData[0]);
        eliteCutOff = Long.MAX_VALUE;
        generation = new ArrayList<>(settings.getGenerationSize() + 10);
        annealingValue = settings.getStartTemperature() * settings.getStartTemperature() * imageSize.height * imageSize.width;
        generationBests = 0;
        generationElites = 0;
    }

    /**
     * add a sample polygon set to this generation with a given score
     *
     * @param score
     *            the deviation from perfection this set calculates
     *
     * @param polygonData
     *            the polygons that draw the image
     *
     * @return whether this is the best polygon set so far
     */
    public ImprovementResult addPolygonData(Score score, PolygonData... polygonData) {
        GenerationMember newMember = new GenerationMember(score, polygonData);
        synchronized (generation) {
            generation.add(newMember);
            if (generation.size() >= settings.getGenerationSize()) {
                processGeneration();
            } else {
                Collections.sort(generation);
            }
            if (score.getDelta() < bestMember.getScore().getDelta()) {
                bestMember = newMember;
                generationBests++;
                return ImprovementResult.BEST;
            } else if (score.getDelta() < eliteCutOff) {
                generationElites++;
                return ImprovementResult.ELITE;
            }
        }
        return ImprovementResult.FAIL;
    }

    /**
     * pick a random member either from the general pool or elite pool skew the results towards the elite
     *
     * @param elite
     *            whether to pick from the elite pool or not
     * @return a random member
     */
    public GenerationMember getRandomMember(boolean elite) {
        synchronized (generation) {
            int size = elite ? (settings.getEliteSize() % generation.size()) : generation.size();

            if (size == 0) {
                return null;
            }

            int r = random.nextInt(size);

            int idx = (int) (r * ((double) r / (double) size));

            return generation.get(idx);
        }
    }

    /**
     * returns the best polygon set to draw the picture
     *
     * @return the best polygon set
     */
    public GenerationMember getBestMember() {
        synchronized (generation) {
            return bestMember;
        }
    }

    private void processGeneration() {
        int eliteSize = settings.getEliteSize();

        Collections.<GenerationMember> sort(generation);
        int sz = generation.size();

        List<GenerationMember> nextGeneration = new ArrayList<>(settings.getGenerationSize() + 10);
        for (int i = 0; i < eliteSize; i++) {
            nextGeneration.add(generation.get(i));
        }

        if ((annealingValue > 0.01) && settings.isUseAnnealing()) {
            int annealingReplacements = 0;

            /* always keep the best, so start at 1 */
            for (int i = 1; i < eliteSize; i++) {
                int candidateIndex = random.nextInt(sz - eliteSize) + eliteSize;
                GenerationMember candidate = generation.get(candidateIndex);
                GenerationMember elite = generation.get(i);
                long delta = candidate.getScore().getDelta() - elite.getScore().getDelta();
                if (delta < annealingValue) {
                    nextGeneration.set(i, candidate);
                    if (Polycasso.DEBUG) {
                        annealingReplacements++;
                    }
                }
            }

            if (Polycasso.DEBUG) {
                System.out.println(
                        "Generation " + generationNumber + " had " + annealingReplacements + " annealing replacements with annealing value: " + annealingValue);
            }
        }

        generation = nextGeneration;

        eliteCutOff = generation.get(eliteSize - 1).getScore().getDelta();

        if (Polycasso.DEBUG) {
            System.out.println("Generation " + generationNumber + " had " + generationBests + " bests and " + generationElites + " elites. Best Score: "
                    + generation.get(0).getScore());
        }
        generationBests = 0;
        generationElites = 0;
        generationNumber++;
        annealingValue *= (1.0 - settings.getCoolingRate());
    }
}
