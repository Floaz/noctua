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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.AnswerChecker;
import net.noctuasource.noctua.core.test.AnswerChecker.CheckResult;
import net.noctuasource.noctua.core.test.CheckupContext;
import net.noctuasource.noctua.core.test.QuestionContext;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.TestHistory;
import net.noctuasource.noctua.core.test.TestHistoryElement;
import net.noctuasource.noctua.core.test.TestState;
import net.noctuasource.noctua.core.test.checker.AnswerCheckerImpl;

public class SchoolCheckTestState implements TestState {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger
							.getLogger(SchoolCheckTestState.class);

	

	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	// ***** Constructor **************************************************** //

	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {
		SchoolTestView view = (SchoolTestView) data.get(TestData.TEST_VIEW);

		List<String>			answers = view.getAnswers();

		QuestionContainer	questions = (QuestionContainer)
									data.get(SchoolTestView.QUESTIONS_TEST_DATA);
		
		CheckupContainer	checkups = new CheckupContainer();

		
		for(int i = 0; i < questions.size(); ++i) {
			AnswerChecker checker = new AnswerCheckerImpl(null);
			boolean correct = checker.check(answers.get(i), questions.get(i))
														== CheckResult.CORRECT;

			CheckupContext cc = new CheckupContext();
			cc.setAnswerCorrect(correct);
			checkups.add(cc);
		}
		
		data.put(SchoolTestView.CHECKUPS_TEST_DATA, checkups);
		
		view.showCheckup();
	}



	@Override
	public void destroy(TestData data) {
		
		QuestionContainer qc = (QuestionContainer) data.get(SchoolTestView.QUESTIONS_TEST_DATA);
		CheckupContainer cc = (CheckupContainer) data.get(SchoolTestView.CHECKUPS_TEST_DATA);

		SchoolTestView view = (SchoolTestView) data.get(TestData.TEST_VIEW);
		List<Boolean>			checkboxes = view.getResultCorrection();
		List<String>			answers = view.getAnswers();

		
		TestHistory testHistory = new TestHistory();
		
		for(int i = 0; i < qc.size(); ++i) {
			boolean correct = checkboxes.get(i) || cc.get(i).isAnswerCorrect();

			TestHistoryElement historyElement = new TestHistoryElement();
			historyElement.setFlashCard(qc.get(i).getFlashCard());
			historyElement.setOrder(testHistory.size());
			historyElement.setQuestion(qc.get(i).getQuestion());
			historyElement.setAnswer(answers.get(i));
			historyElement.setCorrect(correct);
			testHistory.add(historyElement);
		}
				
		data.put(TestData.TEST_HISTORY, testHistory);
	}
	
}
