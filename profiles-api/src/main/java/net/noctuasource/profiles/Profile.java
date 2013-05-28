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



public class Profile {

	private String					m_dir;
	
	private ProfileData				m_data = new ProfileData();

	
	
	
	public Profile() {
	}




	public String getDir() {
		return m_dir;
	}


	public void setDir(String dir) {
		this.m_dir = dir;
	}


	public ProfileData getData() {
		return m_data;
	}


	public void setData(ProfileData data) {
		m_data = data;
	}
	
	
	public String toString() {
		return getData().getName();
	}

	
}
