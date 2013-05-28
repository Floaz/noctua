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
import net.noctuasource.noctua.core.bo.TreeNodeBo;
import net.noctuasource.act.controller.SubContextController;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.model.TreeNode;
import net.noctuasource.noctua.core.ui.TextFieldDialog;



public class RenameTreeNodeView extends SubContextController implements TextFieldDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(RenameTreeNodeView.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	@Resource
	private TreeNodeBo 		treeNodeBo;

	private TreeNode 		node;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
    	this.node = getControllerParams().get("treeNode", TreeNode.class);

		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

    	String message = "Gib die neue Bezeichnung für \""
									+ node.getName() + "\" ein:";

    	TextFieldDialog.create(parentWindow, "Umbenennen", message, this);
	}


	// ***** Methods ******************************************************** //

	@Override
	public boolean success(String input) {
		if(!input.trim().isEmpty()) {
			try {
				treeNodeBo.renameTreeNode(node.getId(), input.trim());
			} catch(Exception e) {
				logger.warn("Could not edit tree node!", e);
			}
			destroy();
			return true;
		} else {
			return false;
		}
	}


	@Override
	public void cancel() {
		destroy();
	}


	@Override
	protected void onDestroy() {
	}

	public void setTreeNodeBo(TreeNodeBo treeNodeBo) {
		this.treeNodeBo = treeNodeBo;
	}

}
