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
package net.noctuasource.noctua.core.ui.vocable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.LinkedList;
import java.util.List;
import net.noctuasource.noctua.core.model.Gender;
import net.noctuasource.noctua.core.model.PartOfSpeech;






public class PartOfSpeechMap {

	// ***** Members ******************************************************** //

	BiMap<String, PartOfSpeech>	partOfSpeechMap = HashBiMap.create();



	// ***** Constructor **************************************************** //

	public PartOfSpeechMap() {
		partOfSpeechMap.put("Keine Angabe", PartOfSpeech.NO_PART_OF_SPEECH);
		partOfSpeechMap.put("Substantiv", PartOfSpeech.SUBSTANTIVE);
		partOfSpeechMap.put("Verb", PartOfSpeech.VERB);
		partOfSpeechMap.put("Adjektiv", PartOfSpeech.ADJECTIVE);
		partOfSpeechMap.put("Adverb", PartOfSpeech.ADVERB);
		partOfSpeechMap.put("Pronomen", PartOfSpeech.PRONOUN);
		partOfSpeechMap.put("Präposition", PartOfSpeech.PREPOSITION);
		partOfSpeechMap.put("Konjunktion", PartOfSpeech.CONJUNCTION);
		partOfSpeechMap.put("Ausdruck", PartOfSpeech.EXPRESSION);
		partOfSpeechMap.put("Andere Wortart", PartOfSpeech.OTHER_SPEECH_PART);
	}


	public List<String> getPartOfSpeechStrings() {
		List<String> list = new LinkedList<>();

		for(PartOfSpeech partOfSpeech : PartOfSpeech.values()) {
			list.add(getStringByPartOfSpeech(partOfSpeech));
		}

		return list;
	}


	public PartOfSpeech getPartOfSpeechByString(String partOfSpeechString) {
		return partOfSpeechMap.get(partOfSpeechString);
	}


	public String getStringByPartOfSpeech(PartOfSpeech partOfSpeech) {
		return partOfSpeechMap.inverse().get(partOfSpeech);
	}

}
