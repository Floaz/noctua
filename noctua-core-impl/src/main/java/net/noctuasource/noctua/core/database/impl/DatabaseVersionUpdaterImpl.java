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
package net.noctuasource.noctua.core.database.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import net.noctuasource.noctua.core.database.DatabaseVersion;
import net.noctuasource.noctua.core.database.DatabaseVersionUpdater;

import org.apache.log4j.Logger;


public class DatabaseVersionUpdaterImpl implements DatabaseVersionUpdater {

	private static final int	NEEDED_VERSION = 2;

	private static final int	ILLEGAL_VERSION = 0;

	private static final String	VERSION_TABLE = "noctua_base_version";
	private static final String	VERSION_ATTR = "Version";



	private static Logger		logger = Logger.getLogger(DatabaseVersionUpdaterImpl.class);





	@Override
	public void updateDatabase(Connection conn) throws Exception {

		boolean autoCommit = conn.getAutoCommit();

		try {
			conn.setAutoCommit(false);

			Map<Integer, DatabaseVersion> versionMap = getVersionMap();

			int currentlyInstalledVersion = getInstalledVersion(conn);

			if(currentlyInstalledVersion == NEEDED_VERSION) {
				logger.debug("Database is up to date.");
				return;
			}
			else if(currentlyInstalledVersion > NEEDED_VERSION) {
				throw new Exception("Database is newer than the version of this program!");
			}

			logger.info("Database is not up to date.");
			logger.info("Upgrade from database version " + currentlyInstalledVersion
							+ " to " + NEEDED_VERSION + ".");

			if(currentlyInstalledVersion == ILLEGAL_VERSION) {
				installVersionTable(conn);
			}

			for(int i = currentlyInstalledVersion +1; i <= NEEDED_VERSION; ++i) {
				DatabaseVersion version = versionMap.get(i);

				if(version == null) {
					throw new Exception("DatabaseVersion " + i + " not installed. "
										+ "Can't upgrade database!");
				}

				try {
					logger.info("Upgrade to version " + i + "...");
					version.updateDatabase(conn);
					setInstalledVersion(conn, i);
					conn.commit();
				}
				catch(Exception e) {
					conn.rollback();
					throw e;
				}
			}
		} finally {
			conn.setAutoCommit(autoCommit);
		}
	}



	private Map<Integer, DatabaseVersion> getVersionMap() {
		Map<Integer, DatabaseVersion> versionMap = new HashMap<>();

		DatabaseVersion[] versions = new DatabaseVersion[0];

		for(DatabaseVersion versionObj : versions) {
			DatabaseVersion version = (DatabaseVersion) versionObj;

			if(!versionMap.containsKey(version.getVersion())) {
				versionMap.put(version.getVersion(), version);
			}
			else {
				logger.warn("DatabaseVersion with this id already exists!");
			}
		}

		return versionMap;
	}



	private int getInstalledVersion(Connection conn) {

		Statement	statement = null;
		ResultSet	result = null;

		try {
			statement = conn.createStatement();
			result = statement.executeQuery("SELECT " + VERSION_ATTR
											 + " FROM " + VERSION_TABLE);

			if(result.next()) {
				return result.getInt(VERSION_ATTR);
			}
		} catch (SQLException e) {
			logger.debug("Error while getting version number. ");
		}
		finally {
			if(result != null) {
				try { result.close(); } catch (SQLException e) {}
			}
			if(statement != null) {
				try { statement.close();} catch (SQLException e) {}
			}
		}

		return ILLEGAL_VERSION;
	}



	private void installVersionTable(Connection conn) throws SQLException {

		Statement	statement = null;

		try {
			statement = conn.createStatement();
			statement.execute("CREATE TABLE " + VERSION_TABLE
								+ "(" + VERSION_ATTR + " INTEGER)");
			statement.close();

			statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO " + VERSION_TABLE
									+ " VALUES(" + ILLEGAL_VERSION + ")");
			statement.close();

			conn.commit();
		} catch (SQLException e) {
			logger.error("Error while installing version table. ", e);
			throw e;
		}
		finally {
			if(statement != null) {
				try { statement.close();} catch (SQLException e) {}
			}

			conn.rollback();
		}
	}



	private void setInstalledVersion(Connection conn, int version) throws SQLException {

		Statement	statement = null;

		try {
			statement = conn.createStatement();
			statement.executeUpdate("UPDATE " + VERSION_TABLE + " SET "
									+ VERSION_ATTR + "=\"" + version + "\"");
			statement.close();
		} catch (SQLException e) {
			logger.error("Error while incrementing version number. ", e);
			throw e;
		}
		finally {
			if(statement != null) {
				try { statement.close();} catch (SQLException e) {}
			}
		}
	}

}
