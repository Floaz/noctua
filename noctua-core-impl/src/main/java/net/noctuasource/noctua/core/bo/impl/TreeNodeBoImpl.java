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

import net.noctuasource.noctua.core.dao.impl.SessionHolder;
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

import org.apache.log4j.Logger;
import org.hibernate.Query;






public class TreeNodeBoImpl implements TreeNodeBo {

	private static Logger logger = Logger.getLogger(TreeNodeDaoImpl.class);


	@Resource
	private SessionHolder	sessionHolder;

	@Resource
	private TreeNodeDao		treeNodeDao;

	@Resource
	private EventBus		eventBus;





	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}






	@Override
	public TreeNode getTreeNodeById(Long id) {
		return (TreeNode) sessionHolder.getCurrentSession().load(TreeNode.class, id);
	}


	@Override
	public List<TreeNode> getRootNodes() {

		Query query = sessionHolder.getCurrentSession()
							.createQuery("from " + Language.class.getName());

		@SuppressWarnings("unchecked")
		List<TreeNode> list = (List<TreeNode>) query.list();
		return list;
	}


	@Override
	public void addLanguage(String name, String code) {
		Language treeNode = new Language();
		treeNode.setName(name);
		treeNode.setLanguageCode(code);
		treeNodeDao.insert(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	public void addFolder(String name, TreeNode parentNode) {
		Folder treeNode = new Folder();
		treeNode.setName(name);
		treeNode.setExpanded(true);
		parentNode.addChildren(treeNode);
		treeNodeDao.insert(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	public void addFlashCardGroup(String name, TreeNode parentNode) {
		FlashCardGroup treeNode = new FlashCardGroup();
		treeNode.setName(name);
		parentNode.addChildren(treeNode);
		treeNode.setMaxFlashCardGroups(6);
		treeNodeDao.insert(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	public void renameTreeNode(Long id, String newName) {
		TreeNode treeNode = treeNodeDao.getTreeNodeById(id);
		treeNode.setName(newName);
		treeNodeDao.update(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.UPDATED, treeNode));
	}


	@Override
	public void deleteTreeNode(Long id) {
		TreeNode treeNode = treeNodeDao.getTreeNodeById(id);
		
		if(treeNode.getParent() != null) {
			treeNode.getParent().removeChildren(treeNode);
		}

		treeNodeDao.delete(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.DELETED, treeNode));
	}




	public void setTreeNodeDao(TreeNodeDao treeNodeDao) {
		this.treeNodeDao = treeNodeDao;
	}

}

