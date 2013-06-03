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
package net.noctuasource.noctua.core.test.school;

import java.util.Random;

import net.noctuasource.noctua.core.model.FlashCardElementType;
import net.noctuasource.noctua.core.test.impl.AbstractQuestionTestState;
import net.noctuasource.noctua.core.test.impl.FlashCardFetcher;
import net.noctuasource.noctua.core.test.impl.QuestionContext;
import net.noctuasource.noctua.core.test.impl.QuestionDirection;
import net.noctuasource.noctua.core.test.impl.TestData;
import net.noctuasource.noctua.core.test.impl.TestSettings;

public class SchoolQuestionTestState extends AbstractQuestionTestState {

	// ***** Basic Static Members ******************************************* //

	// ***** Static Members ************************************************* //
	
	// ***** Members ******************************************************** //

	// ***** Constructor **************************************************** //

	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {
		TestSettings settings = (TestSettings) data.get(TestData.TEST_SETTINGS);

		FlashCardFetcher flashCardFetcher = (FlashCardFetcher) data.get(TestData.FLASH_CARD_FETCHER);
		
		QuestionContainer questions = new QuestionContainer();
		
		while(flashCardFetcher.hasNextFlashCard()) {
			QuestionContext qc = new QuestionContext();
	
			QuestionDirection direction;
			
			if(settings.getDirection() != QuestionDirection.NOT_DEFINED) {
				direction = settings.getDirection();
			} else {
				Random rand = new Random();
				direction = rand.nextBoolean()
										? QuestionDirection.CONTENT_TO_EXPLANATION
										: QuestionDirection.EXPLANATION_TO_CONTENT;
			}
			
			qc.setFlashCard(flashCardFetcher.getNextFlashCard());
			
			FlashCardElementType type =
					direction == QuestionDirection.CONTENT_TO_EXPLANATION
						? FlashCardElementType.CONTENT
						: FlashCardElementType.EXPLANATION;
	
			qc.setQuestion(qc.getFlashCard().getElementsString(type));
			qc.setDirection(direction);
			
			questions.add(qc);
		}
		
		data.put(SchoolTestView.QUESTIONS_TEST_DATA, questions);
		
		super.initialize(data);
		
		SchoolTestView view = (SchoolTestView) data.get(TestData.TEST_VIEW);
		view.showQuestion();
	}

}
