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

import net.noctuasource.noctua.core.test.AnswerChecker;
import net.noctuasource.noctua.core.test.AnswerChecker.CheckResult;
import net.noctuasource.noctua.core.test.CheckupContext;
import net.noctuasource.noctua.core.test.QuestionContext;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.TestHistory;
import net.noctuasource.noctua.core.test.TestHistoryElement;
import net.noctuasource.noctua.core.test.TestState;
import net.noctuasource.noctua.core.ui.test.MultipleChoiceTestView;

public class MCCheckTestState implements TestState {

	// ***** Basic Static Members ******************************************* //

	//private static Logger logger = Logger
	//						.getLogger(MCCheckTestState.class);

	

	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	// ***** Constructor **************************************************** //

	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {

		AnswerChecker checker = (AnswerChecker) data.get(TestData.ANSWER_CHECKER);
		MCAnswerContainer container = (MCAnswerContainer)
								data.get(TestData.MULTIPLE_CHOICE_ANSWER_SET);
		
		MultipleChoiceTestView view = (MultipleChoiceTestView) data.get(TestData.TEST_VIEW);

		int index = view.getAnswerIndex();
		
		String answer = index != MultipleChoiceAnswerView.ILLEGAL_INDEX
											? container.getAnswer(index) : "";
		
		boolean correct = checker.check(answer) == CheckResult.CORRECT;
		
		QuestionContext qc = (QuestionContext) data.get(TestData.QUESTION_CONTEXT);

		TestHistory history = (TestHistory) data.get(TestData.TEST_HISTORY);

		TestHistoryElement historyElement = new TestHistoryElement();
		historyElement.setFlashCard(qc.getFlashCard());
		historyElement.setOrder(history.size());
		historyElement.setQuestion(qc.getQuestion());
		historyElement.setAnswer(answer);
		historyElement.setCorrect(correct);

		history.add(historyElement);

		CheckupContext cc = new CheckupContext();
		cc.setAnswerCorrect(correct);
		data.put(TestData.CHECKUP_CONTEXT, cc);
		
		
		view.showCheckup();
	}



	@Override
	public void destroy(TestData data) {
	}
	
}
