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
package net.noctuasource.profiles.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.noctuasource.profiles.Observable;
import net.noctuasource.profiles.Observer;
import net.noctuasource.profiles.Profile;
import net.noctuasource.profiles.ProfileData;
import net.noctuasource.profiles.ProfileDataFile;
import net.noctuasource.profiles.ProfileManager;
import net.noctuasource.profiles.ProfileXmlException;
import net.noctuasource.profiles.ProfileXmlFile;
import net.noctuasource.util.RandomString;

import org.apache.log4j.Logger;





public class DefaultProfileManager implements ProfileManager, Observable {

	public static final String 	PROFILE_INI = "profile.ini";

	public static final int		RANDOM_PROFILE_DIR_LENGTH = 16;


	static Logger logger = Logger.getLogger(DefaultProfileManager.class);


	private File profilesDir;
	private String profilesXmlFilename;

	private List<Profile> profiles = new LinkedList<>();
	private Profile defaultProfile;

	private List<Observer> observers = new LinkedList<>();




	public DefaultProfileManager() {

	}



	@Override
	public void reloadProfiles(File profilesDir, String filename) {
		logger.debug("Reload profiles...");

		try {
			profiles.clear();
			
			this.profilesDir = profilesDir;
			this.profilesXmlFilename = filename;
			String profilesXmlFullPath = profilesDir + File.separator + filename;

			ProfileXmlFile profileXmlFile = new ProfileXmlFileImpl(profilesXmlFullPath);
			String defaultProfilePath = profileXmlFile.getDefaultProfilePath();

			Iterable<String> profilePaths = profileXmlFile.getProfilePaths();

			for (String relativePath : profilePaths) {
				File absolutePath = new File(profilesDir
												+ File.separator + relativePath);

				File profileFile = new File(absolutePath
												+ File.separator + PROFILE_INI);

				if (!absolutePath.exists()) {
					logger.warn("Profile path does not exist: " + absolutePath);
				}
				else if (!profileFile.exists()) {
					logger.warn("Profile file does not exist: " + profileFile);
				}
				else {
					try {
						ProfileData data = ProfileDataFile.parse(profileFile, relativePath);

						Profile profile = new Profile();
						profile.setData(data);
						profile.setDir(relativePath);

						profiles.add(profile);

						if(relativePath.equals(defaultProfilePath)) {
							defaultProfile = profile;
						}
					} catch (Exception e) {
						logger.warn("Could not parse profile file: " + profileFile);
					}

				}
			}
		} catch (Exception e) {

		}
	}


	@Override
	public Iterable<Profile> getAllProfiles() {
		return profiles;
	}

	@Override
	public Profile getDefaultProfile() {
		return defaultProfile;
	}

	@Override
	public void setDefaultProfile(Profile profile) {
		if(!profiles.contains(profile) && profile != null) {
			logger.warn("Setting profile is not allowed. Not a valid profile!");
			return;
		}

		defaultProfile = profile;

		ProfileXmlFile profileXmlFile;
		try {
			profileXmlFile = getProfileXmlFile();
			profileXmlFile.setDefaultProfilePath(defaultProfile == null ? "" : defaultProfile.getDir());
		} catch (ProfileXmlException e) {
			logger.warn("Exception while writing profiles xml: ", e);
		}
	}


	@Override
	public boolean isValidProfileName(String name) {
		return name != null && !name.trim().isEmpty();
	}


	@Override
	public Profile addProfile(ProfileData data) throws IOException {
		logger.debug("Add new profile...");

		String newProfilePath;

		RandomString rand = new RandomString(RANDOM_PROFILE_DIR_LENGTH);

		File absolutePath;

		do {
			newProfilePath = rand.nextString();

			absolutePath = new File(profilesDir + File.separator + newProfilePath);

		} while( absolutePath.exists());

		if ( !absolutePath.mkdirs()) {
			throw new IOException("Could not create profile dir: " + absolutePath);
		}

		File profileFilename = new File(absolutePath + File.separator + PROFILE_INI);

		ProfileDataFile.write(profileFilename, data);

		try {
			ProfileXmlFile profileXmlFile = getProfileXmlFile();
			profileXmlFile.addProfilePath(newProfilePath);
		} catch (ProfileXmlException e) {
			throw new IOException("Exception in adding profile path to profiles.xml: "
								+ e.getMessage());
		}

		Profile newProfile = new Profile();
		newProfile.setData(data);
		newProfile.setDir(newProfilePath);

		profiles.add(newProfile);

		notifyObservers();

		return newProfile;
	}


	@Override
	public void editProfile(Profile profile, ProfileData newData) throws IOException {

		if (!profiles.contains(profile)) {
			return;
		}

		File absolutePath = new File(profilesDir + File.separator + profile.getDir());
		File profileFilename = new File(absolutePath + File.separator + PROFILE_INI);

		if ( !absolutePath.exists()) {
			throw new IOException("Could not edit profile. Dir does not exist: " + absolutePath);
		}

		ProfileDataFile.write(profileFilename, newData);

		profile.setData(newData);

		notifyObservers();
	}


	@Override
	public void deleteProfile(Profile profile) throws IOException {
		deleteProfile(profile, true);
	}


	@Override
	public void deleteProfile(Profile profile, boolean deleteDir) throws IOException {
		logger.debug("Delete profile " + profile.getData().getName() + "!");

		if (!profiles.contains(profile)) {
			return;
		}

		profiles.remove(profile);

		try {
			ProfileXmlFile profileXmlFile = getProfileXmlFile();
			profileXmlFile.removeProfilePath(profile.getDir());
		} catch (ProfileXmlException e) {
			throw new IOException("Exception in deleting path from profiles.xml: " + e.getMessage());
		}

		File absolutePath = new File(profilesDir + File.separator + profile.getDir());

		if (deleteDir) {
			if (!deleteDirectory(absolutePath)) {
				logger.warn("Could not delete profile directory!");
			}
		}

		notifyObservers();
	}


	private ProfileXmlFile getProfileXmlFile() throws ProfileXmlException {
		return new ProfileXmlFileImpl(profilesDir + File.separator + profilesXmlFilename);
	}



	public boolean isManaged( Profile profile) {
		return profiles.contains(profile);
	}




	@Override
	public void registerObserver(Observer observer) {
		if( !observers.contains(observer)) {
			observers.add(observer);
		}
	}


	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}


	@Override
	public void notifyObservers() {
		for (Observer o : observers) {
			o.updateData();
		}
	}




	static public boolean deleteDirectory(File dir) {

		if (dir.isDirectory()) {

		    String[] children = dir.list();

		    for (int i=0; i<children.length; i++) {
		        boolean success = deleteDirectory(new File(dir, children[i]));
		        if (!success) {
		            return false;
		        }
		    }
		}

		return dir.delete();
	}

}
