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
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import net.noctuasource.noctua.core.business.LanguageDto;
import net.noctuasource.noctua.core.business.LanguageManageBo;

import net.noctuasource.noctua.core.business.TreeNodeBo;
import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.TreeNodeManagerBo;
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






public class TreeNodeBoImpl implements TreeNodeBo, LanguageManageBo, TreeNodeManagerBo {

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






//	@Override
//	@Transactional
//	public TreeNode getTreeNodeById(String id) {
//		return treeNodeDao.findById(id);
//	}
//
//
//	@Override
//	@Transactional
//	public List<TreeNode> getRootNodes() {
//		return treeNodeDao.getRootNodes();
//	}


	@Override
	public int getNumberFlashCardsOfGroup(GroupList groupList) {
		int number = 0;
		for(TreeNodeDto dto : groupList) {
			FlashCardGroup group = (FlashCardGroup) treeNodeDao.findById(dto.getId());
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


	@Override
	public List<LanguageDto> getLanguages() {
		List<LanguageDto> list = new LinkedList<>();
		for(TreeNode node : treeNodeDao.getRootNodes()) {
			LanguageDto dto = new LanguageDto();
			dto.setId(node.getId());
			dto.setName(node.getName());
			dto.setCode("de");
		}
		return list;
	}


	@Override
	@Transactional
	public void addLanguage(LanguageDto newLanguage) {
		Language treeNode = new Language();
		treeNode.setName(newLanguage.getName());
		treeNode.setLanguageCode(newLanguage.getCode());

		treeNodeDao.create(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.CREATED, treeNode));
	}


	@Override
	public void renameLanguage(LanguageDto newLanguage, String newName) {
		renameTreeNode(newLanguage.getId(), newName);
	}


	@Override
	public void deleteLanguage(LanguageDto newLanguage) {
		deleteTreeNode(newLanguage.getId());
	}


	@Override
	@Transactional
	public TreeNodeDto getTreeNodeById(String id) {
		TreeNode node = treeNodeDao.findById(id);
		return treeNodeToDto(node);
	}


	@Override
	@Transactional
	public List<TreeNodeDto> getChildTreeNodes(TreeNodeDto parent) {
		TreeNode node = treeNodeDao.findById(parent.getId());
		List<TreeNodeDto> list = new LinkedList<>();
		for(TreeNode childNode : node.getChildren()) {
			list.add(treeNodeToDto(childNode));
		}
		return list;
	}


	@Override
	@Transactional
	public List<TreeNodeDto> getRootNodes() {
		List<TreeNodeDto> list = new LinkedList<>();
		for(TreeNode childNode : treeNodeDao.getRootNodes()) {
			list.add(treeNodeToDto(childNode));
		}
		return list;
	}


	@Override
	@Transactional
	public void renameTreeNode(TreeNodeDto dto, String newName) {
		TreeNode treeNode = treeNodeDao.findById(dto.getId());
		treeNode.setName(newName);
		treeNodeDao.update(treeNode);

		dto.setName(newName);

		eventBus.post(new TreeNodeEvent(EventType.UPDATED, treeNode));
	}


	@Override
	@Transactional
	public void moveTreeNode(TreeNodeDto dto, TreeNodeDto newParentTreeNode) {
		TreeNode treeNode = treeNodeDao.findById(dto.getId());
		TreeNode parentTreeNode = treeNodeDao.findById(newParentTreeNode.getId());
		TreeNode oldParentTreeNode = treeNode.getParent();

		oldParentTreeNode.removeChildren(treeNode);
		parentTreeNode.addChildren(treeNode);

		treeNodeDao.update(treeNode);
		treeNodeDao.update(parentTreeNode);

		eventBus.post(new TreeNodeEvent(EventType.UPDATED, treeNode));
	}


	@Override
	@Transactional
	public void deleteTreeNode(TreeNodeDto dto) {
		TreeNode treeNode = treeNodeDao.findById(dto.getId());

		if(treeNode.getParent() != null) {
			treeNode.getParent().removeChildren(treeNode);
		}

		treeNodeDao.delete(treeNode);

		eventBus.post(new TreeNodeEvent(EventType.DELETED, treeNode));
	}


	private TreeNodeDto treeNodeToDto(TreeNode node) {
		TreeNodeDto dto = new TreeNodeDto();
		dto.setId(node.getId());
		dto.setName(node.getName());

		if(node instanceof Folder) {
			dto.setType("Folder");
		}
		else if(node instanceof FlashCardGroup) {
			dto.setType("FlashCardGroup");
		}
		else if(node instanceof Language) {
			dto.setType("Language");
		}

		return dto;
	}

}

