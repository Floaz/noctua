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
package net.noctuasource.noctua.core.test.mc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.FlashCardElementType;
import net.noctuasource.noctua.core.model.FlashCardGroup;
import net.noctuasource.noctua.core.test.GroupList;
import net.noctuasource.noctua.core.test.impl.QuestionContext;
import net.noctuasource.noctua.core.test.impl.QuestionDirection;
import net.noctuasource.noctua.core.test.impl.TestData;




public class MCAnswerContainer {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(MCAnswerContainer.class);

	
	
	// ***** Static Members ************************************************* //

	
	
	
	// ***** Members ******************************************************** //

	private TestData		testData;
	
	private Set<FlashCard>	flashCards = new HashSet<FlashCard>();
	
	private ArrayList<FlashCard> answers = new ArrayList<FlashCard>();
	
	
	
	
	// ***** Constructor **************************************************** //

	public MCAnswerContainer(TestData testData) {
		this.testData = testData;
		
		GroupList groups = (GroupList) testData.get(TestData.GROUP_LIST);
		
		for(FlashCardGroup group : groups) {
			flashCards.addAll(group.getFlashCards());
		}
	}
	
	
	
	// ***** Methods ******************************************************** //

	public void loadContainer(int answerCount) {
		logger.debug("Load new multiple choice answer set.");
		
		if(answerCount > flashCards.size()) {
			throw new IllegalArgumentException("answerCount > flashCards.size()");
		}
		
		QuestionContext qc = (QuestionContext) testData.get(TestData.QUESTION_CONTEXT);
		
		answers.clear();
		
		answers.add(qc.getFlashCard());
		
		Random rand = new Random();
		int i = 1;
		
		while(i < answerCount) {
			int nextId = rand.nextInt(flashCards.size());
			FlashCard flashCard = (FlashCard) flashCards.toArray()[nextId];
			
			if(!answers.contains(flashCard)) {
				answers.add(flashCard);
				++i;
			}
		}
		
		Collections.shuffle(answers);
	}

	
	public FlashCard getAnswerFlashCard(int index) {
		if(index < 0 || index > answers.size()) {
			throw new IllegalArgumentException("Illegal index.");
		}
		
		return answers.get(index);
	}

	
	public String getAnswer(int index) {
		if(index < 0 || index > answers.size()) {
			throw new IllegalArgumentException("Illegal index.");
		}

		QuestionContext qc = (QuestionContext) testData.get(TestData.QUESTION_CONTEXT);
		FlashCardElementType type =
				qc.getDirection() == QuestionDirection.CONTENT_TO_EXPLANATION
				? FlashCardElementType.EXPLANATION
				: FlashCardElementType.CONTENT;
		
		return answers.get(index).getElementsString(type);
	}

	
	public int getAnswerCount() {
		return answers.size();
	}

}
