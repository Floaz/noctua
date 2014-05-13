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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="flash_card_groups")
@DiscriminatorValue(TreeNodeTypes.FLASH_CARD_GROUP_TYPE)
public class FlashCardGroup extends TreeNode {

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private Set<Vocable> vocabulary = new HashSet<>();

	@Column(name = "max_levels")
	private int maxFlashCardGroups;



	public int getMaxFlashCardGroups() {
		return maxFlashCardGroups;
	}


	public void setMaxFlashCardGroups(int maxFlashCardGroups) {
		this.maxFlashCardGroups = maxFlashCardGroups;
	}


	public Set<Vocable> getVocabulary() {
		return vocabulary;
	}


	public void setVocabulary(Set<Vocable> vocabulary) {
		this.vocabulary = vocabulary;
	}







    // ********************** Common Methods ******************************** //

	public void addVocable(Vocable vocable) {
		vocable.setGroup(this);
		vocabulary.add(vocable);
	}


	public void removeVocable(Vocable vocable) {
		vocable.setGroup(null);
		vocabulary.remove(vocable);
	}

}
