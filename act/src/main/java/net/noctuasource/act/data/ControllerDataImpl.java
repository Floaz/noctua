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
package net.noctuasource.act.data;

import java.util.HashMap;
import java.util.Map;





/**
 * The implementation of ControllerData.
 * @author Philipp Thomas
 */
public class ControllerDataImpl implements ControllerData {


	private Map<String, Object>		dataByName = new HashMap<>();



	/*
	 * Get the parameter with the full name of the class as key.
	 */
	@Override
	public synchronized <T> T get(Class<T> clazz) {
		return (T) dataByName.get(clazz.getName());
	}


	/*
	 * Get the parameter with the given key.
	 * @throw ClassCastException
	 */
	@Override
	public synchronized <T> T get(String key, Class<T> clazz) {
		return (T) dataByName.get(key);
	}


	/*
	 * Get the parameter with the given key.
	 */
	@Override
	public synchronized Object get(String key) {
		return dataByName.get(key);
	}




	/*
	 * Get the parameter with the full name of the class as key.
	 */
	@Override
	public synchronized <T> T getOrThrow(Class<T> clazz) {
		return (T) getOrThrow(clazz.getName());
	}


	/*
	 * Get the parameter with the given key.
	 * @throw ClassCastException
	 */
	@Override
	public synchronized <T> T getOrThrow(String key, Class<T> clazz) {
		return (T) getOrThrow(key);
	}


	/*
	 * Get the parameter with the given key.
	 */
	@Override
	public synchronized Object getOrThrow(String key) {
		Object o = dataByName.get(key);
		if(o == null) {
			throw new RuntimeException("Illegal key.");
		}
		return o;
	}


	@Override
	public synchronized void set(Object dataObject) {
		dataByName.put(dataObject.getClass().getName(), dataObject);
	}


	@Override
	public synchronized void set(String key, Object dataObject) {
		dataByName.put(key, dataObject);
	}


	@Override
	public synchronized void remove(String key) {
		dataByName.remove(key);
	}

}
