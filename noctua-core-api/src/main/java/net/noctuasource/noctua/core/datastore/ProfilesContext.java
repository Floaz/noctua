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
package net.noctuasource.noctua.core.datastore;

import java.io.IOException;
import java.util.Properties;

import net.noctuasource.profiles.Profile;




/**
 * ProfilesContext.
 * @author Philipp Thomas
 */
public interface ProfilesContext {

	/**
	 * Initializes the profiles context.
	 * @param profile The profile to use.
	 */
	public void init(Profile profile);


	/**
	 * Returns the absolute path of the profile dir.
	 */
	public String getAbsoluteProfileDir();


	/**
	 * Returns the profile.
	 */
	public Profile getProfile();


	/**
	 * Returns the profile properties.
	 */
	public Properties getProperties();


	/**
	 * Saves the properties.
	 * @throws IOException
	 */
	public void saveProperties() throws IOException;


	/**
	 * Returns the version of the profile dir.
	 */
	int			getProfileVersion();


	/**
	 * Sets the version of the profile dir.
	 */
	void		setProfileVersion(int newVersionNumber);


	/**
	 * Returns the version of the profile dir.
	 */
	int			getNeededProfileVersion();

}
