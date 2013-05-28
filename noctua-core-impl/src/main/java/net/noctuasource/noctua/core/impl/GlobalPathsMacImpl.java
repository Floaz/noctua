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



/**
 * Global noctua paths for MAC.
 * @author thomy
 */
public class GlobalPathsMacImpl implements GlobalPaths {

	static final String	STORE_DIR = "Noctua";
	static final String	PROFILES_DIR = "profiles";
	static final String	NEW_OLD_STORE_DIR = "old";


	static String storeDir;
	static String oldStoreDir = null;



	public GlobalPathsMacImpl() {
		String homeDir = System.getProperty("user.home");
		storeDir = new File(homeDir + File.separator + "Library" + File.separator  + STORE_DIR).getAbsolutePath();
	}


	/**
	 * Returns the store dir.
	 */
	@Override
	public String getStoreDir() {
		return storeDir;
	}


	/**
	 * Returns the profiles dir.
	 */
	@Override
	public String getProfilesDir() {
		return new File(storeDir + File.separator + PROFILES_DIR).getAbsolutePath();
	}


	/**
	 * Returns the old store dir of Noctua version 4.x.
	 */
	@Override
	public String getOldStoreDir() {
		return oldStoreDir;
	}


	/**
	 * Returns the new copied old store dir of Noctua version 4.x.
	 */
	@Override
	public String getNewOldStoreDir() {
		return new File(storeDir + File.separator + NEW_OLD_STORE_DIR).getAbsolutePath();
	}

}
