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
 * Data that is used from controller.
 * @author Philipp Thomas
 */
public interface ControllerData extends ReadonlyControllerData {

	/**
	 * Add data with the name of the class as key.
	 */
	void set(Object dataObject);


	/**
	 * Add data with the given key.
	 */
	void set(String key, Object dataObject);


	/**
	 * Removes the data with the given key.
	 */
	void remove(String key);

}
