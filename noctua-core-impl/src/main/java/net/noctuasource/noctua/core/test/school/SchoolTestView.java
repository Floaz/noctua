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

import java.util.List;

import net.noctuasource.noctua.core.test.TestView;

public interface SchoolTestView extends TestView {


	public static final String	QUESTIONS_TEST_DATA = "questionContexts";
	public static final String	CHECKUPS_TEST_DATA = "checkupContexts";

	
		
	public List<String>		getAnswers();
	
	public List<Boolean>	getResultCorrection();
	
}
