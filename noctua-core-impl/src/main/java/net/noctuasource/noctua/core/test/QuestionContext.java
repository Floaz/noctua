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
package net.noctuasource.noctua.core.test;

import net.noctuasource.noctua.core.model.FlashCard;








public class QuestionContext {

	// ***** Members ******************************************************** //

	private FlashCard			flashCard;
	
	private QuestionDirection	direction;
	
	private String				question;

	
	
	// ***** Methods ******************************************************** //

	public FlashCard getFlashCard() {
		return flashCard;
	}

	public void setFlashCard(FlashCard flashCard) {
		this.flashCard = flashCard;
	}

	public QuestionDirection getDirection() {
		return direction;
	}

	public void setDirection(QuestionDirection direction) {
		this.direction = direction;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	
	
	
	
}
