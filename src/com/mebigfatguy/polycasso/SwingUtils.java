package com.mebigfatguy.polycasso;

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * general purpose swing utilities.
 */
public class SwingUtils {

    private SwingUtils() {
    }

    /**
     * sizes all passed in components to be the same height and width
     * 
     * @param components the components to be resized
     */
    public static void sizeUniformly(JComponent... components) {

        for (JComponent c : components) {
            c.setPreferredSize(null);
            c.setMinimumSize(null);
            c.setMaximumSize(null);
        }

        int width = 0;
        int height = 0;

        for (JComponent c : components) {
            Dimension d = c.getPreferredSize();
            if (d.width > width) {
                width = d.width;
            }
            if (d.height > height) {
                height = d.height;
            }
        }

        for (JComponent c : components) {
            Dimension d = c.getPreferredSize();
            d.width = width;
            d.height = height;
            c.setPreferredSize(d);
            c.setMinimumSize(d);
            c.setMaximumSize(d);
        }
    }
}