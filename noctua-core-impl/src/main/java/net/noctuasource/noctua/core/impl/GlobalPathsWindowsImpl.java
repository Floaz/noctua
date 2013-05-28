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

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Shell32Util;
import com.sun.jna.platform.win32.ShlObj;
import java.io.File;
import net.noctuasource.noctua.core.GlobalPaths;



/**
 * Global noctua paths for windows.
 * @author thomy
 */
public class GlobalPathsWindowsImpl implements GlobalPaths {

	static final String	STORE_DIR ="Noctua";
	static final String	PROFILES_DIR ="profiles";
	static final String	NEW_OLD_STORE_DIR = "old";


	static String storeDir;
	static String oldStoreDir = null;



	public GlobalPathsWindowsImpl() {
		String appDataDir = getAppDataDir();
		//String homeDir = System.getProperty("user.home");
		String dataDir = "Noctua";

		if(appDataDir.isEmpty()) {
			throw new IllegalStateException("Illegal appdata dir!");
		}

		storeDir = new File(appDataDir + File.separator + dataDir).getAbsolutePath();
		oldStoreDir = new File(getMyDocumentsDir() + File.separator + "Noctua").getAbsolutePath();
	}


	final String getAppDataDir() {
		return Shell32Util.getFolderPath(ShlObj.CSIDL_APPDATA);
	}


	final String getMyDocumentsDir() {
        if (Platform.isWindows()) {
			return Shell32Util.getFolderPath(ShlObj.CSIDL_LOCAL_APPDATA);
//
//            HWND hwndOwner = null;
//            int nFolder = ShlObj.CSIDL_LOCAL_APPDATA;
//            HANDLE hToken = null;
//            DWORD dwFlags = ShlObj.SHGFP_TYPE_CURRENT;
//            char[] pszPath = new char[ShlObj.MAX_PATH];
//
//            int hResult = Shell32.INSTANCE.SHGetFolderPath(hwndOwner, nFolder, hToken, dwFlags, pszPath);
//
//            if (Shell32.S_OK == hResult) {
//                String path = new String(pszPath);
//                int len = path.indexOf('\0');
//                path = path.substring(0, len);
//                return path;
//            } else {
//                throw new RuntimeException("Error while retrieving my documents dir!");
//            }
        } else {
			throw new IllegalStateException("Platform is not Windows!");
		}
    }

//
//    static class HANDLE extends PointerType implements NativeMapped {
//    }




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
