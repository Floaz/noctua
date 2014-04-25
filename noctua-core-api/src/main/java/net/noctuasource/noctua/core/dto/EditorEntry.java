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
package net.noctuasource.noctua.core.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;





public class EditorEntry {

	private String			id;

	private StringProperty	vocable = new SimpleStringProperty();
	private StringProperty	native1 = new SimpleStringProperty();
	private StringProperty	native2 = new SimpleStringProperty();
	private StringProperty	native3 = new SimpleStringProperty();
	private StringProperty	example = new SimpleStringProperty();
	private StringProperty	exampleTranslation = new SimpleStringProperty();
	private StringProperty	tip = new SimpleStringProperty();
	private StringProperty	info = new SimpleStringProperty();
	private IntegerProperty	gender = new SimpleIntegerProperty(0);
	private IntegerProperty	partOfSpeech = new SimpleIntegerProperty(0);


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getVocable() {
		return vocable.get();
	}

	public StringProperty vocableProperty() {
		return vocable;
	}


	public void setVocable(String vocable) {
		this.vocable.set(vocable);
	}


	public String getNative1() {
		return native1.get();
	}

	public StringProperty native1Property() {
		return native1;
	}


	public void setNative1(String native1) {
		this.native1.set(native1);
	}


	public String getNative2() {
		return native2.get();
	}

	public StringProperty native2Property() {
		return native2;
	}


	public void setNative2(String native2) {
		this.native2.set(native2);
	}


	public String getNative3() {
		return native3.get();
	}

	public StringProperty native3Property() {
		return native3;
	}


	public void setNative3(String native3) {
		this.native3.set(native3);
	}


	public String getExample() {
		return example.get();
	}

	public StringProperty exampleProperty() {
		return example;
	}


	public void setExample(String example) {
		this.example.set(example);
	}


	public String getExampleTranslation() {
		return exampleTranslation.get();
	}

	public StringProperty exampleTranslationProperty() {
		return exampleTranslation;
	}


	public void setExampleTranslation(String exampleTranslation) {
		this.exampleTranslation.set(exampleTranslation);
	}


	public String getTip() {
		return tip.get();
	}

	public StringProperty tipProperty() {
		return tip;
	}


	public void setTip(String tip) {
		this.tip.set(tip);
	}


	public String getInfo() {
		return info.get();
	}

	public StringProperty infoProperty() {
		return info;
	}


	public void setInfo(String info) {
		this.info.set(info);
	}


	public int getGender() {
		return gender.get();
	}


	public IntegerProperty genderProperty() {
		return gender;
	}


	public void setGender(int gender) {
		this.gender.set(gender);
	}


	public int getPartOfSpeech() {
		return partOfSpeech.get();
	}


	public IntegerProperty partOfSpeechProperty() {
		return partOfSpeech;
	}


	public void setPartOfSpeech(int partOfSpeech) {
		this.partOfSpeech.set(partOfSpeech);
	}



}
