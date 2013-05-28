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
package net.noctuasource.act.registry;

import java.util.HashMap;
import java.util.Map;
import net.noctuasource.act.controller.ContextController;

/**
 * Registry for lookup controllers.
 *
 * @author Philipp Thomas
 */
public class DefaultControllerLookupRegistry implements ControllerLookupRegistry {


	// -- Members -------------------------------------

	private Map<String, Class<? extends ContextController>> mapping = new HashMap<>();



	@Override
	public synchronized Class<? extends ContextController> lookup(String controllerKey) {
		return mapping.get(controllerKey);
	}


	public synchronized void addControllerClass(String controllerKey, Class<? extends ContextController> controllerClass) {
		mapping.put(controllerKey, controllerClass);
	}

	public synchronized void removeControllerClass(String controllerKey) {
		mapping.remove(controllerKey);
	}

}

