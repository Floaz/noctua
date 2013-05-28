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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;





@Entity
@Table(name="flashcardelements")
@GenericGenerator(name="FLASH_CARD_ELEMENT_GEN", strategy="native", parameters={})
public class FlashCardElement {

    @Id
    @GeneratedValue(generator="FLASH_CARD_ELEMENT_GEN")
    @Column(name="FlashCardElementId")
	private Long 					id;
	
    @ManyToOne
    @JoinColumn(name = "FlashCardId", nullable = false)
	private FlashCard 				flashCard;

    @Column(name = "Type")
	private FlashCardElementType 	type;
    
    @Column(name = "Value")
	private String 					value;
	
	
	
	
	public FlashCardElement() {
	}
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}


	public FlashCard getFlashCard() {
		return flashCard;
	}


	public void setFlashCard(FlashCard flashCard) {
		this.flashCard = flashCard;
	}


	public FlashCardElementType getType() {
		return type;
	}


	public void setType(FlashCardElementType type) {
		this.type = type;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}

}
