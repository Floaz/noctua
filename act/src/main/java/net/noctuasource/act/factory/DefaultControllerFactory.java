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
package net.noctuasource.act.factory;

import net.noctuasource.act.data.ReadonlyControllerParams;



/**
 * Default controller factory.
 *
 * @author Philipp Thomas
 */
public class DefaultControllerFactory implements ControllerFactory {


	// -- Members -------------------------------------

	private final Class<?>		controllerClass;



	public DefaultControllerFactory(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	public DefaultControllerFactory(String controllerClassName) {
		this(controllerClassName, null);
	}


	public DefaultControllerFactory(String controllerClassName, ClassLoader classLoader) {
		ClassLoader cl = classLoader;
		if(cl == null) {
			cl = getClass().getClassLoader();
		}


		try {
			controllerClass = cl.loadClass(controllerClassName);
		} catch(ClassNotFoundException ex) {
			throw new ControllerFactoryException("Could not find class " + controllerClassName);
		}
	}


	@Override
	public Object create(ReadonlyControllerParams params) {
		try {
			return controllerClass.newInstance();
		} catch(InstantiationException | IllegalAccessException ex) {
			throw new ControllerFactoryException("Could not instantiate class: " + ex.getLocalizedMessage());
		}
	}

}

