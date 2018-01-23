/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2018 MeBigFatGuy.com
 * Copyright 2009-2018 Dave Brosius
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

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

/**
 * finds an image on the web to try to draw with polygons
 */
public class RandomImageFinder {

    private static final String ROOTURL = "http://www.dogpile.com";
    private static final String URL = ROOTURL + "/search/images?q=%s";
    private static final Pattern IMAGE_HTML_PATTERN = Pattern
            .compile("\\<div class=\"resultDisplayUrl\"\\>\\s*\\<a data-icl-coi=\"\\d*\" data-icl-cop=\"results-main\" href=\"([^\"]+)");

    private static final int NAMELEN = 3;
    private static final int ATTEMPTS = 5;

    /**
     * private to avoid construction of this static access only class
     */
    private RandomImageFinder() {
    }

    /**
     * finds an image thru dogpile image search
     *
     * @param settings
     *            settings to fetch proxy information from
     * @return a random image
     *
     * @throws IOException
     *             if fetching resources at urls fails
     */
    public static Image findImage(Settings settings) throws IOException {

        for (int i = 0; i < ATTEMPTS; i++) {
            try {
                char[] ranName = new char[NAMELEN];
                Random ran = new Random(System.currentTimeMillis());
                for (int c = 0; c < NAMELEN; c++) {
                    ranName[c] = (char) ('A' + ran.nextInt(26));
                }

                return findImageAt(String.format(URL, new String(ranName)), settings);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        throw new IOException("Failed to find an inspiration to draw");
    }

    /**
     * retrieve an image thru dogpile image search
     *
     * @param url
     *            the url to look for the image
     * @param settings
     *            settings to fetch proxy information from
     * @return the image
     * @throws IOException
     *             if the site that is hosting the image is down, or non responsive
     */
    private static final Image findImageAt(String url, Settings settings) throws IOException {

        String html = new String(URLFetcher.fetchURLData(url, settings.getProxyHost(), settings.getProxyPort()), "UTF-8");

        List<String> images = new ArrayList<>();

        Matcher m = IMAGE_HTML_PATTERN.matcher(html);
        while (m.find()) {
            images.add(m.group(1));
        }

        String imageURL = images.get((int) (Math.random() * images.size()));
        imageURL = imageURL.replace("\\", "");
        return new ImageIcon(URLFetcher.fetchURLData(imageURL, settings.getProxyHost(), settings.getProxyPort())).getImage();
    }
}
