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
import javax.annotation.Resource;
import net.noctuasource.noctua.core.dao.FlashCardDao;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import net.noctuasource.noctua.core.events.AbstractObjectEvent.EventType;
import net.noctuasource.noctua.core.events.FlashCardEvent;
import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.FlashCardElement;





public class FlashCardDaoImpl implements FlashCardDao {

	private static Logger logger = Logger.getLogger(FlashCardDaoImpl.class);



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
	public FlashCard getFlashCardById(Long id) {
		return (FlashCard) sessionHolder.getCurrentSession().load(FlashCard.class, id);
	}




	@Override
	public void insert(FlashCard flashCard) {
		logger.debug("Insert tree node: " + flashCard);

		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().save(flashCard);
		t.commit();

		sessionHolder.getCurrentSession().flush();

		eventBus.post(new FlashCardEvent(EventType.CREATED, flashCard));
	}


	@Override
	public void update(FlashCard flashCard) {
		logger.debug("Update tree node: " + flashCard);

		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().update(flashCard);
		t.commit();

		eventBus.post(new FlashCardEvent(EventType.UPDATED, flashCard));
	}


	@Override
	public void delete(FlashCard flashCard) {
		logger.debug("Delete flash card: " + flashCard);

		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().delete(flashCard);
		t.commit();

		sessionHolder.getCurrentSession().flush();

		eventBus.post(new FlashCardEvent(EventType.DELETED, flashCard));
	}




	@Override
	public void insertElement(FlashCardElement element) {
		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().save(element);
		t.commit();
	}


	@Override
	public void updateElement(FlashCardElement element) {
		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().update(element);
		t.commit();
	}


	@Override
	public void deleteElement(FlashCardElement element) {
		Transaction t = sessionHolder.getCurrentSession().beginTransaction();
		sessionHolder.getCurrentSession().delete(element);
		t.commit();
	}

}

