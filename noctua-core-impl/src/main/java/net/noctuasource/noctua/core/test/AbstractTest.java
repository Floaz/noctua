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
package net.noctuasource.noctua.core.test;

import net.noctuasource.act.controller.SubContextController;
import org.apache.log4j.Logger;










public abstract class AbstractTest extends SubContextController implements Test {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(AbstractTest.class);



	// ***** Members ******************************************************** //

	private TestData	testData = new TestDataImpl();

	private TestState	currentState;






	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		getTestData().put(TestData.TEST_OBJECT, this);
		getTestData().put(TestData.GROUP_LIST, getControllerParams().get("groupList", GroupList.class));

		next();
	}


	// ***** Methods ******************************************************** //

	protected abstract TestState getNextState();


	@Override
	public void next() {
		logger.debug("Process next state.");

		TestState oldStateState = currentState;

		currentState = getNextState();

		if(oldStateState != null) {
			oldStateState.destroy(getTestData());
		}

		if(currentState != null) {
			currentState.initialize(getTestData());
		} else {
			cancel();
		}
	}


	public boolean again() {
		FlashCardFetcher fetcher = (FlashCardFetcher)
								getTestData().get(TestData.FLASH_CARD_FETCHER);

		return fetcher.hasNextFlashCard();
	}


	@Override
	public void onDestroy() {
		if(currentState != null) {
			currentState.destroy(getTestData());
			currentState = null;
		}

		getTestData().remove(TestData.TEST_OBJECT);
		getTestData().remove(TestData.TEST_SETTINGS);

		TestView view = (TestView) getTestData().get(TestData.TEST_VIEW);
		if(view != null) {
			view.dispose();
			getTestData().remove(TestData.TEST_VIEW);
		}
	}


	@Override
	public void cancel() {
		logger.debug("Canceling test...");
		destroy();
	}


	protected TestState getState() {
		return currentState;
	}


	@Override
	public TestData getTestData() {
		return testData;
	}

}
