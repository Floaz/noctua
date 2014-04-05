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
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.inject.Inject;
import net.noctuasource.noctua.core.datastore.ProfilesContext;

import net.noctuasource.noctua.core.dao.impl.SessionHolder;
import net.noctuasource.noctua.core.database.DatabaseInitializationException;
import net.noctuasource.noctua.core.database.DatabaseInitializer;
import net.noctuasource.noctua.core.database.DatabaseVersionUpdater;
import net.noctuasource.noctua.core.model.ExampleSentence;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.sqlite.hibernate.SQLiteDialect;





public class DatabaseInitializerImpl implements DatabaseInitializer {

	// -- Basic Static Members ------------------------

	static final Logger	logger = Logger.getLogger(DatabaseInitializerImpl.class);



	// -- Static Members ------------------------

	static final String	HIBERNATE_CNF = "hibernate/hibernate.cfg.xml";

	static final String	DATABASE_FILENAME = "database.dat";




	// -- Members ------------------------------

	private File					databaseFile;

	private SessionFactory			sessionFactory;


	@Resource
	private ProfilesContext			profilesContext;

	@Resource
	private SessionHolder			sessionHolder;

	@Resource
	private DatabaseVersionUpdater	databaseVersionUpdater;
//
//	@Autowired
//	private TransactionManager		txManager;

	@Inject
	private TransactionManagerProxy	managerProxy;



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

//	public void setTxManager(TransactionManager txManager) {
//		this.txManager = txManager;
//	}

	public void setManagerProxy(TransactionManagerProxy managerProxy) {
		this.managerProxy = managerProxy;
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
            configuration.addAnnotatedClass(ExampleSentence.class);

            configuration.setProperty("hibernate.dialect", SQLiteDialect.class.getName());
            configuration.setProperty("hibernate.connection.url",
            						  "jdbc:sqlite:" + databaseFile.toString());
            configuration.setProperty("hibernate.connection.driver_class", org.sqlite.JDBC.class.getName());

			configuration.setProperty("hibernate.c3p0.min_size", "1");
			configuration.setProperty("hibernate.c3p0.max_size", "3");
			configuration.setProperty("hibernate.c3p0.timeout", "300");
			configuration.setProperty("hibernate.c3p0.max_statements", "50");
			configuration.setProperty("hibernate.c3p0.idle_test_period", "3000");


			//configuration.setProperty("hibernate.connection.driver_class", TransactionalDriver.class.getName());
//
//            configuration.setProperty("hibernate.transaction.factory_class",
//									  "org.hibernate.transaction.JTATransactionFactory");
//
//            configuration.setProperty("hibernate.current_session_context_class", "jta");

//            configuration.setProperty("hibernate.transaction.manager_lookup_class",
//									  "org.hibernate.transaction.");
//
//            configuration.setProperty("transaction.jta.platform",
//									  "org.hibernate.service.jta.platform.internal.JBossStandAloneJtaPlatform");

            //configuration.setProperty("hibernate.connection.autocommit", "true");

            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");



			List<Class<?>> annotatedClasses = new LinkedList<>();
            annotatedClasses.add(Language.class);
            annotatedClasses.add(TreeNode.class);
            annotatedClasses.add(Folder.class);
            annotatedClasses.add(FlashCardGroup.class);
            annotatedClasses.add(FlashCard.class);
            annotatedClasses.add(FlashCardElement.class);
            annotatedClasses.add(VocableMetaInfo.class);
            annotatedClasses.add(ExampleSentence.class);

			LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
			sessionFactoryBean.setHibernateProperties(configuration.getProperties());
			sessionFactoryBean.setAnnotatedClasses(annotatedClasses.toArray(new Class<?>[]{}));
			//sessionFactoryBean.setJtaTransactionManager(txManager);
			//sessionFactoryBean.setDataSource(null);
			sessionFactoryBean.afterPropertiesSet();

//			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
//			builder.applySettings(configuration.getProperties());
//			ServiceRegistry serviceRegistry = builder.build();
//            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			sessionFactory = sessionFactoryBean.getObject();
            //Session session = sessionFactory.openSession();
	    	sessionHolder.setSessionFactory(sessionFactory);


			HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
			transactionManager.afterPropertiesSet();

			managerProxy.setManager(transactionManager);

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

		sessionFactory.close();
		sessionFactory = null;

		managerProxy.setManager(null);

//
//		Session session = sessionHolder.getCurrentSession();
//
//		if(session != null) {
//			session.flush();
//			session.close();
//
//			sessionHolder.setSessionFactory(session);
//		}

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
