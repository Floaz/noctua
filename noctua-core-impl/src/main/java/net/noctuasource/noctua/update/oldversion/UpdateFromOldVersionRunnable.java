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



import net.noctuasource.act.annotation.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.impl.GlobalPathsFactory;
import net.noctuasource.noctua.core.GlobalPaths;
import org.apache.log4j.Logger;







public class UpdateFromOldVersionRunnable extends SubContextController {

	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(UpdateFromOldVersionRunnable.class);


	// -- Static Members ------------------------

	static final String		VOCABLE_DATA_FILE = "VOKABELN.MDB";


	// -- Members ------------------------------


	@RunLater
	public boolean run() {
		GlobalPaths paths = GlobalPathsFactory.getGlobalPaths();
		if(paths.getOldStoreDir() == null) {
			return true;
		}

		return true;

//
//
//		File oldStoreDir = paths.getOldStoreDir();
//		if(!oldStoreDir.exists() || !oldStoreDir.isDirectory()) {
//			return true;
//		}
//
//		File destination = new File("c:\\some_directory\\new_directory");
//
//		source.renameTo(destination);
//
//		logger.info("Instance run old version update...");
//
//		String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + VOCABLE_DATA_FILE + ";";
//		Connection conn = DriverManager.getConnection(database, "", "");
//
//		Statement s = conn.createStatement();
//
//// create a table
//
//		String tableName = "myTable" + String.valueOf((int) (Math.random() * 1000.0));
//
//		String createTable = "CREATE TABLE " + tableName
//				+ " (id Integer, name Text(32))";
//
//		s.execute(createTable);
//
//		final RootContextController controller = (RootContextController) instance.getData().get("rootController");
//
//		ControllerDestroyWaiter waiter = new ControllerDestroyWaiter();
//
//		final ControllerParams params = new ControllerParams();
//		params.add(waiter);
//
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				controller.executeController(MainWindowView.class, params);
//			}
//		});
//
//
//		// Returns true, if correct destroy, false if interrupted.
//		return waiter.waitForDestroy();
	}

}
