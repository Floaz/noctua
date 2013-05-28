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




/**
 * Parameters that are used from controller.
 * @author Philipp Thomas
 */
public class ControllerParamsBuilder {


	//private Map<Class<?>, Object>	dataByType = new HashMap<>();

	private ControllerParams params = new ControllerParamsImpl();


	/**
	 * Add a parameter with the name of the class as key.
	 */
	public ControllerParamsBuilder add(Object dataObject) {
		add(dataObject.getClass().getName(), dataObject);
		return this;
	}


	/**
	 * Add a parameter with the given key.
	 */
	public ControllerParamsBuilder add(String key, Object dataObject) {
		params.set(key, dataObject);
		return this;
	}



	/**
	 * Build the params.
	 */
	public ControllerParams build() {
		return params;
	}


	/**
	 * Build the params.
	 */
	public static ControllerParamsBuilder create() {
		return new ControllerParamsBuilder();
	}

}
