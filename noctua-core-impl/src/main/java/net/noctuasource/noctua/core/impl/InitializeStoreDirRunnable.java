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



import java.io.File;
import net.noctuasource.noctua.core.GlobalPaths;
import org.apache.log4j.Logger;

import net.noctuasource.instance.Instance;
import net.noctuasource.instance.InstanceRunnable;






public class InitializeStoreDirRunnable implements InstanceRunnable {

	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(InitializeStoreDirRunnable.class);


	// -- Static Members ------------------------


	// -- Members ------------------------------

	@Override
	public boolean run(Instance instance) {
		logger.info("Initialize store dir...");

		GlobalPaths paths = GlobalPathsFactory.getGlobalPaths();

		String storeDirString = paths.getStoreDir();
		File storeDir = new File(storeDirString);
		if(!storeDir.exists()) {
			storeDir.mkdirs();
		}

		instance.getData().set("storedir", storeDirString);
		return true;
	}

}
