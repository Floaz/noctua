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
package net.noctuasource.profiles.impl.osgi;

import net.noctuasource.profiles.ProfileManagerFactory;
import net.noctuasource.profiles.impl.DefaultProfileManagerFactory;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;




public class Activator implements BundleActivator 
{	
	static Logger logger = Logger.getLogger(Activator.class);
	
	
    public void start(BundleContext context) {
    	
    	logger.debug("Register default profile manager factory service.");
    	
        context.registerService( ProfileManagerFactory.class.getName(),
        						 new DefaultProfileManagerFactory(), null);
    }


    
    public void stop(BundleContext context) {
    	logger.debug("Unregister default profile manager service.");
    }
}


