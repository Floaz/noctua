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
package net.noctuasource.noctua.core.ui.other;

import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.NoctuaInstanceUtil;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.ui.TextFieldDialog;
import net.noctuasource.profiles.Profile;
import net.noctuasource.profiles.ProfileData;
import net.noctuasource.profiles.ProfileManager;



public class DatastoreUpdateNewProfileDialog extends SubContextController implements TextFieldDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(DatastoreUpdateNewProfileDialog.class);



	// ***** Members ******************************************************** //

	private ProfileManager	profileManager;

	private Profile			createdProfile;



	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
    	String message = "Noctua unterstützt neuerdings mehrere Profile!\n" +
						"Bitte gib einen Profilnamen für ein neues Profil ein. Es kann jederzeit wieder geändert werden.";

    	TextFieldDialog.create(null, "Neues Profil", message, this);
	}




	// ***** Methods ******************************************************** //

	public Profile getCreatedProfile() {
		return createdProfile;
	}


	@Override
	public boolean success(String input) {
		if(profileManager.isValidProfileName(input.trim())) {
			return false;
		}

		try {
			createdProfile = profileManager.addProfile(new ProfileData(0, input.trim()));
		} catch (Exception ex) {
			logger.error("Error while adding profile. ", ex);
			NoctuaInstanceUtil.destroyNoctuaInstance(this);
		}

		destroy();
		return true;
	}


	@Override
	public void cancel() {
		NoctuaInstanceUtil.destroyNoctuaInstance(this);
	}

}
