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
import net.noctuasource.noctua.core.business.VocableManagerBo;
import net.noctuasource.noctua.core.business.VocabularyManagerBo;
import net.noctuasource.noctua.core.business.add.FlashCardGroupDto;
import net.noctuasource.noctua.core.business.add.NewVocable;
import net.noctuasource.noctua.core.business.add.VocableAddBo;
import net.noctuasource.noctua.core.dao.TreeNodeDao;
import net.noctuasource.noctua.core.dao.VocableDao;
import net.noctuasource.noctua.core.dto.EditorEntry;
import net.noctuasource.noctua.core.events.AbstractObjectEvent.EventType;
import net.noctuasource.noctua.core.model.FlashCardGroup;
import org.apache.log4j.Logger;
import net.noctuasource.noctua.core.dto.VocableListElement;
import net.noctuasource.noctua.core.events.VocableEvent;
import net.noctuasource.noctua.core.model.Vocable;
import org.springframework.transaction.annotation.Transactional;





public class VocableManagerBoImpl implements VocableManagerBo, VocableAddBo, VocabularyManagerBo {

	private static Logger logger = Logger.getLogger(VocableManagerBoImpl.class);


	@Resource
	private TreeNodeDao		treeNodeDao;

	@Resource
	private VocableDao		vocableDao;

	@Resource
	private EventBus		eventBus;




	public void setVocableDao(VocableDao vocableDao) {
		this.vocableDao = vocableDao;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}


	@Override
	@Transactional
	public List<EditorEntry> getEditorEntries(FlashCardGroupDto flashCardGroupDto) {
		List<EditorEntry> list = new LinkedList<>();

		FlashCardGroup flashCardGroup = (FlashCardGroup) treeNodeDao.findById(flashCardGroupDto.getId());

		for(Vocable vocable : flashCardGroup.getVocabulary()) {
			EditorEntry entry = new EditorEntry();
			entry.setId(vocable.getId());
			entry.setVocable(vocable.getVocable());
			entry.setNative1(vocable.getNative1());
			entry.setNative2(vocable.getNative2());
			entry.setNative3(vocable.getNative3());
			entry.setExample(vocable.getExample());
			entry.setExampleTranslation(vocable.getExampleTranslation());
			entry.setTip(vocable.getTip());
			entry.setInfo(vocable.getInfo());
			entry.setGender(vocable.getGender().getValue());
			entry.setPartOfSpeech(vocable.getPartOfSpeech().getValue());
			list.add(entry);
		}

		return list;
	}


	@Override
	@Transactional
	public void saveModifiedEntries(List<EditorEntry> modifiedEntries, List<EditorEntry> deletedEntries) {
		for(EditorEntry entry : deletedEntries) {
			Vocable vocable = vocableDao.findById(entry.getId());
			vocable.getGroup().removeVocable(vocable);
			vocableDao.delete(vocable);
		}

//		for(EditorEntry entry : modifiedEntries) {
//			if(entry.getId() == null) {
//				FlashCard flashCard = new FlashCard();
//				//flashCard.getGroup().removeFlashCard(flashCard);
//				flashCardDao.create(flashCard);
//			}
//		}
	}


	@Override
	@Transactional
	public List<VocableListElement> getVocabularyOfFlashCardGroup(String flashCardId) {
		List<VocableListElement> list = new LinkedList<>();

		FlashCardGroup flashCardGroup = (FlashCardGroup) treeNodeDao.findById( flashCardId);

		Set<Vocable> vocabulary = flashCardGroup.getVocabulary();

		for(Vocable vocable : vocabulary) {
			VocableListElement element = new VocableListElement(
														vocable.getId(),
														vocable.getVocable(),
														vocable.getNative1());
			list.add(element);
		}

		return list;
	}


	@Override
	@Transactional
	public void addVocable(NewVocable newFlashCard, FlashCardGroupDto group) {
		Vocable vocable = new Vocable();
		vocable.setVocable(newFlashCard.getForeignString());
		vocable.setNative1(newFlashCard.getNativeString());
		vocable.setExample(newFlashCard.getSentence());
		FlashCardGroup flashCardGroup = (FlashCardGroup) treeNodeDao.findById(group.getId());
		flashCardGroup.addVocable(vocable);
		vocableDao.create(vocable);

		eventBus.post(new VocableEvent(EventType.CREATED, vocable));
	}


	@Override
	@Transactional
	public void addVocable(Vocable vocable, String flashCardGroupId) {
		FlashCardGroup flashCardGroup = (FlashCardGroup) treeNodeDao.findById(flashCardGroupId);
		flashCardGroup.addVocable(vocable);

		vocableDao.create(vocable);

		eventBus.post(new VocableEvent(EventType.CREATED, null));
	}


	@Override
	@Transactional
	public void moveVocabulary(List<String> vocableIds, String newFlashCardGroupId) {
		FlashCardGroup newFlashCardGroup = (FlashCardGroup) treeNodeDao.findById(newFlashCardGroupId);

		for(String id : vocableIds) {
			Vocable vocable = vocableDao.findById(id);
			FlashCardGroup oldFlashCardGroup = vocable.getGroup();
			oldFlashCardGroup.removeVocable(vocable);
			newFlashCardGroup.addVocable(vocable);

			vocableDao.update(vocable);
		}

		eventBus.post(new VocableEvent(EventType.UPDATED, null));
	}


	@Override
	@Transactional
	public void deleteVocabulary(List<String> ids) {
		for(String id : ids) {
			Vocable vocable = vocableDao.findById(id);
			vocable.getGroup().removeVocable(vocable);
			vocableDao.delete(vocable);
		}

		eventBus.post(new VocableEvent(EventType.DELETED, null));
	}

}

