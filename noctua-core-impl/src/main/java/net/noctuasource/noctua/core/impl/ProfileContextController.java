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



import net.noctuasource.noctua.core.datastore.ProfilesContext;
import net.noctuasource.act.controller.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.spring.SpringDefaultConstants;
import net.noctuasource.noctua.core.database.DatabaseInitializationException;
import net.noctuasource.noctua.core.database.DatabaseInitializer;
import net.noctuasource.noctua.core.impl.launcher.NoctuaInstanceUtil;
import net.noctuasource.noctua.core.ui.other.DatastoreTooNewMessageDialog;
import net.noctuasource.profiles.Profile;
import org.apache.log4j.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;





/**
 * Initializes the profiles context.
 * @author Philipp Thomas
 */
public class ProfileContextController extends SubContextController {


	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(ProfileContextController.class);


	// -- Static Members ------------------------

	static final String		PROFILE_CONTEXT_FILE = "/spring/profile-context.xml";


	// -- Members ------------------------------

	private ConfigurableApplicationContext	context;

	private ProfilesContext					profilesContext;

	private DatabaseInitializer				dbInitializer;



	@RunLater
	public void init() {
		try {
			initApplicationContext();
			injectResources();

			// Load profile
			Profile profile = getControllerParams().getOrThrow("profile", Profile.class);
			profilesContext.init(profile);

			if(profilesContext.getProfileVersion() < profilesContext.getNeededProfileVersion()) {
				updateProfileDir();
			}
			else if(profilesContext.getProfileVersion() > profilesContext.getNeededProfileVersion()) {
				showTooNewMessage();
				return;
			}


			dbInitializer.openDatabase();

			startMainWindow();
		}
		catch(DatabaseInitializationException ex) {
			logger.error("Could not initialize database!", ex);
			NoctuaInstanceUtil.destroyNoctuaInstance(this);
		}
		catch(Exception ex) {
			logger.error("Could not initialize profile context!", ex);
			NoctuaInstanceUtil.destroyNoctuaInstance(this);
		}
	}


	@Override
	protected void onDestroy() {
		if(context != null) {
			context.close();
		}
	}




	/**
	 * Initializes the application context for profiles.
	 */
	private void initApplicationContext() {
		logger.debug("Load profile application context...");

		ApplicationContext parentApplicationContext =
							getControllerData().getOrThrow(SpringDefaultConstants.DEFAULT_CONTEXT_DATA_KEY,
														   ApplicationContext.class);

		context = new ClassPathXmlApplicationContext(new String[]{PROFILE_CONTEXT_FILE}, parentApplicationContext);

		getControllerData().set(SpringDefaultConstants.DEFAULT_CONTEXT_DATA_KEY, context);
	}


	/**
	 * Initializes the application context for profiles.
	 */
	private void injectResources() {
		profilesContext = context.getBean(ProfilesContext.class);
		dbInitializer = context.getBean(DatabaseInitializer.class);
	}


	/**
	 * Updates the old profile dir.
	 */
	private void updateProfileDir() {
		profilesContext.setProfileVersion(1);
	}


	/**
	 * Starts main window.
	 */
	private void startMainWindow() {
		executeController("mainWindowView");
	}


	/**
	 * Show too new message.
	 */
	private void showTooNewMessage() {
		executeController(DatastoreTooNewMessageDialog.class);
	}

}
