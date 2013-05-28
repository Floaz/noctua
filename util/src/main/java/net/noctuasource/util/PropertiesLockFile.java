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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Properties;






/**
 * A file that can be locked and stores properties.
 *
 * @author Philipp Thomas
 */
public class PropertiesLockFile {


	File lockFile = null;

	FileLock lock = null;

	FileChannel lockChannel = null;

	FileOutputStream lockStream = null;



	/**
	 * Creates class lock instance
	 *
	 * @param key
	 *            Unique application key
	 * @throws UnsupportedEncodingException
	 */
	public PropertiesLockFile(String filename) {
		lockFile = new File(filename);
	}


	/**
	 * Locks the file. Now another application instance can't gain lock.
	 *
	 * @throws LockException
	 */
	public void lock(Properties props, String comment)  throws LockException {
		try {
			lockStream = new FileOutputStream(lockFile);
			lockChannel = lockStream.getChannel();
			lock = lockChannel.tryLock();

			if(lock == null) {
				throw new LockException("Can't lock file \"" + lockFile + "\"!");
			}

			props.store(lockStream, comment);
		}
		catch(IOException ex) {
			throw new LockException("Can't lock file \"" + lockFile + "\"!", ex);
		}
	}



	/**
	 * Lock. Now another application instance can't gain lock.
	 *
	 * @throws IOException
	 */
	public Properties readProperties()  throws IOException {
		FileInputStream is = new FileInputStream(lockFile);

		Properties props = new Properties();
		props.load(is);

		return props;
	}




	/**
	 * Remove Lock. Now another application instance can gain lock.
	 *
	 * @throws Exception
	 */
	public void release() throws IOException {
		if (lock != null) {
			lock.release();
		}

		if (lockChannel != null) {
			lockChannel.close();
		}

		if (lockStream != null) {
			lockStream.close();
		}

		if (lockFile != null) {
			lockFile.delete();
		}
	}


	@Override
	protected void finalize() throws Throwable {
		this.release();
		super.finalize();
	}

}
