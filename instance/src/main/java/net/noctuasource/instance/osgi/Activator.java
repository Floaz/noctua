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



import net.noctuasource.instance.InstanceManager;
import net.noctuasource.instance.internal.InstanceManagerImpl;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;



/**
 * Extension of the default OSGi bundle activator
 */
public final class Activator implements BundleActivator
{
	private Logger logger = Logger.getLogger(Activator.class);


	private InstanceManager		instanceManager;

	private Watchdog			watchdog;


    public void start(BundleContext bundleContext) throws Exception {

    	Runnable noInstanceHook = new FrameworkShutdownRunner(bundleContext);


    	logger.debug("Create instance manager...");
    	instanceManager = new InstanceManagerImpl();
		instanceManager.addShutdownHook(noInstanceHook);

    	bundleContext.registerService( InstanceManager.class.getName(),
    												instanceManager, null);



    	watchdog = new Watchdog(instanceManager, noInstanceHook);
    	watchdog.start();
    }



    public void stop(BundleContext bundleContext) throws Exception {

    	if(watchdog != null) {
    		watchdog.stop();
    	}

    	if(instanceManager != null) {
    		logger.debug("Close all instances...");
    		//instanceManager.closeAllInstance();
    	}
    }
}

