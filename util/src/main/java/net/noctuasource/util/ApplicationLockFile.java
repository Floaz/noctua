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
package net.noctuasource.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Properties;

/**
 * AppLock.
 *
 * @author Philipp Thomas
 */
public class ApplicationLockFile {

	private static String PROPERTIES_COMMENT = "Application Lock File";


	private PropertiesLockFile propertiesLockFile;

	private String key = null;



	/**
	 * Creates class lock instance
	 *
	 * @param key Unique application key
	 * @throws UnsupportedEncodingException
	 */
	public ApplicationLockFile(String key, boolean userSeperated) {
		String username = System.getProperty("user.name");
		String userString;
		try {
			userString = userSeperated ? URLEncoder.encode(username, "UTF-8") : "";
		} catch (UnsupportedEncodingException e) {
			userString = "";
		}

		String tmpDir = System.getProperty("java.io.tmpdir");
		if (!tmpDir.endsWith(System.getProperty("file.separator"))) {
			tmpDir += System.getProperty("file.separator");
		}

		File lockFile = null;

		// Acquire MD5
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			md5.reset();

			String hashText = new BigInteger(1,
										md5.digest(key.getBytes())).toString(16);

			while (hashText.length() < 32) {
				hashText = "0" + hashText;
			}

			lockFile = new File(tmpDir + userString + hashText + ".lock");
		} catch (Exception ex) {
		}

		// MD5 acquire fail
		if (lockFile == null) {
			lockFile = new File(tmpDir + userString + key + ".lock");
		}

		propertiesLockFile = new PropertiesLockFile(lockFile.getAbsolutePath());

		this.key = key;
	}


	/**
	 * Lock. Now another application instance can't gain lock.
	 */
	public void lock(Properties props)  throws LockException {
		Properties storeProps = (Properties) props.clone();
		storeProps.setProperty("lock.key", key);

		propertiesLockFile.lock(props, PROPERTIES_COMMENT);
	}



	/**
	 * Lock. Now another application instance can't gain lock.
	 *
	 * @throws IOException
	 */
	public Properties readProperties()  throws IOException {
		return propertiesLockFile.readProperties();
	}



	/**
	 * Remove Lock. Now another application instance can gain lock.
	 *
	 * @throws IOException
	 */
	public void release() throws IOException {
		propertiesLockFile.release();
	}
}
