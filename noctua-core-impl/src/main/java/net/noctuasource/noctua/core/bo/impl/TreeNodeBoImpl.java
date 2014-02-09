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
package net.noctuasource.noctua.core.bo.impl;

import net.noctuasource.noctua.core.dao.impl.TreeNodeDaoImpl;
import com.google.common.eventbus.EventBus;
import java.util.List;
import javax.annotation.Resource;

import net.noctuasource.noctua.core.bo.TreeNodeBo;
import net.noctuasource.noctua.core.dao.TreeNodeDao;
import net.noctuasource.noctua.core.events.AbstractObjectEvent.EventType;
import net.noctuasource.noctua.core.events.TreeNodeEvent;
import net.noctuasource.noctua.core.model.FlashCardGroup;
import net.noctuasource.noctua.core.model.Folder;
import net.noctuasource.noctua.core.model.Language;
import net.noctuasource.noctua.core.model.TreeNode;
import net.noctuasource.noctua.core.test.GroupList;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;






public class TreeNodeBoImpl implements TreeNodeBo {

	private static Logger logger = Logger.getLogger(TreeNodeDaoImpl.class);


	@Resource
	private TreeNodeDao		treeNodeDao;

	@Resource
	private EventBus		eventBus;




	public void setTreeNodeDao(TreeNodeDao treeNodeDao) {
		this.treeNodeDao = treeNodeDao;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}






	@Override
	@Transactional
	public TreeNode getTreeNodeById(String id) {
		return treeNodeDao.findById(id);
	}


	@Override
	@Transactional
	public List<TreeNode> getRootNodes() {
		return treeNodeDao.getRootNodes();
	}


	@Override
	public int getNumberFlashCardsOfGroup(GroupList groupList) {
		int number = 0;
		for(FlashCardGroup group : groupList) {
			number += group.getFlashCards().size();
		}
		return number;
	}


	@Override
	@Transactional
	public void addLanguage(String name, String code) {
		Language treeNode = new Language();
		treeNode.setName(name);
		treeNode.setLanguageCode(code);

		treeNodeDao.create(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	@Transactional
	public void addFolder(String name, TreeNode parentNode) {
		Folder treeNode = new Folder();
		treeNode.setName(name);
		treeNode.setExpanded(true);
		parentNode.addChildren(treeNode);

		treeNodeDao.create(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	@Transactional
	public void addFlashCardGroup(String name, TreeNode parentNode) {
		FlashCardGroup treeNode = new FlashCardGroup();
		treeNode.setName(name);
		parentNode.addChildren(treeNode);
		treeNode.setMaxFlashCardGroups(6);

		treeNodeDao.create(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	@Transactional
	public void renameTreeNode(String id, String newName) {
		TreeNode treeNode = treeNodeDao.findById(id);
		treeNode.setName(newName);
		treeNodeDao.update(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.UPDATED, treeNode));
	}


	@Override
	@Transactional
	public void moveTreeNode(String id, String newParentTreeNodeId) {
		TreeNode treeNode = treeNodeDao.findById(id);
		TreeNode newParentTreeNode = treeNodeDao.findById(newParentTreeNodeId);
		TreeNode oldParentTreeNode = treeNode.getParent();

		oldParentTreeNode.removeChildren(treeNode);
		newParentTreeNode.addChildren(treeNode);

		treeNodeDao.update(treeNode);
		treeNodeDao.update(newParentTreeNode);

		eventBus.post(new TreeNodeEvent(EventType.UPDATED, treeNode));
	}


	@Override
	@Transactional
	public void deleteTreeNode(String id) {
		TreeNode treeNode = treeNodeDao.findById(id);

		if(treeNode.getParent() != null) {
			treeNode.getParent().removeChildren(treeNode);
		}

		treeNodeDao.delete(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.DELETED, treeNode));
	}

}

