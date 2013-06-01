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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.controller.RootContextController;
import net.noctuasource.act.data.ApplicationControllerParams;
import net.noctuasource.act.events.DefaultControllerEventListener;
import net.noctuasource.noctua.core.ui.loading.LoadingScreenManager;
import net.noctuasource.noctua.core.ui.loading.SplashLoadingScreen;
import org.apache.log4j.Logger;


public class JavaFXApplication extends Application {

	// -- Basic Static Members ------------------------

	private static Logger			logger = Logger.getLogger(JavaFXApplication.class);


	// ***** Members ******************************************************** //


	// ***** Constructor **************************************************** //

	/**
	 * Main for starting Noctua.
	 */
	public static void main(String[] args) {
		JavaFXApplication.launch(args);
	}



	// ***** Methods ******************************************************** //


	@Override
	public void start(Stage stage) throws Exception {
		Platform.setImplicitExit(false);

		LoadingScreenManager.get().showSplashLoadingScreen();
		
		String[] appArgs = getParameters().getRaw().toArray(new String[0]);
		ApplicationControllerParams params = new ApplicationControllerParams(appArgs);
		final ContextController rootController =
							RootContextController.createRootController(NoctuaRootContextControllerImpl.class, params);
		rootController.addControllerEventListener(new DefaultControllerEventListener() {
			@Override
			public void onAfterControllerDestroyed(ContextController destroyedController) {
				if(destroyedController == rootController) {
					Platform.exit();
				}
			}
		});
	}

}
