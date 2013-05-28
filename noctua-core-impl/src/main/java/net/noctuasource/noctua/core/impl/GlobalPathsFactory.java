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

import net.noctuasource.noctua.core.GlobalPaths;



/**
 * The factory for global paths.
 * @author thomy
 */
public class GlobalPathsFactory {

	private static GlobalPaths globalPaths = null;


	/**
	 * Returns the correct implementation of global paths.
	 */
	public static synchronized GlobalPaths getGlobalPaths() {
		if(globalPaths != null) {
			return globalPaths;
		} else {
			String os = System.getProperty("os.name").toLowerCase();

			if(os.indexOf("win") >= 0) {
				globalPaths = new GlobalPathsWindowsImpl();
			}
			else if(os.indexOf("mac") >= 0) {
				globalPaths = new GlobalPathsMacImpl();
			}
			else {
				globalPaths = new GlobalPathsUnixImpl();
			}

			return globalPaths;
		}
	}

}
