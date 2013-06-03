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
package net.noctuasource.noctua.core.test.impl;

import net.noctuasource.noctua.core.test.TestData;
import java.util.Date;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.act.util.AfterDestroyRunnable;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.ui.test.ResultView;




public class ResultTestState implements TestState {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger
			.getLogger(ResultTestState.class);


	// ***** Static Members ************************************************* //


	// ***** Members ******************************************************** //


	// ***** Constructor **************************************************** //


	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {
		TestView view = (TestView) data.get(TestData.TEST_VIEW);
		view.dispose();

		TestHistory history = (TestHistory) data.get(TestData.TEST_HISTORY);
		int correctCount = history.getCorrectCount();
		int count = history.size();

		logger.debug("Test result: correct=" + correctCount
						+ " wrong=" + (count -correctCount)
						+ " sum=" + count);

		Date startTime = (Date) data.get(TestData.START_TIME);
		Long timeElapsed = (Long) data.get(TestData.ELAPSED_TIME_COUNTER);
		TestResultData resultData = new TestResultData(history, startTime, timeElapsed);

		final AbstractTest test = (AbstractTest) data.get(TestData.TEST_OBJECT);

		ContextController resultView = test.executeController(
															ResultView.class,
															ControllerParamsBuilder.create().add(resultData).build());
		AfterDestroyRunnable.create(resultView, new Runnable() {
			@Override
			public void run() {
				test.cancel();
			}
		});
	}


	@Override
	public void destroy(TestData data) {
	}

}
