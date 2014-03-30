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
package net.noctuasource.noctua.core.ui.loading;


import org.apache.log4j.Logger;



/**
 * LoadingScreenManager.
 * @author Philipp Thomas
 */
public class LoadingScreenManager {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(LoadingScreenManager.class);


	// ***** Static Members ************************************************* //

	private static LoadingScreenManager	instance = null;


	// ***** Members ******************************************************** //

	private LoadingScreen		lastLoadingScreen;




	// ***** Constructor **************************************************** //

	public static LoadingScreenManager get() {
		if(instance == null) {
			instance = new LoadingScreenManager();
		}

		return instance;
	}


	public LoadingScreen showSplashLoadingScreen() {
		if(lastLoadingScreen != null) {
			return null;
		}

		lastLoadingScreen = SplashLoadingScreen.get();
		lastLoadingScreen.show();
		return lastLoadingScreen;
	}

	public LoadingScreen showNormalLoadingScreen() {
		if(lastLoadingScreen != null) {
			return null;
		}

		lastLoadingScreen = LoadingScreenView.get();
		lastLoadingScreen.show();
		return lastLoadingScreen;
	}


	public void hideLoadingScreen() {
		if(lastLoadingScreen != null) {
			lastLoadingScreen.hide();
			lastLoadingScreen = null;
		}
	}
}
