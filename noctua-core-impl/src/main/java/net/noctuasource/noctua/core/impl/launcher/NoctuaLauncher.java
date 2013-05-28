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
package net.noctuasource.noctua.core.impl.launcher;

import java.io.IOException;
import java.util.Properties;
import net.noctuasource.act.registry.ControllerLookupRegistry;
import net.noctuasource.act.controller.RootContextController;
import net.noctuasource.act.data.ApplicationControllerParams;
import net.noctuasource.act.javafx.ExceptionHandler;
import net.noctuasource.act.javafx.JfxPlatformExecutor;
import net.noctuasource.act.spring.SpringAutowireControllerEventListener;
import net.noctuasource.act.spring.SpringDefaultConstants;

import net.noctuasource.noctua.core.impl.JavaFXApplication;
import net.noctuasource.noctua.core.ui.ExceptionDialog;

import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;




/**
 * The launcher for Noctua.
 */
public class NoctuaLauncher extends RootContextController {


	// -- Basic Static Members ------------------------

	private static Logger			logger = Logger.getLogger(NoctuaLauncher.class);



	// -- Static Members ------------------------

	private static final String		PROPERTIES_FILE = "/noctua.properties";
	private static final String		LOG4J_CONFIG_FILE = "log4j.xml";
	private static final String		CONTEXT_FILE = "/spring/application-context.xml";



	// -- Members ------------------------------

	private Properties							properties = new Properties();

	private ConfigurableApplicationContext		applicationContext;



	/**
	 * Main for starting Noctua.
	 */
	public static void main(String[] args) {
		try {
			ApplicationControllerParams params = new ApplicationControllerParams(args);
			RootContextController.createRootController(NoctuaLauncher.class, params);
		}
		catch(Exception e) {
			logger.fatal("Error while launching Noctua!", e);
		}
	}



	/**
	 * Entry point for starting Noctua.
	 */
	@Override
	protected void onInit() {
		// First independent things to load.
		setupLogging();

		loadProperties();


		// Start JavaFX.
        Thread launcherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JavaFXApplication.launch(JavaFXApplication.class, "");
				}
				catch(Exception e) {
					logger.error("Error while launching java-fx-app! ", e);
				}
			}
		});
        launcherThread.setName("JavaFX-LauncherMain");
        launcherThread.start();


		// Set javafx platform as executor.
		ExceptionHandler exceptionHandler = new ExceptionHandler() {
			@Override
			public void handleException(Exception exception) {
				ExceptionDialog.create(exception);
			}
		};

		setExecutor(new JfxPlatformExecutor(exceptionHandler));


		// Load and refresh application context.
		applicationContext = new ClassPathXmlApplicationContext(CONTEXT_FILE);
		String contextDataKey = SpringDefaultConstants.DEFAULT_CONTEXT_DATA_KEY;
		getControllerData().set(contextDataKey, applicationContext);
		addControllerEventListener(new SpringAutowireControllerEventListener(contextDataKey));

		ControllerLookupRegistry controllerLookupRegistry = applicationContext.getBean(ControllerLookupRegistry.class);
		addControllerLookupRegistry(controllerLookupRegistry);


		// Start Noctua instance.
		executeController(NoctuaInstanceController.class);
	}



	@Override
	public void onDestroy() {
		JavaFXApplication.quitApp();
		applicationContext.close();
	}



	private void setupLogging() {
		//DOMConfigurator.configure(LOG4J_CONFIG_FILE);
	}


	private void loadProperties() {
		try {
			properties.load(getClass().getResourceAsStream(PROPERTIES_FILE));
		}
		catch(IOException e) {
			logger.fatal("Could not load properties!");
			System.exit(-1);
		}

		properties.setProperty("instance.host.port.min", "28000");
		properties.setProperty("instance.host.port.max", "29000");
	}

}
