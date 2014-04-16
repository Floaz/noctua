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
package net.noctuasource.noctua.core.test.normal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javafx.application.Platform;
import net.noctuasource.act.data.ControllerParamsBuilder;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.AnswerChecker;
import net.noctuasource.noctua.core.test.impl.FlashCardFetcher;
import net.noctuasource.noctua.core.test.impl.QuestionContext;
import net.noctuasource.noctua.core.test.impl.StartTestState;
import net.noctuasource.noctua.core.test.impl.Test;
import net.noctuasource.noctua.core.test.impl.TestCountdown;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.impl.TestHistory;
import net.noctuasource.noctua.core.test.impl.TestSettings;
import net.noctuasource.noctua.core.test.impl.TestView;
import net.noctuasource.noctua.core.test.checker.AnswerCheckerImpl;
import net.noctuasource.noctua.core.test.fetcher.RandomFlashCardFetcher;
import net.noctuasource.noctua.core.ui.test.NormalTestView;

public class NormalStartTestState extends StartTestState {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger
			.getLogger(NormalStartTestState.class);


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
					final Test test = (Test) data.get(TestData.TEST_OBJECT);
					if(test != null) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								test.next();
							}
						});
					}
				}
			});

			data.put(TestData.COUNTDOWN_OBJECT, countdown);
		}

		FlashCardFetcher fetcher = new RandomFlashCardFetcher(data);

		AnswerChecker checker = new AnswerCheckerImpl(data);

		QuestionContext questionContext = new QuestionContext();

		TestHistory testHistory = new TestHistory();

		data.put(TestData.FLASH_CARD_FETCHER, fetcher);
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
		NormalTest test = (NormalTest) data.get(TestData.TEST_OBJECT);
		TestView view = (TestView) test.createController(NormalTestView.class, ControllerParamsBuilder.create()
																					.add("testData", data).build());
		data.put(TestData.TEST_VIEW, view);
	}


	@Override
	public void destroy(TestData data) {
	}

}
