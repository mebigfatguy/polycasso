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

import java.awt.Dimension;

/**
 * the main web start application class
 */
public class Polycasso {

    /**
     * enable some console debugging, and show the target image
     */
    public static final boolean DEBUG = false;
    ;

    /**
     * the main entry point to the web start app
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        PainterFrame pf = new PainterFrame();

        Dimension dim = new Dimension(500, 300);
        pf.setPreferredSize(dim);
        pf.setSize(dim);
        pf.setLocationRelativeTo(null);
        pf.setVisible(true);
    }
}
