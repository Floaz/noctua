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
@Table(name="ExampleSentences")
@GenericGenerator(name="EXAMPLE_SENTENCE_GEN", strategy="native", parameters={})
public class ExampleSentence {


    @Id
    @GeneratedValue(generator="EXAMPLE_SENTENCE_GEN")
    @Column(name="SentenceId")
	private Long			id;

    @ManyToOne
    @JoinColumn(name = "FlashCardElementId", nullable = false)
	private FlashCardElement vocable;

    @Column(name = "SentenceText", nullable=false)
	private String			sentence;

    @Column(name = "SentenceTranslation", nullable=true)
	private String			sentenceTranslation = null;


	// ********************** Accessor Methods ****************************** //

	public ExampleSentence() {
	}


	public ExampleSentence(String sentence, String sentenceTranslation) {
		this.sentence = sentence;
		this.sentenceTranslation = sentenceTranslation;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public FlashCardElement getVocable() {
		return vocable;
	}


	public void setVocable(FlashCardElement vocable) {
		this.vocable = vocable;
	}


	public String getSentence() {
		return sentence;
	}


	public void setSentence(String sentence) {
		this.sentence = sentence;
	}


	public String getSentenceTranslation() {
		return sentenceTranslation;
	}


	public void setSentenceTranslation(String sentenceTranslation) {
		this.sentenceTranslation = sentenceTranslation;
	}

}
