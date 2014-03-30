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
package net.noctuasource.noctua.core.database.impl;


import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;




public class TransactionManagerProxy implements PlatformTransactionManager {

	private PlatformTransactionManager manager;


	public void setManager(PlatformTransactionManager manager) {
		this.manager = manager;
	}




	@Override
	public TransactionStatus getTransaction(TransactionDefinition td) throws TransactionException {
		return manager.getTransaction(td);
	}


	@Override
	public void commit(TransactionStatus ts) throws TransactionException {
		manager.commit(ts);
	}


	@Override
	public void rollback(TransactionStatus ts) throws TransactionException {
		manager.rollback(ts);
	}



}
