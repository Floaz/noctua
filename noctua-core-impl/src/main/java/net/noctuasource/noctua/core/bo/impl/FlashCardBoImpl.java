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

import com.google.common.eventbus.EventBus;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import net.noctuasource.noctua.core.bo.FlashCardBo;
import net.noctuasource.noctua.core.dao.FlashCardDao;
import net.noctuasource.noctua.core.dao.TreeNodeDao;
import net.noctuasource.noctua.core.events.AbstractObjectEvent.EventType;
import net.noctuasource.noctua.core.events.FlashCardEvent;
import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.FlashCardElementType;
import net.noctuasource.noctua.core.model.FlashCardGroup;
import org.apache.log4j.Logger;
import net.noctuasource.noctua.core.dto.VocableListElement;
import org.springframework.transaction.annotation.Transactional;





public class FlashCardBoImpl implements FlashCardBo {

	private static Logger logger = Logger.getLogger(FlashCardBoImpl.class);


	@Resource
	private TreeNodeDao		treeNodeDao;

	@Resource
	private FlashCardDao	flashCardDao;

	@Resource
	private EventBus		eventBus;




	public void setFlashCardDao(FlashCardDao flashCardDao) {
		this.flashCardDao = flashCardDao;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}




	@Override
	@Transactional
	public List<VocableListElement> getVocabularyOfFlashCardGroup(String flashCardId) {
		List<VocableListElement> list = new LinkedList<>();

		FlashCardGroup flashCardGroup = (FlashCardGroup) treeNodeDao.findById( flashCardId);

		Set<FlashCard> flashCards = flashCardGroup.getFlashCards();

		for(FlashCard flashCard : flashCards) {
			VocableListElement element = new VocableListElement(
														flashCard.getId(),
														flashCard.getElementsString(FlashCardElementType.CONTENT),
														flashCard.getElementsString(FlashCardElementType.EXPLANATION));
			list.add(element);
		}

		return list;
	}


	@Override
	@Transactional
	public void addFlashCard(FlashCard flashCard, String flashCardGroupId) {
		FlashCardGroup flashCardGroup = (FlashCardGroup) treeNodeDao.findById(flashCardGroupId);
		flashCardGroup.addFlashCard(flashCard);

		flashCardDao.create(flashCard);
	}


	@Override
	@Transactional
	public void moveFlashCards(List<String> flashCardIds, String newFlashCardGroupId) {
		FlashCardGroup newFlashCardGroup = (FlashCardGroup) treeNodeDao.findById(newFlashCardGroupId);

		for(String id : flashCardIds) {
			FlashCard flashCard = flashCardDao.findById(id);
			FlashCardGroup oldFlashCardGroup = flashCard.getGroup();
			oldFlashCardGroup.removeFlashCard(flashCard);
			newFlashCardGroup.addFlashCard(flashCard);

			flashCardDao.update(flashCard);
		}
	}


	@Override
	@Transactional
	public void deleteVocabulary(List<String> ids) {
		for(String id : ids) {
			FlashCard flashCard = flashCardDao.findById(id);
			flashCard.getGroup().removeFlashCard(flashCard);
			flashCardDao.delete(flashCard);
		}

		eventBus.post(new FlashCardEvent(EventType.DELETED, null));
	}

}

