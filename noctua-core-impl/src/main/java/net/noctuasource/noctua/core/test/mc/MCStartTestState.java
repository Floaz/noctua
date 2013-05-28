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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import net.noctuasource.act.data.ControllerParamsBuilder;

import net.noctuasource.noctua.core.test.AnswerChecker;
import net.noctuasource.noctua.core.test.FlashCardFetcher;
import net.noctuasource.noctua.core.test.QuestionContext;
import net.noctuasource.noctua.core.test.StartTestState;
import net.noctuasource.noctua.core.test.Test;
import net.noctuasource.noctua.core.test.TestCountdown;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.TestHistory;
import net.noctuasource.noctua.core.test.TestSettings;
import net.noctuasource.noctua.core.test.TestView;
import net.noctuasource.noctua.core.test.checker.AnswerCheckerImpl;
import net.noctuasource.noctua.core.test.fetcher.RandomFlashCardFetcher;
import net.noctuasource.noctua.core.ui.test.MultipleChoiceTestView;

public class MCStartTestState extends StartTestState {

	// ***** Basic Static Members ******************************************* //

	//private static Logger logger = Logger
	//		.getLogger(MCStartTestState.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	// ***** Constructor **************************************************** //

	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {
		TestSettings settings = (TestSettings) data.get(TestData.TEST_SETTINGS);

		if(settings.isTimeLimit()) {
			TestCountdown countdown = new TestCountdown(data);
			countdown.addListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Test test = (Test) data.get(TestData.TEST_OBJECT);
					if(test != null) {
						test.next();
					}
				}
			});

			data.put(TestData.COUNTDOWN_OBJECT, countdown);
		}

		FlashCardFetcher fetcher = new RandomFlashCardFetcher(data);

		MCAnswerContainer container = new MCAnswerContainer(data);

		AnswerChecker checker = new AnswerCheckerImpl(data);

		QuestionContext questionContext = new QuestionContext();

		TestHistory testHistory = new TestHistory();

		data.put(TestData.FLASH_CARD_FETCHER, fetcher);
		data.put(TestData.MULTIPLE_CHOICE_ANSWER_SET, container);
		data.put(TestData.ANSWER_CHECKER, checker);
		data.put(TestData.QUESTION_CONTEXT, questionContext);
		data.put(TestData.TEST_HISTORY, testHistory);
		data.put(TestData.START_TIME, new Date());
		data.put(TestData.ELAPSED_TIME_COUNTER, new Long(0));


		buildView(data);

		Test test = (Test) data.get(TestData.TEST_OBJECT);
		test.next();
	}


	@Override
	public void buildView(TestData data) {
		MCTest test = (MCTest) data.get(TestData.TEST_OBJECT);
		TestView view = test.executeController(MultipleChoiceTestView.class, ControllerParamsBuilder.create()
																					.add("testData", data).build());
		data.put(TestData.TEST_VIEW, view);
	}


	@Override
	public void destroy(TestData data) {
	}

}
