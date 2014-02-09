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

import java.util.LinkedList;
import java.util.List;

import net.noctuasource.noctua.core.dataevent.DataEventListener;
import net.noctuasource.noctua.core.dataevent.DataEventRegistry;


public class DataEventController<T> implements DataEventRegistry<T> {

	
	private List<DataEventListener<T>>	listeners =
										new LinkedList<DataEventListener<T>>();
	
	
	


	public void fireEvent(T object, DataEventListener.EventType type) {
		for(DataEventListener<T> listener : listeners) {
			listener.dataEvent(object, type);
		}
	}

	
	
	@Override
	public void registerListener(DataEventListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(DataEventListener<T> listener) {
		listeners.remove(listener);
	}

	
	
	
	
	
}
