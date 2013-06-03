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

import java.util.Date;

public class TestResultData {

	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	private TestHistory		testHistory;
	
	private Date			startTime;
	
	private long				timeElapsed;
	
	
	// ***** Constructor **************************************************** //

	public TestResultData(TestHistory testHistory, Date startTime, long timeElapsed) {
		this.testHistory = testHistory;
		this.startTime = startTime;
		this.timeElapsed = timeElapsed;
	}

	
	
	// ***** Methods ******************************************************** //

	public TestHistory getTestHistory() {
		return testHistory;
	}

	
	public Date getStartTime() {
		return startTime;
	}


	public long getTimeElapsed() {
		return timeElapsed;
	}
	
	
}
