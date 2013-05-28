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
package net.noctuasource.act.controller;

import net.noctuasource.act.data.*;
import java.util.HashMap;
import java.util.Map;





/**
 * The implementation of ControllerData which lookup wholte tree path.
 * @author Philipp Thomas
 */
class ControllerDataTreeImpl implements ControllerData {


	private Map<String, Object>		dataByName = new HashMap<>();

	private ContextController		controller;


	/**
	 * Constructor.
	 * @param controller
	 */
	ControllerDataTreeImpl(ContextController controller) {
		this.controller = controller;
	}



	/*
	 * Get the parameter with the full name of the class as key.
	 */
	@Override
	public <T> T get(Class<T> clazz) {
		return (T) get(clazz.getName());
	}


	/*
	 * Get the parameter with the given key.
	 * @throw ClassCastException
	 */
	@Override
	public <T> T get(String key, Class<T> clazz) {
		return (T) get(key);
	}


	/*
	 * Get the parameter with the given key.
	 */
	@Override
	public Object get(String key) {
		ContextController currentController = controller;
		while(currentController != null) {
			Object dataObject = currentController.getLocalControllerData().get(key);
			if(dataObject != null) {
				return dataObject;
			}

			currentController = currentController.getParentController();
		}

		return null;
	}




	/*
	 * Get the parameter with the full name of the class as key.
	 */
	@Override
	public <T> T getOrThrow(Class<T> clazz) {
		return (T) getOrThrow(clazz.getName());
	}


	/*
	 * Get the parameter with the given key.
	 * @throw ClassCastException
	 */
	@Override
	public <T> T getOrThrow(String key, Class<T> clazz) {
		return (T) getOrThrow(key);
	}


	/*
	 * Get the parameter with the given key.
	 */
	@Override
	public Object getOrThrow(String key) {
		Object o = get(key);
		if(o == null) {
			throw new RuntimeException("Illegal key.");
		}
		return o;
	}


	@Override
	public void set(Object dataObject) {
		controller.getLocalControllerData().set(dataObject);
	}


	@Override
	public void set(String key, Object dataObject) {
		controller.getLocalControllerData().set(key, dataObject);
	}


	@Override
	public void remove(String key) {
		controller.getLocalControllerData().remove(key);
	}

}
