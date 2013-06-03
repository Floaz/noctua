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

import net.noctuasource.act.data.ControllerData;








public interface TestData extends ControllerData {


	// ***** Static Members ************************************************* //

	public static final String	TEST_OBJECT = "test";
	public static final String	TEST_VIEW = "testView";
	public static final String	TEST_SETTINGS = "testSettings";
	public static final String	FLASH_CARD_FETCHER = "flashCardFetcher";
	public static final String	QUESTION_CONTEXT = "questionContext";
	public static final String	CHECKUP_CONTEXT = "checkupContext";
	public static final String	ANSWER_CHECKER = "answerChecker";
	public static final String	COUNTDOWN_OBJECT = "countdown";
	public static final String	TEST_HISTORY = "testHistory";
	public static final String	GROUP_LIST = "groupList";
	public static final String	MULTIPLE_CHOICE_ANSWER_SET = "multipleChoiceAnswerSet";
	public static final String	START_TIME = "startTime";
	public static final String	ELAPSED_TIME_COUNTER = "elapsedTimeCounter";



	// ***** Methods ******************************************************** //

	@Deprecated
	public Object put(String name, Object object);


}
