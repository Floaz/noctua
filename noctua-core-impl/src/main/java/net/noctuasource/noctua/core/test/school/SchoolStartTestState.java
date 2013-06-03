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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javafx.application.Platform;
import net.noctuasource.act.data.ControllerParamsBuilder;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.FlashCardFetcher;
import net.noctuasource.noctua.core.test.impl.StartTestState;
import net.noctuasource.noctua.core.test.impl.Test;
import net.noctuasource.noctua.core.test.impl.TestCountdown;
import net.noctuasource.noctua.core.test.impl.TestData;
import net.noctuasource.noctua.core.test.impl.TestSettings;
import net.noctuasource.noctua.core.test.impl.TestView;
import net.noctuasource.noctua.core.test.fetcher.RandomFlashCardFetcher;
import net.noctuasource.noctua.core.ui.test.SchoolTestViewImpl;








public class SchoolStartTestState extends StartTestState {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger
			.getLogger(SchoolStartTestState.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	// ***** Constructor **************************************************** //

	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {
		TestSettings settings = (TestSettings) data.get(TestData.TEST_SETTINGS);
		FlashCardFetcher fetcher = new RandomFlashCardFetcher(data);

		data.put(TestData.FLASH_CARD_FETCHER, fetcher);
		data.put(TestData.START_TIME, new Date());
		data.put(TestData.ELAPSED_TIME_COUNTER, new Long(0));


		if(settings.isTimeLimit()) {
			int seconds = fetcher.getRemainingCount() * settings.getTimeLimitSeconds();

			TestCountdown countdown = new TestCountdown(seconds);
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


		buildView(data);

		Test test = (Test) data.get(TestData.TEST_OBJECT);
		test.next();
	}


	@Override
	public void buildView(TestData data) {
		SchoolTest test = (SchoolTest) data.get(TestData.TEST_OBJECT);
		TestView view = test.executeController(SchoolTestViewImpl.class, ControllerParamsBuilder.create()
																				.add("testData", data).build());
		data.put(TestData.TEST_VIEW, view);
	}


	@Override
	public void destroy(TestData data) {
	}

}
