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
import java.util.Map;

import net.noctuasource.noctua.core.model.Gender;






public class GenderMap {

	// ***** Members ******************************************************** //

	BiMap<String, Gender>	genderMap = HashBiMap.create();


	// ***** Constructor **************************************************** //

	public GenderMap() {
		genderMap.put("Keine Angabe", Gender.NO_GENDER);
		genderMap.put("Neutral", Gender.NEUTRAL);
		genderMap.put("Maskulin", Gender.MASCULINE);
		genderMap.put("Feminin", Gender.FEMININE);
	}


	public List<String> getGenderStrings() {
		List<String> list = new LinkedList<>();

		for(Gender gender : Gender.values()) {
			list.add(getStringByGender(gender));
		}

		return list;
	}


	public Gender getGenderByString(String genderString) {
		return genderMap.get(genderString);
	}

	public String getStringByGender(Gender gender) {
		return genderMap.inverse().get(gender);
	}

}
