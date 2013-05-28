/**
 * This file is part of Noctua.
 *
 * Copyright (C) 2013 Philipp Rene Thomas <info@noctuasource.net>
 *
 * Noctua is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Noctua is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Noctua.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.noctuasource.noctua.core.ui.profiles;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.noctuasource.noctua.core.ui.StyleConstants;
import net.noctuasource.profiles.Profile;

import org.apache.log4j.Logger;







public class ProfilesListRenderer implements ListCellRenderer {
    
	static Logger 		logger = Logger.getLogger(ProfilesListRenderer.class);
	
	private static String ICON_FILENAME = "/images/profile.png";
	
	
	
    public Component getListCellRendererComponent(  JList list,
											        Object value,
											        int index,
											        boolean isSelected,
											        boolean cellHasFocus)
    {
    	Profile profile = (Profile) value;
    	
    	ImageIcon icon = createImageIcon(ICON_FILENAME);
    	
    	JLabel iconLabel = new JLabel(profile.getData().getName(), icon, JLabel.CENTER);
    	iconLabel.setVerticalTextPosition(JLabel.BOTTOM);
    	iconLabel.setHorizontalTextPosition(JLabel.CENTER);

    	//iconLabel.setBackground(isSelected ? Color.BLUE : Color.WHITE);
    	iconLabel.setForeground(isSelected ? Color.BLUE : Color.BLACK);
    	
    	iconLabel.setBorder(StyleConstants.INNER_BORDER);
    	
        return iconLabel;
    }
    
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ProfilesListRenderer.class.getResource(path);

        if( imgURL == null) {
        	logger.error("Could not load image icon: " + path);
        	return null;
        }
        
        return new ImageIcon(imgURL);
    }
	
}
