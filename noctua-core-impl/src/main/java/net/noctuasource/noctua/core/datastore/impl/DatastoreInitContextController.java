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
import javax.annotation.Resource;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.annotation.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.util.AfterDestroyRunnable;
import net.noctuasource.noctua.core.ExecutorIdentifiers;
import net.noctuasource.noctua.core.datastore.DatastoreManager;
import net.noctuasource.noctua.core.NoctuaInstanceUtil;
import net.noctuasource.noctua.core.ui.other.DatastoreTooNewMessageDialog;
import net.noctuasource.noctua.core.ui.other.DatastoreUpdateNewProfileDialog;
import net.noctuasource.profiles.Profile;
import org.apache.log4j.Logger;






/**
 * DatastoreInitContextController.
 * @author Philipp Thomas
 */
public class DatastoreInitContextController extends SubContextController {


	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(DatastoreInitContextController.class);


	// -- Static Members ------------------------

	static final String		PROFILE_CONTEXT_FILE = "/spring/profile-context.xml";


	// -- Members ------------------------------

	@Resource
	private DatastoreManager datastoreManager;

	public void setDatastoreManager(DatastoreManager datastoreManager) {
		this.datastoreManager = datastoreManager;
	}



	@RunLater(executor=ExecutorIdentifiers.BACKGROUND_EXECUTOR)
	public void init() {
		logger.debug("Init datastore...");

		try {
			if(!datastoreManager.existsDatastore()) {
				logger.debug("Datastore does not exist... creating...");
				datastoreManager.createDatastore();
				doCopyOldData();
				return;
			} else {
				int datastoreVersion = datastoreManager.getDatastoreVersion();
				int neededDatastoreVersion = datastoreManager.getNeededDatastoreVersion();

				if(datastoreVersion == neededDatastoreVersion) {
					destroy();
				}
				else if(datastoreVersion > neededDatastoreVersion) {
					showTooNewMessage();
				}
				else if(datastoreVersion < neededDatastoreVersion) {
					updateDatastore();
				}
			}
		}
		catch(Exception ex) {
			logger.error("Could not initialize datastore!", ex);
			NoctuaInstanceUtil.destroyNoctuaInstance(this);
		}
	}


	private void doCopyOldData() {
		ContextController copyOldDataController = createController("copyOldData");

		AfterDestroyRunnable.create(copyOldDataController, new Runnable() {
			@Override
			public void run() {
				destroy();
			}
		});
	}


	private void showTooNewMessage() {
		createController(DatastoreTooNewMessageDialog.class);
	}


	private void updateDatastore() {
		int datastoreVersion = datastoreManager.getDatastoreVersion();

		if(datastoreVersion != 0) {
			logger.error("Only support update from version 0 to 1!");
			NoctuaInstanceUtil.destroyNoctuaInstance(this);
			return;
		}


		final DatastoreUpdateNewProfileDialog datastoreUpdateNewProfileDialog = (DatastoreUpdateNewProfileDialog)
												createController(DatastoreUpdateNewProfileDialog.class);

		AfterDestroyRunnable.create(datastoreUpdateNewProfileDialog, new Runnable() {
			@Override
			public void run() {
				updateDataToNewProfile(datastoreUpdateNewProfileDialog.getCreatedProfile());
			}
		});
	}


	private void updateDataToNewProfile(Profile profile) {
		String profileDir = profile.getDir();

		File newDatabaseFile = new File(profileDir + File.separator + "database.dat");
		File newImagesFile = new File(profileDir + File.separator + "images");

		File oldDatabaseFile = new File(datastoreManager.getDatastorePath() + File.separator + "database.dat");
		File oldCacheFile = new File(datastoreManager.getDatastorePath() + File.separator + "cache.dat");
		File oldConfigFile = new File(datastoreManager.getDatastorePath() + File.separator + "config.ini");
		File oldImagesDir = new File(datastoreManager.getDatastorePath() + File.separator + "images");

		oldCacheFile.delete();
		oldConfigFile.delete();

		oldDatabaseFile.renameTo(newDatabaseFile);
		oldImagesDir.renameTo(newImagesFile);

		datastoreManager.setDatastoreVersion(1);

		destroy();
	}
}
