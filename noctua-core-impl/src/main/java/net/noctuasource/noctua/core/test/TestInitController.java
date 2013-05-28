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
import net.noctuasource.noctua.core.test.mc.MCTest;
import net.noctuasource.noctua.core.test.normal.NormalTest;
import net.noctuasource.noctua.core.test.school.SchoolTest;







public class TestInitController extends SubContextController {

	@Override
	protected void onCreate() {
		String testType = getControllerParams().get("testType", String.class);

		switch(testType) {
			case TestTypes.NORMAL_TEST:
				executeController(NormalTest.class, getControllerParams());
				break;
			case TestTypes.MULTIPLE_CHOICE_TEST:
				executeController(MCTest.class, getControllerParams());
				break;
			case TestTypes.SCHOOL_TEST:
				executeController(SchoolTest.class, getControllerParams());
				break;
			default:
				throw new RuntimeException("Illegal test type!");
		}

		// @todo Add listener for destroy.
	}

}
