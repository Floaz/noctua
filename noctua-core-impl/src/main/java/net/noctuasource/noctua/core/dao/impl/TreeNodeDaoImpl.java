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
package net.noctuasource.noctua.core.dao.impl;

import com.google.common.eventbus.EventBus;
import java.util.List;
import javax.annotation.Resource;

import net.noctuasource.noctua.core.dao.TreeNodeDao;
import net.noctuasource.noctua.core.events.AbstractObjectEvent.EventType;
import net.noctuasource.noctua.core.events.TreeNodeEvent;
import net.noctuasource.noctua.core.model.Language;
import net.noctuasource.noctua.core.model.TreeNode;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Transaction;






public class TreeNodeDaoImpl implements TreeNodeDao {

	private static Logger logger = Logger.getLogger(TreeNodeDaoImpl.class);


	@Resource
	private SessionHolder	sessionHolder;

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
	public void insert(TreeNode treeNode) {
		logger.debug("Insert tree node: " + treeNode);

		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().save(treeNode);
		t.commit();

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	public void update(TreeNode treeNode) {
		logger.debug("Update tree node: " + treeNode);

		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().update(treeNode);
		t.commit();

		eventBus.post(new TreeNodeEvent(EventType.UPDATED, treeNode));
	}


	@Override
	public void delete(TreeNode treeNode) {
		logger.debug("Delete tree node: " + treeNode);

		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().delete(treeNode);
		t.commit();

		eventBus.post(new TreeNodeEvent(EventType.DELETED, treeNode));
	}

}

