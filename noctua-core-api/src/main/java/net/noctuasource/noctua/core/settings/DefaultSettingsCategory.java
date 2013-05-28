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
package net.noctuasource.noctua.core.settings;

import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;





public class DefaultSettingsCategory implements SettingsCategory {

	public static final SettingsCategory CATEGORY_GENERAL =
			new DefaultSettingsCategory("General", "general.png", 1);

	public static final SettingsCategory CATEGORY_MISCELLANEOUS =
			new DefaultSettingsCategory("Miscellaneous", "miscellaneous.png", 500);
	
	
	
	
	
	
	
	private String textKey;
	private String iconFile;
	private int priority;
	
	
	DefaultSettingsCategory(String textKey, String iconFile, int priority) {
		this.textKey = textKey;
		this.iconFile = iconFile;
		this.priority = priority;
	}
	
	
	@Override
	public String getName() {
		return textKey;
	}

	@Override
	public ImageIcon getIcon() {
		URL url = DefaultSettingsCategory.class.getResource("/images/settings/"
															+ iconFile);
		
		if(url == null) {
			return null;
		}
		
		try {
			return new ImageIcon(url.toURI().toString());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	@Override
	public int getPriority() {
		return priority;
	}

}
