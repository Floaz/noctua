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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;





@Entity
@DiscriminatorValue(value="1")
public class ContentFlashCardElement extends FlashCardElement {

	@OneToMany(mappedBy = "vocable")
	private List<ExampleSentence>	sentences = new ArrayList<>();





    // ********************** Accessor Methods ****************************** //

	public ContentFlashCardElement() {
		setType(FlashCardElementType.CONTENT);
	}


	public List<ExampleSentence> getSentences() {
		return sentences;
	}


	public void setSentences(List<ExampleSentence> sentences) {
		this.sentences = sentences;
	}

}
