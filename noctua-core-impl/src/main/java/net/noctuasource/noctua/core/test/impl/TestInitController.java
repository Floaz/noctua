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

import javax.annotation.Resource;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.controller.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.util.AfterDestroyRunnable;
import net.noctuasource.noctua.core.business.FlashCardGroupManagerBo;
import net.noctuasource.noctua.core.business.GroupList;
import net.noctuasource.noctua.core.test.mc.MCTest;
import net.noctuasource.noctua.core.test.normal.NormalTest;
import net.noctuasource.noctua.core.test.school.SchoolTest;
import org.apache.log4j.Logger;







public class TestInitController extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(TestInitController.class);

	private static final int		MIN_FLASHCARDS = 3;


	// ***** Members ******************************************************* //

	@Resource
	FlashCardGroupManagerBo flashCardGroupManagerBo;



	@RunLater
	public void init() {
		String testType = getControllerParams().get("testType", String.class);
		GroupList groupList = getControllerParams().get("groupList", GroupList.class);

		ContextController controller = null;

		if(flashCardGroupManagerBo.getNumberFlashCardsOfGroup(groupList) < MIN_FLASHCARDS) {
			controller = executeController("tooFewFlashCardsMessageDialog");
		}
		else {
			switch(testType) {
				case TestTypes.NORMAL_TEST:
					controller = executeController(NormalTest.class, getControllerParams());
					break;
				case TestTypes.MULTIPLE_CHOICE_TEST:
					controller = executeController(MCTest.class, getControllerParams());
					break;
				case TestTypes.SCHOOL_TEST:
					controller = executeController(SchoolTest.class, getControllerParams());
					break;
				default:
					logger.error("Illegal test type!");
					destroy();
					return;
			}
		}

		AfterDestroyRunnable.create(controller, new Runnable() {
			@Override
			public void run() {
				destroy();
			}
		});
	}


	public void setFlashCardGroupManagerBo(FlashCardGroupManagerBo flashCardGroupManagerBo) {
		this.flashCardGroupManagerBo = flashCardGroupManagerBo;
	}

}
