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

import net.noctuasource.noctua.core.ui.QuestionDialog;
import net.noctuasource.noctua.core.ui.QuestionDialog.Result;
import net.noctuasource.profiles.Profile;
import net.noctuasource.profiles.ProfileManager;



public class ProfileDeleteDialogView implements QuestionDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(ProfileDeleteDialogView.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	private ProfileManager 		profileManager;

	private Profile 			profile;


	// ***** Constructor **************************************************** //

	private ProfileDeleteDialogView(Window parent, ProfileManager profileManager, Profile profile) {
    	this.profileManager = profileManager;
    	this.profile = profile;

    	String message = "Möchtest du das Profil \""
						+ profile.getData().getName()
						+ "\" UNWIEDERBRINGLICH löschen?";

    	QuestionDialog.create(parent, "Profil löschen", message, false, this);
	}


	// ***** Methods ******************************************************** //

    static public void createAndShow(final Window parent,
    								 final ProfileManager profileManager,
    								 final Profile profile) {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	new ProfileDeleteDialogView(parent, profileManager, profile);
			}
		});
    }


	@Override
	public void finish(Result result) {
		if(result != Result.YES) {
			return;
		}

    	try {
			profileManager.deleteProfile(profile, true);
		} catch (Exception e) {
			logger.warn("Could not delete profile: ", e);
		}
	}
}
