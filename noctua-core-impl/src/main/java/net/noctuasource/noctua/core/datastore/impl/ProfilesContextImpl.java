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
package net.noctuasource.noctua.core.datastore.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.noctuasource.noctua.core.GlobalPaths;
import net.noctuasource.noctua.core.datastore.ProfilesContext;
import net.noctuasource.noctua.core.impl.GlobalPathsFactory;
import net.noctuasource.profiles.Profile;

import org.apache.log4j.Logger;




public class ProfilesContextImpl implements ProfilesContext {

	// -- Basic Static Members ------------------------

	static final Logger	logger = Logger.getLogger(ProfilesContextImpl.class);


	// -- Static Members ------------------------

	static final String		CONFIG_FILE = "config.properties";

	private static final int	NEEDED_VERSION = 1;




	// -- Members ------------------------------

	private Profile 		profile = null;

	private File			absoluteProfilePath = null;

	private File			profileConfigFile = null;
	private Properties		profileConfig = null;






	@Override
	public void init(Profile profile) {
		logger.debug("Initialize ProfilesContext...");

		this.profile = profile;

		GlobalPaths paths = GlobalPathsFactory.getGlobalPaths();
		absoluteProfilePath = new File( paths.getProfilesDir()
										+ File.separator
										+ profile.getDir());

		logger.debug("The absolute current profile path is \"" + absoluteProfilePath + "\"!");

		profileConfigFile = new File(absoluteProfilePath + File.separator + CONFIG_FILE);


		logger.debug("Load profile config file \"" + profileConfigFile + "\"...");

		profileConfig = new Properties();
		if(profileConfigFile.exists()) {
			try {
				profileConfig.load(new FileInputStream(profileConfigFile));
			} catch (IOException ex) {
				logger.debug("...but could not be loaded!", ex);;
			}
		}
		else {
			logger.debug("...but it does not exist!");
		}

	}


	@Override
	public Profile getProfile() {
		return profile;
	}


	@Override
	public String getAbsoluteProfileDir() {
		return absoluteProfilePath.getAbsolutePath();
	}


	@Override
	public Properties getProperties() {
		return profileConfig;
	}


	@Override
	public void saveProperties() throws IOException {
		if( profileConfig == null) {
			throw new IllegalStateException("No profile config to save!");
		}

		profileConfig.store(new FileOutputStream(profileConfigFile), "");
	}


	@Override
	public int getProfileVersion() {
		return profile.getData().getVersion();
	}


	@Override
	public void setProfileVersion(int newVersionNumber) {
		profile.getData().setVersion(newVersionNumber);
	}


	@Override
	public int getNeededProfileVersion() {
		return NEEDED_VERSION;
	}


}
