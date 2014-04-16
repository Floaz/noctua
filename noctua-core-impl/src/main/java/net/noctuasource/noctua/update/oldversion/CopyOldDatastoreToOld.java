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
package net.noctuasource.noctua.update.oldversion;



import net.noctuasource.noctua.core.impl.GlobalPathsFactory;
import java.io.File;
import net.noctuasource.act.annotation.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.GlobalPaths;
import net.noctuasource.noctua.core.ui.MessageDialog;
import org.apache.log4j.Logger;







public class CopyOldDatastoreToOld extends SubContextController {

	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(CopyOldDatastoreToOld.class);


	// -- Static Members ------------------------

	static final String		NEW_OLD_DIR = "old/";
	static final String		VOCABLE_DATA_FILE = "VOKABELN.MDB";


	// -- Members ------------------------------


	@RunLater
	public void run() {
		final GlobalPaths paths = GlobalPathsFactory.getGlobalPaths();
		if(paths.getOldStoreDir() == null) {
			destroy();
			return;
		}


		File oldStoreDir = new File(paths.getOldStoreDir());
		if(!oldStoreDir.exists() || !oldStoreDir.isDirectory()) {
			destroy();
			return;
		}

		logger.info("Old data exists and will be moved...");

		final File destination = new File(paths.getNewOldStoreDir());
		oldStoreDir.renameTo(destination);


		String message = "Die Daten der alten Noctua Version (4.22 oder älter) wurden von \"" + paths.getOldStoreDir()
						 + "\" nach \"" + destination.getAbsolutePath() + "\" verschoben!";

		MessageDialog.create(null, "Alte Daten verschoben", message, new MessageDialog.Listener() {
			@Override
			public boolean onMessageClose() {
				destroy();
				return true;
			}
		});
	}

}
