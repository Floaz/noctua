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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import net.noctuasource.noctua.core.GlobalPaths;
import net.noctuasource.noctua.core.datastore.DatastoreManager;
import net.noctuasource.noctua.core.impl.GlobalPathsFactory;

import org.apache.log4j.Logger;


/**
 * DatastoreManager
 * @author Philipp Thomas
 */
public class DatastoreManagerImpl implements DatastoreManager {

	// -- Basic Static Members ------------------------

	private static Logger		logger = Logger.getLogger(DatastoreManagerImpl.class);



	// -- Static Members ------------------------

	private static final int	NEEDED_VERSION = 1;

	private static final int	ILLEGAL_VERSION = 0;

	private static final String	LOCAL_DATASTORE_KEY = "noctua.datastore";
	private static final String	LOCAL_DATASTORE_VALUE = "local";

	private static final String	VERSION_KEY = "version";
	private static final String	DATASTORE_INFO_FILENAME = "datastore.properties";




	@Override
	public boolean existsDatastore() {
		File datastore = getDatastoreFile();
		return datastore.exists();
	}

	@Override
	public void createDatastore() {
		File datastore = getDatastoreFile();
		datastore.mkdirs();

		setDatastoreVersion(1);
	}

	@Override
	public int getDatastoreVersion() {
		File propsFile = new File(getDatastoreFile() + File.separator + DATASTORE_INFO_FILENAME);
		if(!propsFile.exists()) {
			return ILLEGAL_VERSION;
		}

		Properties props = new Properties();
		try {
			FileInputStream stream = new FileInputStream(propsFile);
			props.load(stream);
		} catch (IOException ex) {
			logger.fatal("Could not load datastore version.");
			throw new RuntimeException("Could not load datastore version.");
		}

		Integer version = Integer.parseInt(props.getProperty(VERSION_KEY, Integer.toString(ILLEGAL_VERSION)));
		return version;
	}

	@Override
	public int getNeededDatastoreVersion() {
		return NEEDED_VERSION;
	}

	@Override
	public void setDatastoreVersion(int newVersionNumber) {
		File propsFile = new File(getDatastoreFile() + File.separator + DATASTORE_INFO_FILENAME);

		Properties props = new Properties();

		try {
			FileInputStream stream = new FileInputStream(propsFile);
			props.load(stream);
		} catch (IOException ex) {
		}

		props.setProperty(VERSION_KEY, Integer.toString(newVersionNumber));

		try {
			FileOutputStream stream = new FileOutputStream(propsFile);
			props.store(stream, "");
		} catch (IOException ex) {
			logger.fatal("Could not load datastore version.");
			throw new RuntimeException("Could not load datastore version.");
		}
	}

	@Override
	public String getDatastorePath() {
		return getDatastoreFile().getAbsolutePath();
	}



	private File getDatastoreFile() {
		String localDatastoreProperty = System.getProperty(LOCAL_DATASTORE_KEY);

		if(localDatastoreProperty != null && localDatastoreProperty.equals(LOCAL_DATASTORE_VALUE)) {
			return new File(System.getProperty("user.dir"));
		}
		else {
			GlobalPaths paths = GlobalPathsFactory.getGlobalPaths();
			return new File(paths.getStoreDir());
		}
	}


}
