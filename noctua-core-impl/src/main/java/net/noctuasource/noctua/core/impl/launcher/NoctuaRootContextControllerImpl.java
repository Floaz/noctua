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

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.noctuasource.act.controller.RootContextController;
import net.noctuasource.act.javafx.ExceptionHandler;
import net.noctuasource.act.javafx.JfxPlatformExecutor;
import net.noctuasource.noctua.core.ExecutorIdentifiers;
import org.apache.log4j.Logger;






/**
 * The launcher for Noctua.
 */
public class NoctuaRootContextControllerImpl extends RootContextController {


	// -- Basic Static Members ------------------------

	private static Logger			logger = Logger.getLogger(NoctuaRootContextControllerImpl.class);


	// -- Members ------------------------------

	private ExecutorService			backgroundExecutor = null;





	/**
	 * Setting some basic stuff.
	 */
	@Override
	protected void onInit() {
		setupLogging();

		final ExceptionHandler exceptionHandler = new JavaFxExceptionHandler();
		Executor javafxExecutor = new JfxPlatformExecutor(exceptionHandler);
		addExecutor(ExecutorIdentifiers.JAVAFX_EXECUTOR, javafxExecutor);
		setDefaultExecutor(ExecutorIdentifiers.JAVAFX_EXECUTOR);

		backgroundExecutor = Executors.newSingleThreadExecutor();
		addExecutor(ExecutorIdentifiers.BACKGROUND_EXECUTOR, new Executor() {
			@Override
			public void execute(Runnable command) {
				try {
					backgroundExecutor.execute(command);
				}
				catch(RuntimeException e) {
					logger.error("Error while executing background task. ", e);
					exceptionHandler.handleException(e);
				}
			}
		});

		// Start Noctua instance.
		createController(NoctuaInstanceController.class);
	}



	@Override
	public void onDestroy() {
		if(backgroundExecutor != null) {
			backgroundExecutor.shutdown();
		}
	}



	private void setupLogging() {
		//DOMConfigurator.configure(LOG4J_CONFIG_FILE);
	}

}
