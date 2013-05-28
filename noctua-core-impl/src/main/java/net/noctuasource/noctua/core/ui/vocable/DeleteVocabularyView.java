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
package net.noctuasource.noctua.core.ui.vocable;

import net.noctuasource.noctua.core.ui.mainwindow.DeleteTreeNodeView;
import java.util.List;
import javafx.stage.Window;
import javax.annotation.Resource;
import net.noctuasource.noctua.core.bo.FlashCardBo;
import net.noctuasource.act.controller.SubContextController;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.ui.QuestionDialog;
import net.noctuasource.noctua.core.ui.QuestionDialog.Result;



public class DeleteVocabularyView extends SubContextController
								implements QuestionDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(DeleteTreeNodeView.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	@Resource
	private FlashCardBo 		flashCardBo;

	private List<Long> 			flashCardIds;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		flashCardIds = (List<Long>) getControllerParams().getOrThrow("vocableIds");

		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

    	String message = "Möchtest du wirklich "
						+ flashCardIds.size()
						+ " Vokabeln UNWIEDERBRINGLICH löschen?";

    	QuestionDialog.create(parentWindow, "Löschen", message, false, this);
	}


	// ***** Methods ******************************************************** //

	@Override
	public void finish(Result result) {
		if(result == Result.YES) {
			try {
				flashCardBo.deleteVocabulary(flashCardIds);
			} catch (Exception e) {
				logger.warn("Could not delete tree node: ", e);
			}
		}

		destroy();
	}


	@Override
	protected void onDestroy() {
	}




	public void setFlashCardBo(FlashCardBo flashCardBo) {
		this.flashCardBo = flashCardBo;
	}

}
