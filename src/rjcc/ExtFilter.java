/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rjcc;

import java.io.File;

/**
 *
 * @author admin
 */
class ExtFileFilter extends javax.swing.filechooser.FileFilter {
 
	String ext;
	String description;
	ExtFileFilter(String ext, String descr) {
		this.ext = ext;
		description = descr;
	}
	
        @Override
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension == null)
				return (ext.length() == 0);
			return ext.equals(extension);
		}
		return false;
	}
	
	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}			
		}
		return null;
	}
	
	public String getDescription() {
		return description;
	}
}
