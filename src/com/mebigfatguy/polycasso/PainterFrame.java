/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2014 MeBigFatGuy.com
 * Copyright 2009-2014 Dave Brosius
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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

/**
 * the main window for showing the image as it is being improved on.
 */
public class PainterFrame extends JFrame implements ImageGeneratedListener {

    private static final long serialVersionUID = 7729602294481171194L;
    private PainterPanel panel;
    private JMenuItem paintImage;
    private JMenuItem copyImage;
    private JMenuItem completeImage;
    private JMenu saveAsMenu;
    private JMenuItem saveAsPNGItem;
    private JMenuItem saveAsSVGItem;
    private JMenuItem saveAsJavaItem;
    private JMenuItem quitItem;
    private JMenuItem aboutItem;
    private JMenuItem settingsItem;
    private JMenuItem proxyItem;
    private ImageGenerator generator;
    private final Settings settings;

    /**
     * creates the main window, setups up menus and listeners
     */
    public PainterFrame() {
        setTitle(PolycassoBundle.getString(PolycassoBundle.Key.Title));
        initComponents();
        initMenus();
        initListeners();
        pack();
        settings = loadSettings();
        generator = null;
    }

    /**
     * creates and lays out components for this frame
     */
    private void initComponents() {

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(4, 4));
        panel = new PainterPanel();
        cp.add(panel, BorderLayout.CENTER);
    }

    /**
     * initializes the menus
     */
    private void initMenus() {
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = new JMenu(PolycassoBundle.getString(PolycassoBundle.Key.File));
        paintImage = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.PaintImage));
        fileMenu.add(paintImage);
        copyImage = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.CopyImage));
        fileMenu.add(copyImage);
        completeImage = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.CompleteImage));
        completeImage.setEnabled(false);
        fileMenu.add(completeImage);
        fileMenu.addSeparator();
        saveAsMenu = new JMenu(PolycassoBundle.getString(PolycassoBundle.Key.SaveAs));
        fileMenu.add(saveAsMenu);
        saveAsPNGItem = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.PNG));
        saveAsMenu.add(saveAsPNGItem);
        saveAsSVGItem = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.SVG));
        saveAsMenu.add(saveAsSVGItem);
        saveAsJavaItem = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.JAVA));
        saveAsMenu.add(saveAsJavaItem);
        saveAsMenu.setEnabled(false);
        fileMenu.addSeparator();
        quitItem = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.Quit));
        fileMenu.add(quitItem);
        mb.add(fileMenu);

        JMenu editMenu = new JMenu(PolycassoBundle.getString(PolycassoBundle.Key.Edit));
        settingsItem = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.Settings));
        editMenu.add(settingsItem);
        proxyItem = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.Proxy));
        editMenu.add(proxyItem);
        mb.add(editMenu);

        JMenu aboutMenu = new JMenu(PolycassoBundle.getString(PolycassoBundle.Key.About));
        aboutItem = new JMenuItem(PolycassoBundle.getString(PolycassoBundle.Key.AboutPolycasso));
        aboutMenu.add(aboutItem);
        mb.add(aboutMenu);

        setJMenuBar(mb);
    }

    /**
     * initializes the listeners for the various components
     */
    private void initListeners() {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                saveSettings(settings);
                System.exit(0);
            }
        });

        paintImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    Image targetImage = RandomImageFinder.findImage(settings);
                    beginGenerating(targetImage);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(PainterFrame.this, ioe.getMessage());
                }
            }
        });

        copyImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String url = JOptionPane.showInputDialog(PainterFrame.this, PolycassoBundle.getString(PolycassoBundle.Key.EnterURL));
                    if (url != null) {
                        Image targetImage = new ImageIcon(URLFetcher.fetchURLData(url, settings.getProxyHost(), settings.getProxyPort())).getImage();
                        beginGenerating(targetImage);
                    }
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(PainterFrame.this, ioe.getMessage());
                }
            }
        });

        completeImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (generator != null) {
                    generator.complete();
                }

                completeImage.setEnabled(false);
            }
        });

        saveAsPNGItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveImage(FileType.PNG);
            }
        });

        saveAsSVGItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveImage(FileType.SVG);
            }
        });

        saveAsJavaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveImage(FileType.Java);
            }
        });

        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (generator != null) {
                    generator.stopGenerating();
                }
                dispose();
                saveSettings(settings);
                System.exit(0);
            }
        });

        settingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SettingsDialog dialog = new SettingsDialog(settings);
                dialog.setLocationRelativeTo(PainterFrame.this);
                dialog.setModal(true);
                dialog.setVisible(true);
                if (dialog.isOK()) {
                    Settings dlgSettings = dialog.getSettings();
                    settings.setGenerationSize(dlgSettings.getGenerationSize());
                    settings.setEliteSize(dlgSettings.getEliteSize());
                    settings.setUseAnnealing(dlgSettings.isUseAnnealing());
                    settings.setStartTemperature(dlgSettings.getStartTemperature());
                    settings.setCoolingRate(dlgSettings.getCoolingRate());
                    settings.setMaxImageSize(dlgSettings.getMaxImageSize());
                    settings.setMaxPolygons(dlgSettings.getMaxPolygons());
                    settings.setMaxPoints(dlgSettings.getMaxPoints());
                    settings.setMaxPtMovement(dlgSettings.getMaxPtMovement());
                }
            }
        });

        proxyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ProxyDialog dialog = new ProxyDialog(settings);
                dialog.setLocationRelativeTo(PainterFrame.this);
                dialog.setModal(true);
                dialog.setVisible(true);
                if (dialog.isOK()) {
                    Settings dlgSettings = dialog.getSettings();
                    settings.setProxyHost(dlgSettings.getProxyHost());
                    settings.setProxyPort(dlgSettings.getProxyPort());
                }
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                AboutDialog ad = new AboutDialog();
                ad.setLocationRelativeTo(PainterFrame.this);
                ad.setModal(true);
                ad.setVisible(true);
            }
        });
    }

    /**
     * implements the ImageGeneratedListener interface to redraw the new best image
     * 
     * @param event the event describing the new best image
     */
    @Override
    public void imageGenerated(ImageGeneratedEvent event) {
        panel.setImage(event.getImage());
    }

    /**
     * save the image to what ever format was chosen
     * 
     * @param type the chosen file type to save as
     */
    public void saveImage(FileType type) {
        try {
            FileSelector selector = new FileSelector(type);
            String fileName = selector.getFileName();
            if (fileName != null) {
                File f = new File(fileName);
                if (f.exists()) {
                    String message = MessageFormat.format(PolycassoBundle.getString(PolycassoBundle.Key.OverwriteWarning), fileName);
                    int choice = JOptionPane.showConfirmDialog(PainterFrame.this, message, PolycassoBundle.getString(PolycassoBundle.Key.SaveAs), JOptionPane.YES_NO_OPTION);
                    if (choice != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                type.getSaver().save(fileName, generator.getImageSize(), generator.getBestData());
            }
        } catch (IOException ioe) {
            String message = MessageFormat.format(PolycassoBundle.getString(PolycassoBundle.Key.SaveFailure), ioe.getMessage());
            JOptionPane.showMessageDialog(PainterFrame.this, message);
        }
    }

    private void beginGenerating(Image targetImage) {
        if (generator != null) {
            generator.stopGenerating();
        }

        ImageSizer sizer = new ImageSizer(targetImage);
        Dimension size = new Dimension(sizer.getWidth(), sizer.getHeight());
        generator = new DefaultImageGenerator(settings, targetImage, size);

        panel.setTarget(generator.getTargetImage());
        size = generator.getImageSize();

        Dimension wSize = new Dimension(size);
        wSize.height += 2 * PainterFrame.this.getJMenuBar().getHeight();
        if (Polycasso.DEBUG){
            wSize.height *= 2;
        }
        setSize(wSize);
        generator = new DefaultImageGenerator(settings, targetImage, size);
        generator.addImageGeneratedListener(PainterFrame.this);
        generator.startGenerating();
        completeImage.setEnabled(true);
        saveAsMenu.setEnabled(true);
    }

    private Settings loadSettings() {
        ObjectInputStream ois = null;
        try {
            File polyDir = getSettingsDirectory();
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(polyDir, "settings.ser"))));
            return (Settings)ois.readObject();
        } catch (Exception e) {
            return new Settings();
        } finally {
            IOUtils.closeQuietly(ois);
        }
    }

    private void saveSettings(Settings s) {
        ObjectOutputStream oos = null;
        try {
            File polyDir = getSettingsDirectory();
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(polyDir, "settings.ser"))));
            oos.writeObject(s);
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(oos);
        }
    }

    private File getSettingsDirectory() {
        File polyDir = new File(System.getProperty("user.home"), ".polycasso");
        polyDir.mkdirs();
        return polyDir;
    }
}
