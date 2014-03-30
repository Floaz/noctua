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
package net.noctuasource.noctua.core.ui.mainwindow;

import javafx.stage.Window;
import javax.annotation.Resource;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.business.TreeNodeBo;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.model.TreeNode;
import net.noctuasource.noctua.core.ui.QuestionDialog;
import net.noctuasource.noctua.core.ui.QuestionDialog.Result;



public class DeleteTreeNodeView extends SubContextController
								implements QuestionDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(DeleteTreeNodeView.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	@Resource
	private TreeNodeBo 		treeNodeBo;

	private TreeNode 		node;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		node = getControllerParams().getOrThrow("treeNode", TreeNode.class);

		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

    	String message = "Möchtest du \""
						+ node.getName()
						+ "\" UNWIEDERBRINGLICH löschen?";

    	QuestionDialog.create(parentWindow, "Löschen", message, false, this);
	}


	// ***** Methods ******************************************************** //

	@Override
	public void finish(Result result) {
		if(result == Result.YES) {
			try {
				treeNodeBo.deleteTreeNode(node.getId());
			} catch (Exception e) {
				logger.warn("Could not delete tree node: ", e);
			}
		}

		destroy();
	}

	@Override
	protected void onDestroy() {
	}

	public void setTreeNodeBo(TreeNodeBo treeNodeBo) {
		this.treeNodeBo = treeNodeBo;
	}

}
