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

import javax.swing.JComponent;




public interface SettingsPage {
	
	/**
	* Return a GUI component which allows the user to edit this set of related 
	* preferences.
	*/  
	JComponent getUI();

	
	/**
	* Returns the category id in which this will be added.
	*/
	SettingsCategory getCategory();

	
	/**
	* Returns the priority index
	*/
	int getPriority();
	
	
	/**
	* The name of the tab in which this <tt>PreferencesEditor</tt>
	* will be placed. 
	*/
	String getTitle();
	
	
	/**
	* The mnemonic to appear in the tab name.
	*
	* <P>Must match a letter appearing in {@link #getTitle}.
	* Use constants defined in <tt>KeyEvent</tt>, for example <tt>KeyEvent.VK_A</tt>.
	*/
	int getMnemonic();
	
	
	/**
	* Store the related preferences as they are currently displayed, overwriting
	* all corresponding settings.
	*/
	void savePreferences();
	
	
	/**
	* Reset the related preferences to their default values, but only as 
	* presented in the GUI, without affecting stored preference values.
	*
	* <P>This method may not apply in all cases. For example, if the item 
	* represents a config which has no meaningful default value (such as a mail server 
	* name), the desired behavior may be to only allow a manual change. In such a 
	* case, the implementation of this method must be a no-operation. 
	*/
	void matchGuiToDefaultPreferences();

}
