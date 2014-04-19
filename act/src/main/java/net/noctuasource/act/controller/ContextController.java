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

import net.noctuasource.act.factory.ControllerFactoryRegistry;
import net.noctuasource.act.data.ControllerData;
import net.noctuasource.act.data.ReadonlyControllerParams;
import net.noctuasource.act.events.ControllerEventListener;




/*
 * Context controller.
 */
public interface ContextController extends ControllerExecutor {

	void						create(ContextController parentController, ReadonlyControllerParams params);

	void						destroy();

	ContextController			getRootController();

	ContextController			getParentController();

	Iterable<ContextController>	getSubControllers();
	boolean						hasSubControllers();

	void addControllerEventListener(ControllerEventListener listener);
	void removeControllerEventListener(ControllerEventListener listener);

	void addControllerLookupRegistry(ControllerFactoryRegistry registry);
	void removeControllerLookupRegistry(ControllerFactoryRegistry registry);


	/**
	 * Returns controller name or empty string, if controller has no name.
	 */
	String getControllerName();

	/**
	 * Sets the controller name.
	 */
	void setControllerName(String newName);


	/**
	 * Returns controller data of this controller.
	 */
	ControllerData getLocalControllerData();


	/**
	 * Returns controller data instance which lookup data of whole tree path.
	 */
	ControllerData getControllerData();


	/**
	 * Returns controller params.
	 */
	ReadonlyControllerParams getControllerParams();


	void postEvent(Object event);

	void postEventLocal(Object event);

	void registerEventListener(Object listener);

	void unregisterEventListener(Object listener);


	/**
	 * Run the specified Runnable on the default Thread at some unspecified time in the future.
	 *
	 * @param runnable the Runnable whose run method will be executed.
	 */
	void executeLater(Runnable runnable);


	/**
	 * Run the specified Runnable on the specified executor Thread at some unspecified time in the future.
	 *
	 * @param runnable the Runnable whose run method will be executed
	 * @param executorId the Id of the executor who will be used to execut.
	 *
	 * @throws IllegalArgumentException when executor with id does not exist.
	 */
	void executeLater(Runnable runnable, String executorId);



	Object getController();

}
