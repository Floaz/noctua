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

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.AbstractTest;
import net.noctuasource.noctua.core.test.ResultTestState;
import net.noctuasource.noctua.core.test.SettingsTestState;
import net.noctuasource.noctua.core.test.TestState;





public class NormalTest extends AbstractTest {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(NormalTest.class);



	// ***** Static Members ************************************************* //



	// ***** Members ******************************************************** //



	// ***** Methods ******************************************************** //


	@Override
	protected TestState getNextState() {
		TestState currentState = getState();

		if(currentState == null) {
			return new SettingsTestState();
		}
		else if(currentState instanceof SettingsTestState) {
			return new NormalStartTestState();
		}
		else if(currentState instanceof NormalStartTestState) {
			return new NormalQuestionTestState();
		}
		else if(currentState instanceof NormalQuestionTestState) {
			return new NormalCheckTestState();
		}
		else if(currentState instanceof NormalCheckTestState) {
			if(again()) {
				return new NormalQuestionTestState();
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
