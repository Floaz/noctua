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
package net.noctuasource.noctua.core.events;




/*
 * Abstract Event.
 */
public class AbstractObjectEvent<T> {

	// -- Definition --------------------------

	public enum EventType {
		CREATED,
		UPDATED,
		DELETED
	};


	// -- Members ------------------------------

	private EventType	type;

	private T			object;




	public AbstractObjectEvent(EventType type, T object) {
		this.type = type;
		this.object = object;
	}

	public EventType getType() {
		return type;
	}

	public T getObject() {
		return object;
	}

}
