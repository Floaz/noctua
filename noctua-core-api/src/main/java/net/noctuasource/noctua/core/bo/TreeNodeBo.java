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
package net.noctuasource.noctua.core.bo;


import java.util.List;

import net.noctuasource.noctua.core.model.TreeNode;
import net.noctuasource.noctua.core.test.GroupList;


public interface TreeNodeBo {

	public TreeNode			getTreeNodeById(Long id);

	public List<TreeNode>	getRootNodes();

	public int				getNumberFlashCardsOfGroup(GroupList groupList);


	public void addLanguage(String name, String code);
	public void addFolder(String name, TreeNode parentTreeNode);
	public void addFlashCardGroup(String name, TreeNode parentTreeNode);

	public void renameTreeNode(Long id, String newName);

	public void moveTreeNode(Long id, Long newParentTreeNode);

	public void deleteTreeNode(Long id);

}



