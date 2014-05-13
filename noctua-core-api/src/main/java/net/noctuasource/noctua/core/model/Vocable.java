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
@Table(name="vocabulary")
@GenericGenerator(name="VOCABLE_GEN", strategy="uuid2", parameters={})
public class Vocable {

    @Id
    @GeneratedValue(generator="VOCABLE_GEN")
    @Column(name="vocable_id")
	private String			id;

    @ManyToOne
    @JoinColumn(name = "tree_node_id", nullable = false)
	private FlashCardGroup	group;

    @Column(name = "level", nullable = false)
	private Integer			level = 1;

    @Column(name = "vocable")
	private String			vocable;

    @Column(name = "native1")
	private String			native1;

    @Column(name = "native2")
	private String			native2;

    @Column(name = "native3")
	private String			native3;

    @Column(name = "example")
	private String			example;

    @Column(name = "example_translation")
	private String			exampleTranslation;

    @Column(name = "tip")
	private String			tip ;

    @Column(name = "info")
	private String			info;

    @Column(name = "gender", nullable=false)
	private Gender			gender = Gender.NO_GENDER;

    @Column(name = "word_type", nullable=false)
	private PartOfSpeech	partOfSpeech = PartOfSpeech.NO_PART_OF_SPEECH;





    // ********************** Accessor Methods ****************************** //

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


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public FlashCardGroup getGroup() {
		return group;
	}


	public void setGroup(FlashCardGroup group) {
		this.group = group;
	}


	public Integer getLevel() {
		return level;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}


	public String getVocable() {
		return vocable;
	}


	public void setVocable(String vocable) {
		this.vocable = vocable;
	}


	public String getNative1() {
		return native1;
	}


	public void setNative1(String native1) {
		this.native1 = native1;
	}


	public String getNative2() {
		return native2;
	}


	public void setNative2(String native2) {
		this.native2 = native2;
	}


	public String getNative3() {
		return native3;
	}


	public void setNative3(String native3) {
		this.native3 = native3;
	}


	public String getExample() {
		return example;
	}


	public void setExample(String example) {
		this.example = example;
	}


	public String getExampleTranslation() {
		return exampleTranslation;
	}


	public void setExampleTranslation(String exampleTranslation) {
		this.exampleTranslation = exampleTranslation;
	}


	public String getTip() {
		return tip;
	}


	public void setTip(String tip) {
		this.tip = tip;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}

}
