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
package net.noctuasource.instance.osgi;

import org.apache.log4j.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class FrameworkShutdownRunner implements Runnable {

	static Logger logger = Logger.getLogger(FrameworkShutdownRunner.class);
	
	private BundleContext			bundleContext;
	
	
	
	public FrameworkShutdownRunner(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	
	
	@Override
	public void run() {
		logger.info("Invoke shutdown of application...");
		
		Bundle systemBundle = bundleContext.getBundle(0);
		
		try {
			systemBundle.stop();
		} catch (BundleException e) {
			logger.error("Error while shutdowning! " + e.getMessage());
		}
	}
}
