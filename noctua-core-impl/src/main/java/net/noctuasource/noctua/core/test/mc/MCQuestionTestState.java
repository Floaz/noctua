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

import java.util.Random;

import net.noctuasource.noctua.core.model.FlashCardElementType;
import net.noctuasource.noctua.core.test.impl.AbstractQuestionTestState;
import net.noctuasource.noctua.core.test.impl.FlashCardFetcher;
import net.noctuasource.noctua.core.test.impl.QuestionContext;
import net.noctuasource.noctua.core.test.impl.QuestionDirection;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.impl.TestSettings;
import net.noctuasource.noctua.core.test.impl.TestView;

public class MCQuestionTestState extends AbstractQuestionTestState {

	// ***** Basic Static Members ******************************************* //

	//private static Logger logger = Logger
	//		.getLogger(MCQuestionTestState.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	// ***** Constructor **************************************************** //

	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {
		TestSettings settings = (TestSettings) data.get(TestData.TEST_SETTINGS);
		QuestionContext qc = (QuestionContext)
											data.get(TestData.QUESTION_CONTEXT);

		QuestionDirection direction;
		
		if(settings.getDirection() != QuestionDirection.NOT_DEFINED) {
			direction = settings.getDirection();
		} else {
			Random rand = new Random();
			direction = rand.nextBoolean()
									? QuestionDirection.CONTENT_TO_EXPLANATION
									: QuestionDirection.EXPLANATION_TO_CONTENT;
		}
		
		FlashCardFetcher flashCardFetcher = (FlashCardFetcher) data.get(TestData.FLASH_CARD_FETCHER);
		qc.setFlashCard(flashCardFetcher.getNextFlashCard());
		
		FlashCardElementType type =
				direction == QuestionDirection.CONTENT_TO_EXPLANATION
					? FlashCardElementType.CONTENT
					: FlashCardElementType.EXPLANATION;

		qc.setQuestion(qc.getFlashCard().getElementsString(type));
		qc.setDirection(direction);

		MCAnswerContainer answerSet = (MCAnswerContainer)
								data.get(TestData.MULTIPLE_CHOICE_ANSWER_SET);
		answerSet.loadContainer(MCTest.NUMBER_OF_ANSWERS);
		
		super.initialize(data);
		
		TestView view = (TestView) data.get(TestData.TEST_VIEW);
		view.showQuestion();
	}

	
}
