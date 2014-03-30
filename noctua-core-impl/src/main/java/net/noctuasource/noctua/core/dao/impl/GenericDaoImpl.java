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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import net.noctuasource.noctua.core.dao.GenericDAO;


import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;






public class GenericDaoImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	private static Logger logger = Logger.getLogger(GenericDaoImpl.class);


	@Resource
	private SessionHolder	sessionHolder;


	private final Class<T> persistentClass;




	public GenericDaoImpl() {
		Class<?> superclass = getClass();
		while(!superclass.getSuperclass().equals(GenericDaoImpl.class)) {
			superclass = superclass.getSuperclass();
		}
        Type t = superclass.getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.persistentClass = (Class) pt.getActualTypeArguments()[0];
	}




	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}


	protected EntityManager getEntityManager() {
		throw new UnsupportedOperationException("EntityManager not supported.");
	}


	protected Session getSession() {
		if(sessionHolder == null) {
			throw new IllegalStateException("Session Holder is null!");
		}

		Session session = sessionHolder.getCurrentSession();

		if(session == null) {
			throw new IllegalStateException("Session Holder holds no session!");
		}

		return session;
	}



	@Override
	public T findById(ID id) {
		return (T) getSession().get(persistentClass, id);
	}


	@Override
	public List<T> findByKeyValue(String key, String value) {
		Query query = getSession().createQuery("from " + getPersistentClass().getName() + " as p where p." + key + "=?");
		query.setParameter(1, value);
		return query.list();
	}


	@Override
	public List<T> getAll() {
		Query query = getSession().createQuery("FROM " + getPersistentClass().getName() + " c");
        return query.list();
	}


	@Override
	public void create(T c) {
		Session session = getSession();
		session.save(c);
		session.flush();
	}


	@Override
	public void save(T object) {
		getSession().saveOrUpdate(object);
	}


	@Override
	public void update(T object) {
		getSession().update(object);
	}


	@Override
	public void delete(T object) {
		getSession().delete(object);
	}


	@Override
	public Class<T> getPersistentClass() {
		return persistentClass;
	}
}

