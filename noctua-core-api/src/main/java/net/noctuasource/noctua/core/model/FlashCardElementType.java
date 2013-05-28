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
package net.noctuasource.noctua.core.model;

public enum FlashCardElementType {
	
	ILLEGAL_TYPE(0),
	CONTENT(1),
	EXPLANATION(2),
	ADDITIONAL_INFO_CONTENT(3),
	ADDITIONAL_INFO_EXPLANATION(4),
	SENTENCE_CONTENT(5),
	SENTENCE_EXPLANATION(6),
	TIP_CONTENT(7),
	TIP_EXPLANATION(8);
	
	
	private int value;
	
	private FlashCardElementType(int value) {
		this.value = value;
	}
	
	
	public int getValue() {
		return value;
	}
	
}
