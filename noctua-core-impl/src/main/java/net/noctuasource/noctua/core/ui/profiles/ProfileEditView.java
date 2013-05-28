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

import javafx.application.Platform;
import javafx.stage.Window;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.ui.TextFieldDialog;
import net.noctuasource.profiles.Profile;
import net.noctuasource.profiles.ProfileData;
import net.noctuasource.profiles.ProfileManager;



public class ProfileEditView implements TextFieldDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(ProfileEditView.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	private ProfileManager 		profileManager;

	private Profile 			profile;


	// ***** Constructor **************************************************** //

	private ProfileEditView(Window parent, ProfileManager profileManager, Profile profile) {
    	this.profileManager = profileManager;
    	this.profile = profile;

    	String message = "Gib den neuen Profilnamen für das Profil \""
									+ profile.getData().getName() + "\" ein:";

    	TextFieldDialog.create(parent, "Profil umbenennen", message, this);
	}


	// ***** Methods ******************************************************** //

    static public void createAndShow(final Window parent,
    								 final ProfileManager profileManager,
    								 final Profile profile) {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	new ProfileEditView(parent, profileManager, profile);
			}
		});
    }


	@Override
	public boolean success(String input) {
		if(input.trim().isEmpty()) {
			return false;
		}

    	try {
        	ProfileData data = profile.getData();
        	data.setName(input.trim());
        	profileManager.editProfile(profile, data);
    	} catch(Exception e) {
    		logger.warn("Could not edit profile!", e);
    	}

    	return true;
	}


	@Override
	public void cancel() {
	}
}
