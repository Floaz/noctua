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
package net.noctuasource.act.controller;

import net.noctuasource.act.registry.ControllerLookupRegistry;
import net.noctuasource.act.data.ControllerData;
import net.noctuasource.act.data.ReadonlyControllerParams;
import net.noctuasource.act.events.ControllerEventListener;




/*
 * Context controller.
 */
public interface ContextController {

	void						create(ContextController parentController, ReadonlyControllerParams params);

	void						destroy();

	ContextController			getRootController();

	ContextController			getParentController();

	Iterable<ContextController>	getSubControllers();
	boolean						hasSubControllers();

	void addControllerEventListener(ControllerEventListener listener);
	void removeControllerEventListener(ControllerEventListener listener);

	void addControllerLookupRegistry(ControllerLookupRegistry registry);
	void removeControllerLookupRegistry(ControllerLookupRegistry registry);


	/**
	 * Returns controller data of this controller.
	 */
	ControllerData getLocalControllerData();


	/**
	 * Returns controller data instance which lookup data of whole tree path.
	 */
	ControllerData getControllerData();


	void postEvent(Object event);

	void postEventLocal(Object event);

	void registerEventListener(Object listener);
	void unregisterEventListener(Object listener);

	void executeLater(Runnable runnable);

}
