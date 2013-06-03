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

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javax.annotation.Resource;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.bo.TreeNodeBo;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.events.TreeNodeEvent;
import net.noctuasource.noctua.core.model.TreeNode;
import net.noctuasource.noctua.core.ui.mainwindow.ObjectTreeViewItem.Filter;



/**
 * UnitTreeView.
 * @author Philipp Thomas
 */
public class UnitTreeView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(UnitTreeView.class);


	// ***** Static Members ************************************************* //


	// ***** Members ******************************************************** //

	@Resource
	EventBus					eventBus;

	@Resource
	TreeNodeBo					treeNodeBo;

	private TreeView<TreeNode>	treeView;

	private List<Filter>		filters = new LinkedList<>();



	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
        initTreeView();

		eventBus.register(this);
	}


	@Override
	protected void onDestroy() {
		eventBus.unregister(this);
	}


	// ***** Methods ******************************************************** //

    private void initTreeView() {
    	treeView = new TreeView<>();
    	treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    	updateTreeView();
    }


	@Subscribe
    public void updateTreeView(TreeNodeEvent event) {
    	updateTreeView();
    }


    public void updateTreeView() {
    	ObjectTreeViewItem item = new ObjectTreeViewItem(treeNodeBo);
		for(Filter filter : filters) {
			item.addFilter(filter);
		}
    	treeView.setRoot(item);
    	treeView.setShowRoot(false);

    	treeView.getSelectionModel().clearSelection();
    }


    public List<TreeNode> getSelectedNodes() {
    	List<TreeItem<TreeNode>> items = treeView.getSelectionModel().getSelectedItems();
		List<TreeNode> nodes = new LinkedList<>();

		for(TreeItem<TreeNode> item : items) {
			nodes.add(item.getValue());
		}

		return nodes;
    }

    public TreeNode getSelectedNode() {
    	TreeItem<TreeNode> item = treeView.getSelectionModel().getSelectedItem();
    	if(item != null) {
    		return item.getValue();
    	}
    	return null;
    }


    public void setSelectionMode(boolean multipleSelection) {
    	treeView.getSelectionModel().setSelectionMode(multipleSelection
														? SelectionMode.MULTIPLE
														: SelectionMode.SINGLE);
    }


    public void addFilter(Filter filter) {
    	filters.add(filter);
    }

    public void removeFilter(Filter filter) {
    	filters.remove(filter);
    }



    public TreeView getTreeView() {
    	return treeView;
    }



	public void setTreeNodeBo(TreeNodeBo treeNodeBo) {
		this.treeNodeBo = treeNodeBo;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

}
