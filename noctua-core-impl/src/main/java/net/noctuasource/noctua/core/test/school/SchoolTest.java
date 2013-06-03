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

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.AbstractTest;
import net.noctuasource.noctua.core.test.impl.ResultTestState;
import net.noctuasource.noctua.core.test.impl.SettingsTestState;
import net.noctuasource.noctua.core.test.impl.TestState;





public class SchoolTest extends AbstractTest {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(SchoolTest.class);


	// ***** Methods ******************************************************** //

	@Override
	protected TestState getNextState() {
		TestState currentState = getState();

		if(currentState == null) {
			return new SettingsTestState();
		}
		else if(currentState instanceof SettingsTestState) {
			return new SchoolStartTestState();
		}
		else if(currentState instanceof SchoolStartTestState) {
			return new SchoolQuestionTestState();
		}
		else if(currentState instanceof SchoolQuestionTestState) {
			return new SchoolCheckTestState();
		}
		else if(currentState instanceof SchoolCheckTestState) {
			if(again()) {
				return new SchoolQuestionTestState();
			} else {
				//cancel();
				return new ResultTestState();
			}
		}
		else if(currentState instanceof ResultTestState) {
			return null;
		}
		else {
			logger.warn("Illegal last state.");
			return null;
		}
	}
}
