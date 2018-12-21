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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

/**
 * generates a java source code that draws the image with polygons in a JFrame
 */
public class JavaSaver implements Saver {

	private static final String EXTENSION = ".java";
	private static final String TABS = "\t\t\t\t\t";

	/**
	 * saves the polygon data as a java file that opens a JFrame and draws the
	 * polygons
	 *
	 * @param fileName  the name of the file to write to
	 * @param imageSize the dimension of the image
	 * @param data      the polygons to draw
	 */
	@Override
	public void save(String fileName, Dimension imageSize, PolygonData[] data) throws IOException {

		int sep = fileName.lastIndexOf(File.separator);
		String className;
		if (sep >= 0) {
			className = fileName.substring(sep + 1);
		} else {
			className = fileName;
		}

		if (className.endsWith(EXTENSION)) {
			className = className.substring(0, className.length() - EXTENSION.length());
		}

		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
				InputStream templateStream = getClass()
						.getResourceAsStream("/com/mebigfatguy/polycasso/JavaSaver.template")) {
			String template = IOUtils.toString(templateStream, StandardCharsets.UTF_8);

			String polygonData = getPolygonData(data);
			String colorData = getColorData(data);
			String transparencyData = getTransparencyData(data);

			/* All the curly braces confuses MessageFormat, so just do it manually */
			template = template.replaceAll("\\{0\\}", className);
			template = template.replaceAll("\\{1\\}", String.valueOf(imageSize.width));
			template = template.replaceAll("\\{2\\}", String.valueOf(imageSize.height));
			template = template.replaceAll("\\{3\\}", polygonData);
			template = template.replaceAll("\\{4\\}", colorData);
			template = template.replaceAll("\\{5\\}", transparencyData);

			pw.println(template);

		} catch (IOException ioe) {
		}
	}

	private String getPolygonData(PolygonData... data) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		String outerComma = "";
		String innerComma;
		for (PolygonData pd : data) {

			pw.println(outerComma);
			pw.print(TABS);
			pw.println("{");

			/* Xs */
			pw.print(TABS);
			pw.print("\t{");

			innerComma = "";
			Polygon poly = pd.getPolygon();
			for (int j = 0; j < poly.npoints; j++) {
				pw.print(innerComma);
				pw.print(poly.xpoints[j]);
				innerComma = ",";
			}

			pw.println("},");

			/* Ys */
			pw.print(TABS);
			pw.print("\t{");

			innerComma = "";
			poly = pd.getPolygon();
			for (int j = 0; j < poly.npoints; j++) {
				pw.print(innerComma);
				pw.print(poly.ypoints[j]);
				innerComma = ",";
			}

			pw.println("}");

			pw.print(TABS);
			pw.print("}");
			outerComma = ",";
		}

		pw.flush();
		return sw.toString();
	}

	private String getColorData(PolygonData[] data) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		String comma = "";
		for (PolygonData pd : data) {

			pw.println(comma);
			pw.print(TABS);
			pw.print("{");

			Color color = pd.getColor();
			pw.print(color.getRed());
			pw.print(",");
			pw.print(color.getGreen());
			pw.print(",");
			pw.print(color.getBlue());
			pw.print("}");
			comma = ",";
		}

		pw.flush();
		return sw.toString();
	}

	private String getTransparencyData(PolygonData[] data) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println();
		pw.print(TABS);
		String comma = "";
		for (PolygonData pd : data) {

			pw.print(comma);
			pw.print(pd.getAlpha());
			pw.print("f");
			comma = ",";
		}

		pw.flush();
		return sw.toString();
	}

}
