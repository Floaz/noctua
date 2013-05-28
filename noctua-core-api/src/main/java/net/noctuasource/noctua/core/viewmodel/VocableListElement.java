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
package net.noctuasource.noctua.core.viewmodel;





public class VocableListElement {

	private Long			id;

	private String			foreignString;

	private String			nativeString;

	private String			sentence;


	
	public VocableListElement(Long id, String foreignString, String nativeString, String sentence) {
		this.id = id;
		this.foreignString = foreignString;
		this.nativeString = nativeString;
		this.sentence = sentence;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getForeignString() {
		return foreignString;
	}

	public void setForeignString(String foreignString) {
		this.foreignString = foreignString;
	}

	public String getNativeString() {
		return nativeString;
	}

	public void setNativeString(String nativeString) {
		this.nativeString = nativeString;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}


}
