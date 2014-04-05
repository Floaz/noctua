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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;





@Entity
@Table(name="VocableMetaInfo")
public class VocableMetaInfo extends FlashCardElement {

    @Column(name = "Gender", nullable=false)
	private Gender			gender = Gender.NO_GENDER;

    @Column(name = "WordType", nullable=false)
	private PartOfSpeech	partOfSpeech = PartOfSpeech.NO_PART_OF_SPEECH;





    // ********************** Accessor Methods ****************************** //

	public VocableMetaInfo() {
		setType(FlashCardElementType.VOCABLE_META_INFO);
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public PartOfSpeech getPartOfSpeech() {
		return partOfSpeech;
	}


	public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}

}
