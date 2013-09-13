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

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;





@Entity
@Table(name="FlashCards")
@Inheritance(strategy = InheritanceType.JOINED)
@GenericGenerator(name="FLASH_CARD_GEN", strategy="uuid2", parameters={})
public class FlashCard {

	public static final Long FIRST_LEVEL = 1L;




    @Id
    @GeneratedValue(generator="FLASH_CARD_GEN")
    @Column(name="FlashCardId")
	private String id;

    @ManyToOne
    @JoinColumn(name = "TreeNodeId", nullable = false)
	private FlashCardGroup group;

    @Column(name = "FlashCardLevel")
	private Long level;

    @OneToMany(mappedBy = "flashCard", cascade = CascadeType.ALL)
	private List<FlashCardElement> elements = new LinkedList<>();





    // ********************** Accessor Methods ****************************** //

	public FlashCard() {
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}


	public FlashCardGroup getGroup() {
		return group;
	}


	public void setGroup(FlashCardGroup group) {
		this.group = group;
	}


	public List<FlashCardElement> getElements() {
		return elements;
	}


	public void setElements(List<FlashCardElement> elements) {
		this.elements = elements;
	}




    // ********************** Common Methods ******************************** //


	public void addElement(FlashCardElement element) {
		element.setFlashCard(this);
		elements.add(element);
	}


	public void removeElement(FlashCardElement element) {
		element.setFlashCard(null);
		elements.remove(element);
	}




	public List<FlashCardElement> getElementsOfType(FlashCardElementType type) {
		List<FlashCardElement> e = new LinkedList<FlashCardElement>();

		for(FlashCardElement element : elements) {
			if(element.getType() == type) {
				e.add(element);
			}
		}

		return e;
	}


	public String getElementsString(FlashCardElementType type) {
		return getElementsString(type, "; ");
	}


	public String getElementsString(FlashCardElementType type, String glue) {
		List<String> parts = new LinkedList<String>();

		for(FlashCardElement element : elements) {
			if(element.getType() == type) {
				parts.add(element.getValue());
			}
		}

		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(String part : parts) {
			String partString = first ? part : glue + part;
			builder.append(partString);
			first = false;
		}

		return builder.toString();
	}


	@Override
	public String toString() {
		return getElementsString(FlashCardElementType.CONTENT);

	}

}
