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

import net.noctuasource.noctua.core.model.FlashCardElement;
import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.TreeNode;
import net.noctuasource.noctua.core.model.Language;
import net.noctuasource.noctua.core.model.Folder;
import net.noctuasource.noctua.core.model.FlashCardGroup;
import net.noctuasource.noctua.core.model.VocableMetaInfo;
import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import net.noctuasource.noctua.core.datastore.ProfilesContext;

import net.noctuasource.noctua.core.dao.impl.SessionHolder;
import net.noctuasource.noctua.core.database.DatabaseInitializationException;
import net.noctuasource.noctua.core.database.DatabaseInitializer;
import net.noctuasource.noctua.core.database.DatabaseVersionUpdater;
import net.noctuasource.noctua.core.model.ContentFlashCardElement;
import net.noctuasource.noctua.core.model.ExampleSentence;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.sqlite.hibernate.SQLiteDialect;





public class DatabaseInitializerImpl implements DatabaseInitializer {

	// -- Basic Static Members ------------------------

	static final Logger	logger = Logger.getLogger(DatabaseInitializerImpl.class);



	// -- Static Members ------------------------

	static final String	HIBERNATE_CNF = "hibernate/hibernate.cfg.xml";

	static final String	DATABASE_FILENAME = "database.dat";




	// -- Members ------------------------------

	private File					databaseFile;

	@Resource
	private ProfilesContext			profilesContext;

	@Resource
	private SessionHolder			sessionHolder;

	@Resource
	private DatabaseVersionUpdater	databaseVersionUpdater;





	// -- Inject Setters ------------------------

	public void setProfilesContext(ProfilesContext profilesContext) {
		this.profilesContext = profilesContext;
	}

	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public void setDatabaseVersionUpdater(DatabaseVersionUpdater databaseVersionUpdater) {
		this.databaseVersionUpdater = databaseVersionUpdater;
	}







	@Override
	public boolean openDatabase() throws DatabaseInitializationException {
		if(databaseFile != null) {
			throw new DatabaseInitializationException("Database already opened!");
		}

		logger.debug("Initialize database...");

		File dir = new File(profilesContext.getAbsoluteProfileDir());

		if(!dir.exists()) {
			throw new DatabaseInitializationException("Database dir does not exist!");
		}

		databaseFile = new File(dir + File.separator + DATABASE_FILENAME);

		logger.debug("Use database: " + databaseFile.toString());


    	Connection connection = null;

    	try {
			connection = getConnection();

        	databaseVersionUpdater.updateDatabase(connection);

        	connection.close();


            Configuration configuration = new Configuration();

            configuration.addAnnotatedClass(Language.class);
            configuration.addAnnotatedClass(TreeNode.class);
            configuration.addAnnotatedClass(Folder.class);
            configuration.addAnnotatedClass(FlashCardGroup.class);
            configuration.addAnnotatedClass(FlashCard.class);
            configuration.addAnnotatedClass(FlashCardElement.class);
            configuration.addAnnotatedClass(VocableMetaInfo.class);
            configuration.addAnnotatedClass(ContentFlashCardElement.class);
            configuration.addAnnotatedClass(ExampleSentence.class);

            configuration.setProperty("hibernate.dialect", SQLiteDialect.class.getName());
            configuration.setProperty("hibernate.connection.url",
            						"jdbc:sqlite:" + databaseFile.toString());
            configuration.setProperty("hibernate.connection.driver_class", org.sqlite.JDBC.class.getName());
            //configuration.setProperty("hibernate.connection.username", "");
            //configuration.setProperty("hibernate.connection.password", "");

            configuration.setProperty("hibernate.transaction.factory_class",
                    "org.hibernate.transaction.JDBCTransactionFactory");
            //configuration.setProperty("hibernate.current_session_context_class", "manage");

            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");

            SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession();

	    	sessionHolder.setCurrentSession(session);

	    	return true;
		} catch(Exception e) {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					logger.error("Could not close jdbc connection!", ex);
				}
			}

			String msg = "Error while initializing database!";
			//logger.error(msg, e);
			throw new DatabaseInitializationException(msg, e);
		}
	}


	@Override
	@PreDestroy
	public void closeDatabase() {
		logger.debug("Close database...");

		Session session = sessionHolder.getCurrentSession();

		if(session != null) {
			session.flush();
			session.close();

			sessionHolder.setCurrentSession(session);
		}

		databaseFile = null;
	}


	public File getDatabaseFile() {
		return databaseFile;
	}


	public Connection getConnection() throws SQLException {
	    DriverManager.registerDriver(new org.sqlite.JDBC());

	    Enumeration<Driver> drivers = DriverManager.getDrivers();
	    while(drivers.hasMoreElements()) {
	    	Driver driver = drivers.nextElement();
	    	logger.debug("Treiber: " + driver.toString());
	    }

	    String connectionString = "jdbc:sqlite:" + databaseFile.toString();
	    logger.debug("Connection-String: " + connectionString);
	    return DriverManager.getConnection(connectionString);
	}



	// Hibernate4 Source

	//	ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder();
	//
	//	ServiceRegistry serviceReg =
	//					serviceRegistryBuilder
	//							.applySetting("hibernate.dialect", SQLiteDialect.class.getName())
	//							.buildServiceRegistry();
	//
	//	MetadataSources metadataSources = new MetadataSources(serviceReg);
	//	metadataSources.addAnnotatedClass(Language.class);
	//
	//	Metadata metadata = metadataSources.buildMetadata();
	//
	//	SessionFactory sessionFactory = metadata.buildSessionFactory();
	//
	//	Session session = sessionFactory.withOptions()
	//								    .connection(connection)
	//								    .openSession();


}
