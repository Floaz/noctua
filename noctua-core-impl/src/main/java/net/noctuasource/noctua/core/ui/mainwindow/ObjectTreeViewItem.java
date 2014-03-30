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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.TreeNodeManagerBo;






/**
 * ObjectTreeViewItem.
 * @author Philipp Thomas
 */
public class ObjectTreeViewItem extends TreeItem<TreeNodeDto> {

	private static final int	MAX_DEPTH = 30;


	private TreeNodeManagerBo	treeNodeManagerBo;

	private TreeNodeDto			currentNode;

	private boolean 			childrenLoaded = false;

	private int					depth = 0;

	private RootInfo			rootInfo;



	public ObjectTreeViewItem(TreeNodeManagerBo treeNodeManagerBo) {
		this.treeNodeManagerBo = treeNodeManagerBo;
		rootInfo = new RootInfo();
	}


	private ObjectTreeViewItem(TreeNodeManagerBo treeNodeManagerBo, TreeNodeDto node, int depth, RootInfo rootInfo) {
		this.treeNodeManagerBo = treeNodeManagerBo;
		this.currentNode = node;
		this.depth = depth;
		this.rootInfo = rootInfo;

		super.setValue(node);


		String iconFilename = "/images/Placebo.png";
		boolean expanded = false;

		switch(node.getType()) {
			case "Language":
				iconFilename = "/images/World.png";
				expanded = true;
				break;
			case "Folder":
				iconFilename = "/images/Folder.png";
				expanded = node.isExpanded();
				break;
			case "FlashCardGroup":
				iconFilename = "/images/Group.png";
				break;
		}

		super.setExpanded(expanded);

		Image icon = new Image(ObjectTreeViewItem.class.getResourceAsStream(iconFilename));
		super.setGraphic(new ImageView(icon));
	}


	@Override
	public ObservableList<TreeItem<TreeNodeDto>> getChildren() {
		if(!childrenLoaded) {
			loadChildren();
		}

		return super.getChildren();
	}


	@Override
	public boolean isLeaf() {
		if(!childrenLoaded) {
			loadChildren();
		}

		return super.getChildren().isEmpty();
	}



	private void loadChildren() {
		if(depth > MAX_DEPTH) {
			return;
		}

		childrenLoaded = true;

		List<TreeNodeDto> nodes;
		if(currentNode == null) {
			nodes = treeNodeManagerBo.getRootNodes();
		} else {
			nodes = treeNodeManagerBo.getChildTreeNodes(currentNode);
		}

		sortChildren(nodes);

		List<TreeItem<TreeNodeDto>> rawList = new ArrayList<>();

		for(TreeNodeDto node : nodes) {
			boolean filtered = false;
			for(Filter filter : rootInfo.filters) {
				if(filter.filter(node)) {
					filtered = true;
					break;
				}
			}

			if(!filtered) {
				ObjectTreeViewItem item = new ObjectTreeViewItem(treeNodeManagerBo, node, depth+1, rootInfo);
				rawList.add(item);
			}
		}

		super.getChildren().addAll(rawList);
	}



	private void sortChildren(List<TreeNodeDto> nodes) {
		Collections.sort(nodes, new Comparator<TreeNodeDto>() {
			@Override
			public int compare(TreeNodeDto o1, TreeNodeDto o2) {
				if(o1.getType().equals("Folder") && !(o2.getType().equals("Folder"))) {
					return -1;
				}
				if(!(o1.getType().equals("Folder")) && o2.getType().equals("Folder")) {
					return 1;
				}
				else {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			}
		});
	}


	public void addFilter(Filter filter) {
		rootInfo.filters.add(filter);
	}

	public void removeFilter(Filter filter) {
		rootInfo.filters.remove(filter);
	}




	public static interface Filter {
		boolean filter(TreeNodeDto node);
	}


	class RootInfo {
		public List<Filter>		filters = new LinkedList<>();
	}

}



