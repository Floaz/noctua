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

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.AbstractTest;
import net.noctuasource.noctua.core.test.impl.ResultTestState;
import net.noctuasource.noctua.core.test.impl.SettingsTestState;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.impl.TestState;





public class MCTest extends AbstractTest {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(MCTest.class);



	// ***** Static Members ************************************************* //

	public static int	NUMBER_OF_ANSWERS = 3;


	// ***** Methods ******************************************************** //

	@Override
	public void cancel() {
		super.cancel();
		getTestData().remove(TestData.MULTIPLE_CHOICE_ANSWER_SET);
	}


	@Override
	protected TestState getNextState() {
		if(getState() == null) {
			return new SettingsTestState();
		}
		else if(getState() instanceof SettingsTestState) {
			return new MCStartTestState();
		}
		else if(getState() instanceof MCStartTestState) {
			return new MCQuestionTestState();
		}
		else if(getState() instanceof MCQuestionTestState) {
			return new MCCheckTestState();
		}
		else if(getState() instanceof MCCheckTestState) {
			if(again()) {
				return new MCQuestionTestState();
			} else {
				return new ResultTestState();
			}
		}
		else if(getState() instanceof ResultTestState) {
			return null;
		}
		else {
			logger.warn("Illegal last state.");
			return null;
		}
	}

}
