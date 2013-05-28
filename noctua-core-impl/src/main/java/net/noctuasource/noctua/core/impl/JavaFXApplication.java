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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

public class JavaFXApplication extends Application {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(JavaFXApplication.class);

	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //


	// ***** Constructor **************************************************** //



	// ***** Methods ******************************************************** //


	@Override
	public void init() throws Exception {
		super.init();
	}


	@Override
	public void start(Stage stage) throws Exception {
		logger.debug("Starting JavaFX App...");

		Platform.setImplicitExit(false);
	}


	@Override
	public void stop() throws Exception {
		logger.debug("Stopping JavaFX App...");
		super.stop();
	}


	static public void quitApp() {
		Platform.exit();
	}
}
