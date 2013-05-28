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
package net.noctuasource.profiles;

import java.util.HashMap;
import java.util.Map;

public class ProfileData {

	public static final int	DEFAULT_VERSION = 0;
	
	private int						version;
	
	private String					name;
	
	private Map<String, String>		metaData = new HashMap<String, String>();

	
	
	
	public ProfileData() {
	}


	public ProfileData(String name) {
		this.version = DEFAULT_VERSION;
		this.name = name;
	}

	
	public ProfileData(int version, String name) {
		this.version = version;
		this.name = name;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData(String key) {
		return metaData.get(key);
	}

	public Iterable<String> getAllDataKeys() {
		return metaData.keySet();
	}

	public void setData(String key, String value) {
		this.metaData.put(key, value);
	}
	
	
}
