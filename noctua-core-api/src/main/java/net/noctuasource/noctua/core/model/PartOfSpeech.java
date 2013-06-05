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


/**
 * PartOfSpeech.
 * @author Philipp Thomas
 */
public enum PartOfSpeech {

	NO_PART_OF_SPEECH(0),
	SUBSTANTIVE(1),
	VERB(2),
	ADJECTIVE(3),
	ADVERB(4),
	PREPOSITION(5),
	EXPRESSION(6),
	PRONOUN(7),
	CONJUNCTION(8),
	OTHER_SPEECH_PART(9);


	private int value;

	private PartOfSpeech(int value) {
		this.value = value;
	}


	public int getValue() {
		return value;
	}

}
