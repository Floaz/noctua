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
package net.noctuasource.instance;

import java.util.List;


/**
 * Seperates applications in multiple instance.
 */
public interface InstanceManager {

	/*
	 * Creates an instance and returns it.
	 */
	public Instance					createInstance(InstanceData data, InstanceRunnable entryPoint);

	/*
	 * Creates an instance and returns it.
	 */
	public Instance					createInstance(InstanceData data, InstanceRunnable[] entryPoints);

	/*
	 * Returns the number of instances.
	 */
	public int						getInstancesCount();

	/*
	 * Adds a shutdown hook.
	 * Shutdown hooks are called when the last instance has been closed.
	 */
	public void						addShutdownHook(Runnable runnable);

	/*
	 * Removes a shutdown hook.
	 */
	public void						removeShutdownHook(Runnable runnable);

	/*
	 * Returns all shutdown hooks.
	 */
	public List<Runnable>			getShutdownHooks();

}

