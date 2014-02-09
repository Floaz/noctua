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

import java.util.LinkedList;

public class TestHistory extends LinkedList<TestHistoryElement> {

	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	//private List<TestHistoryElement>	elements = new LinkedList<TestHistoryElement>();
	
	
	
	
	// ***** Constructor **************************************************** //

	// ***** Methods ******************************************************** //

	public int getCorrectCount() {
		int n = 0;
		
		for(TestHistoryElement element : this) {
			if(element.isCorrect()) {
				++n;
			}
		}
		
		return n;
	}

	
	public int getTipsCount() {
		int n = 0;
		
		for(TestHistoryElement element : this) {
			n += element.getTipsCount();
		}
		
		return n;
	}
	
	
	
	
	
	
}