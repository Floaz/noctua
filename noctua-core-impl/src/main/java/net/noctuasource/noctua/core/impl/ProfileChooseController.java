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
package net.noctuasource.noctua.core.impl;

import com.google.common.eventbus.Subscribe;
import java.io.File;
import javax.annotation.Resource;
import net.noctuasource.act.annotation.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.ExecutorIdentifiers;


import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.GlobalPaths;
import net.noctuasource.profiles.Profile;
import net.noctuasource.profiles.ProfileManager;






public class ProfileChooseController extends SubContextController {

	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(ProfileChooseController.class);


	// -- Static Members ------------------------

	static final String		PROFILES_INI_FILENAME = "profiles.xml";



	// -- Members ------------------------------

	@Resource
	private ProfileManager	profileManager;

	public void setProfileManager(ProfileManager profileManager) {
		this.profileManager = profileManager;
	}



	@Override
	protected void onCreate() {
		registerEventListener(this);
	}

	@Override
	protected void onDestroy() {
		unregisterEventListener(this);
	}



	@RunLater(executor=ExecutorIdentifiers.BACKGROUND_EXECUTOR)
	public void init() {
		logger.debug("Run profile choose...");

		GlobalPaths paths = GlobalPathsFactory.getGlobalPaths();
		File profilesDir = new File(paths.getProfilesDir());
		profileManager.reloadProfiles(profilesDir, PROFILES_INI_FILENAME);

		Boolean resetDefaultProfile = getControllerParams().get("resetDefaultProfile", Boolean.class);
		if(resetDefaultProfile != null && resetDefaultProfile.booleanValue()) {
			profileManager.setDefaultProfile(null);
		}

		Profile profile = profileManager.getDefaultProfile();

		if(profile == null) {
			createController("profileChooseView");
		} else {
			postEvent(new ProfileChosenEvent(profile));
		}
	}


	@Subscribe
	public void onProfileChosen(ProfileChosenEvent event) {
		destroy();
	}

}
