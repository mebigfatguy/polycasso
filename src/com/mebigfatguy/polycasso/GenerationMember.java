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

import java.util.Arrays;

/**
 * class that holds a sample set of polygons and it's score
 */
public class GenerationMember implements Comparable<GenerationMember> {

    private final Score score;
    private final PolygonData[] data;

    GenerationMember(Score polyScore, PolygonData[] polyData) {
        score = polyScore;
        data = polyData;
    }

    /**
     * returns the score for this member
     * @return the score
     */
    public Score getScore() {
        return score;
    }

    /**
     * returns the polygon data for this member
     *
     * @return the polygon data
     */
    public PolygonData[] getData() {
        return data;
    }

    @Override
    public int compareTo(GenerationMember o) {
        long delta = score.getDelta() - o.score.getDelta();
        if (delta > 0) {
            return 1;
        } else if (delta < 0) {
            return -1;
        }

        return data.length - o.data.length;
    }

    @Override
    public int hashCode() {
        return score.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GenerationMember) {
            return score.getDelta() == ((GenerationMember)o).score.getDelta();
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + score + ": " + Arrays.toString(data) + ")";
    }
}