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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import net.noctuasource.noctua.core.business.TreeNodeBo;
import net.noctuasource.noctua.core.model.FlashCardGroup;
import net.noctuasource.noctua.core.model.Folder;
import net.noctuasource.noctua.core.model.Language;
import net.noctuasource.noctua.core.model.TreeNode;






/**
 * ObjectTreeViewItem.
 * @author Philipp Thomas
 */
public class ObjectTreeViewItem extends TreeItem<TreeNode> {

	private static final int	MAX_DEPTH = 30;


	private TreeNodeBo			treeNodeBo;

	private TreeNode			currentNode;

	private boolean 			childrenLoaded = false;

	private int					depth = 0;

	private RootInfo			rootInfo;



	public ObjectTreeViewItem(TreeNodeBo treeNodeBo) {
		this.treeNodeBo = treeNodeBo;
		rootInfo = new RootInfo();
	}


	public ObjectTreeViewItem(TreeNodeBo treeNodeBo, TreeNode node, int depth, RootInfo rootInfo) {
		this.treeNodeBo = treeNodeBo;
		this.currentNode = node;
		this.depth = depth;
		this.rootInfo = rootInfo;

		super.setValue(node);


		String iconFilename = "/images/Placebo.png";
		boolean expanded = false;

		if(node instanceof Language) {
			iconFilename = "/images/World.png";
			expanded = true;
		} else if(node instanceof Folder) {
			iconFilename = "/images/Folder.png";
			expanded = ((Folder)node).isExpanded();
		} else if(node instanceof FlashCardGroup) {
			iconFilename = "/images/Group.png";
		}

		super.setExpanded(expanded);

		Image icon = new Image(ObjectTreeViewItem.class.getResourceAsStream(iconFilename));
		super.setGraphic(new ImageView(icon));
	}


	@Override
	public ObservableList<TreeItem<TreeNode>> getChildren() {
//		if(!childrenLoaded) {
//			loadChildren();
//		}

		return super.getChildren();
	}


	@Override
	public boolean isLeaf() {
//		if(!childrenLoaded) {
//			loadChildren();
//		}

		return super.getChildren().isEmpty();
	}



	private void loadChildren() {
		if(depth > MAX_DEPTH) {
			return;
		}

		childrenLoaded = true;

		List<TreeNode> nodes;
		if(currentNode == null) {
			nodes = treeNodeBo.getRootNodes();
		} else {
			nodes = currentNode.getChildren();
		}

		sortChildren(nodes);

		List<TreeItem<TreeNode>> rawList = new ArrayList<>();

		for(TreeNode node : nodes) {
			boolean filtered = false;
			for(Filter filter : rootInfo.filters) {
				if(filter.filter(node)) {
					filtered = true;
					break;
				}
			}

			if(!filtered) {
				ObjectTreeViewItem item = new ObjectTreeViewItem(treeNodeBo, node, depth+1, rootInfo);
				rawList.add(item);
			}
		}

		super.getChildren().addAll(rawList);
	}



	private void sortChildren(List<TreeNode> nodes) {
		Collections.sort(nodes, new Comparator<TreeNode>() {
			@Override
			public int compare(TreeNode o1, TreeNode o2) {
				if(o1 instanceof Folder && !(o2 instanceof Folder)) {
					return -1;
				}
				if(!(o1 instanceof Folder) && o2 instanceof Folder) {
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
		boolean filter(TreeNode node);
	}


	class RootInfo {
		public List<Filter>		filters = new LinkedList<>();
	}

}



