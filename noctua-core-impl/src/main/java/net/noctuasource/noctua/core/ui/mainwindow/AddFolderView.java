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
import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.TreeNodeManagerBo;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.ui.TextFieldDialog;



public class AddFolderView extends SubContextController implements TextFieldDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(AddFolderView.class);


	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	@Resource
	private TreeNodeManagerBo	treeNodeBo;

	private TreeNodeDto			node;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		node = getControllerParams().get("treeNode", TreeNodeDto.class);
		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

    	if(node == null) {
    		throw new IllegalArgumentException("Node is null");
    	}

    	String message = "Wie soll der neue Ordner heißen?";

    	TextFieldDialog.create(parentWindow, "Neuer Ordner", message, this);
	}


	// ***** Methods ******************************************************** //

	@Override
	public boolean success(String input) {
		if(input.trim().isEmpty()) {
			return false;
		}

    	try {
    		treeNodeBo.addFolder(input.trim(), node);
    	} catch(Exception e) {
    		logger.warn("Could not add folder!", e);
    	}

		destroy();

    	return true;
	}


	@Override
	public void cancel() {
		destroy();
	}

	public void setTreeNodeBo(TreeNodeManagerBo treeNodeBo) {
		this.treeNodeBo = treeNodeBo;
	}

}
