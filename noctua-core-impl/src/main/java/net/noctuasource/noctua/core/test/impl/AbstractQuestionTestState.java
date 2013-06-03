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










public abstract class AbstractQuestionTestState implements TestState {

	// ***** Members ******************************************************** //

	Date questionStart;
	

	// ***** Constructor **************************************************** //
	
	
	// ***** Methods ******************************************************** //

	@Override
	public void initialize(final TestData data) {
		TestCountdown countdown = (TestCountdown) data.get(TestData.COUNTDOWN_OBJECT);
		if(countdown != null) {
			countdown.start();
		}

		questionStart = new Date();
	}


	@Override
	public void destroy(TestData data) {
		TestCountdown countdown = (TestCountdown) data.get(TestData.COUNTDOWN_OBJECT);
		if(countdown != null) {
			countdown.stop();
		}
		
		Long counter = (Long) data.get(TestData.ELAPSED_TIME_COUNTER);
		counter += (new Date().getTime() - questionStart.getTime()) / 1000;
		data.put(TestData.ELAPSED_TIME_COUNTER, counter);
	}
	
	
	
	
}
