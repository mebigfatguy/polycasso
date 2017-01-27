/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2017 MeBigFatGuy.com
 * Copyright 2009-2017 Dave Brosius
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

import java.util.ResourceBundle;

/**
 * manages the resource bundle properties file for this application
 */
public class PolycassoBundle {

    /**
     * an enumeration of all the possible entries in the bundle
     */
    enum Key {
        Title("pc.title"),
        Reset("pc.reset"),
        OK("pc.ok"),
        Cancel("pc.cancel"),
        Info("pc.info"),
        File("pc.file"),
        PaintImage("pc.paintimage"),
        CopyImage("pc.copyimage"),
        CompleteImage("pc.completeimage"),
        SaveAs("pc.saveas"),
        PNG("pc.png"),
        PNGDescription("pc.pngdescription"),
        SVG("pc.svg"),
        SVGDescription("pc.svgdescription"),
        JAVA("pc.java"),
        JAVADescription("pc.javadescription"),
        Quit("pc.quit"),
        Edit("pc.edit"),
        Settings("pc.settings"),
        Proxy("pc.proxy"),
        About("pc.about"),
        AboutPolycasso("pc.aboutpolycasso"),
        GeneticsOptions("pc.geneticsoptions"),
        GenerationSize("pc.generationsize"),
        GenerationSizeToolTip("pc.generationsize.tt"),
        EliteSize("pc.elitesize"),
        EliteSizeToolTip("pc.elitesize.tt"),
        UseAnnealing("pc.useannealing"),
        UseAnnealingToolTip("pc.useannealing.tt"),
        StartTemperature("pc.starttemperature"),
        StartTemperatureToolTip("pc.starttemperature.tt"),
        CoolingRate("pc.coolingrate"),
        CoolingRateToolTip("pc.coolingrate.tt"),
        ImageOptions("pc.imageoptions"),
        MaxImageSize("pc.maximagesize"),
        Width("pc.width"),
        Height("pc.height"),
        MaximumPolygons("pc.maximumpolygons"),
        NumberOfCompetingImages("pc.numberofcompetingimages"),
        MaximumPolygonPoints("pc.maxpolygonpoints"),
        MaximumPointMovement("pc.maximumpointmovement"),
        MaximumColorChange("pc.maximumcolorchange"),
        ProxyHost("pc.proxyhost"),
        ProxyPort("pc.proxyport"),
        EnterURL("pc.enterurl"),
        BadSetting("pc.badsetting"),
        SaveFailure("pc.savefailure"),
        OverwriteWarning("pc.overwritewarning");

        String id;

        /**
         * creates a key given the properties file name
         * 
         * @param id the properties file entry name
         */
        Key(String id) {
            this.id = id;
        }

        /**
         * retrieves the properties file entry name for this Key
         * 
         * @return the properties file entry name id
         */
        public String id() {
            return id;
        }
    }

    private static ResourceBundle bundle = ResourceBundle.getBundle("com/mebigfatguy/polycasso/resource");

    /**
     * protects this class from being instantiated as it is meant to be accessed as a static class
     */
    private PolycassoBundle() {

    }

    /**
     * retrieves a string from a resource bundle given a key
     * 
     * @param key the key of the property item that is to be retrieved
     * @return the string representing the localized name
     */
    public static String getString(Key key) {
        return bundle.getString(key.id());
    }
}
