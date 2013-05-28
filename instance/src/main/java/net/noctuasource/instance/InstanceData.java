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
package net.noctuasource.instance;

import java.util.HashMap;
import java.util.Map;


/**
 * Data that is hold by an instance.
 * @author Philipp Thomas
 */
public class InstanceData {

	public static final String		ARGS_DATA_KEY = "args";



	private Map<String, Object>		data = new HashMap<>();



	public Object get(String key) {
		return data.get(key);
	}


	public void set(String key, Object dataObject) {
		if( dataObject != null) {
			data.put(key, dataObject);
		} else {
			data.remove(key);
		}
	}


	public void remove(String key) {
		data.remove(key);
	}

}
