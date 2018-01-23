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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * a class for picking a save as file name given the specified file type
 */
public class FileSelector {

    FileType fileType;

    /**
     * constructs a selector of the given type
     * 
     * @param type
     *            the type of save operation desired
     */
    public FileSelector(FileType type) {
        fileType = type;
    }

    /**
     * shows a dialog to pick a save target. Ensures that the proper extension is given for the file
     * 
     * @return the path of the selected file or null if not selected
     */
    public String getFileName() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new PolyFilter());
        chooser.setDialogTitle(PolycassoBundle.getString(PolycassoBundle.Key.SaveAs));
        int option = chooser.showSaveDialog(null);
        if (option != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();
        String path = file.getPath();
        String ext = fileType.getExtension();
        if (!path.endsWith(ext)) {
            path += ext;
        }

        return path;
    }

    /**
     * a file filter class for filtering only the files with the correct file extensions
     */
    class PolyFilter extends FileFilter {

        /**
         * implements the interface to select only files with the appropriate extension
         *
         * @return if the file is selectable
         */
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }

            return (file.getPath().endsWith(fileType.getExtension()));
        }

        /**
         * returns a string to be shown in the dialog for the file type desired
         * 
         * @return the type string
         */
        @Override
        public String getDescription() {
            return fileType.getDescription();
        }

    }
}
