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
package net.noctuasource.noctua.core.test.checker;

import java.util.List;

import net.noctuasource.noctua.core.model.FlashCardElement;
import net.noctuasource.noctua.core.model.FlashCardElementType;
import net.noctuasource.noctua.core.test.impl.AnswerChecker;
import net.noctuasource.noctua.core.test.impl.QuestionContext;
import net.noctuasource.noctua.core.test.impl.QuestionDirection;
import net.noctuasource.noctua.core.test.TestData;




public class AnswerCheckerImpl implements AnswerChecker {

	// ***** Basic Static Members ******************************************* //

	//private static Logger logger = Logger.getLogger(AnswerCheckerImpl.class);

	
	
	// ***** Static Members ************************************************* //

	
	
	
	// ***** Members ******************************************************** //

	private TestData		testData;
	
	
	
	
	
	// ***** Constructor **************************************************** //

	public AnswerCheckerImpl(TestData testData) {
		this.testData = testData;
	}
	
	
	
	// ***** Methods ******************************************************** //

	@Override
	public CheckResult check(String answer) {
		QuestionContext qc = (QuestionContext) testData.get(TestData.QUESTION_CONTEXT);
		
		return check(answer, qc);
	}

	
	@Override
	public CheckResult check(String answer, QuestionContext qc) {
		answer = answer.trim();
		
		if(answer.isEmpty()) {
			return CheckResult.WRONG;
		}
				
		FlashCardElementType type =
				qc.getDirection() == QuestionDirection.CONTENT_TO_EXPLANATION
				? FlashCardElementType.EXPLANATION
				: FlashCardElementType.CONTENT;
		
		List<FlashCardElement> elements = qc.getFlashCard().getElementsOfType(type);
		
		for(FlashCardElement element : elements) {
			if(element.getValue().trim().equals(answer)) {
				return CheckResult.CORRECT;
			}
		}
		
		if(qc.getFlashCard().getElementsString(type).trim().equals(answer)) {
			return CheckResult.CORRECT;
		}
		
		return CheckResult.WRONG;
	}

	
}
