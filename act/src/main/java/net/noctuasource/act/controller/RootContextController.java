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

import com.google.common.eventbus.Subscribe;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import net.noctuasource.act.controller.events.ControllerDestroyedEvent;
import net.noctuasource.act.data.ControllerParams;




/**
 * Root controller for building a context-oriented application controller tree (ACT).
 *
 * @author Philipp Thomas
 */
public class RootContextController extends AbstractContextController {

	private Map<String, Executor>	executors = new HashMap<>();

	private String					defaultExecutorId = "";



    /**
     * Constructs a new RootContextController.
     */
	public static RootContextController createRootController() {
		RootContextController instance = new RootContextController();
		instance.create(null, null);
		return instance;
	}


    /**
     * Constructs a new RootContextController.
     */
	public static RootContextController createRootController(ControllerParams params) {
		RootContextController instance = new RootContextController();
		instance.create(null, params);
		return instance;
	}


    /**
     * Constructs a new RootContextController.
     */
	public static RootContextController createRootController(Class<? extends RootContextController> clazz,
															 ControllerParams params) {
		RootContextController instance = null;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new IllegalArgumentException("Could not instantiate root controller!");
		}

		instance.create(null, params);
		return instance;
	}


    /**
     * Constructs a new RootContextController and initializes it in the initExecutor.
     */
	public static void createRootController(final Class<? extends RootContextController> clazz,
											final ControllerParams params,
											Executor initExecutor) {
		try {
			final RootContextController instance = clazz.newInstance();

			initExecutor.execute(new Runnable() {
				@Override
				public void run() {
					instance.create(null, params);
				}
			});
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new IllegalArgumentException("Could not instantiate root controller!");
		}
	}



    /**
     * Constructor.
     */
    protected RootContextController() {
	}


	@Override
	protected final void onCreate() {
		registerEventListener(new Object(){
			@Subscribe
			public void onControllerDestroyed(ControllerDestroyedEvent event) {
				if(event.getParentController() == RootContextController.this) {
					if(!getSubControllers().iterator().hasNext()) {
						logger.debug("Last subcontroller has been destroyed. Destroy root!");
						destroy();
					}
				}
			}
		});

		onInit();
	}

	protected void onInit() {
	}


	@Override
	protected void onDestroy() {
	}



	/**
	 * Returns default executor.
	 */
	public synchronized Executor getDefaultExecutor() {
		if(defaultExecutorId.isEmpty()) {
			return null;
		}

		return executors.get(defaultExecutorId);
	}

	/**
	 * Set the default executor for events.
	 * @param defaultExecutorId id of the default executor.
	 */
	public void setDefaultExecutor(String defaultExecutorId) {
		this.defaultExecutorId = defaultExecutorId;
	}


	/**
	 * Returns executor with given id.
	 *
	 * @param executorId id of the executor.
	 */
	public synchronized Executor getExecutor(String executorId) {
		return executors.get(executorId);
	}


	/**
	 * Adds a new executor.
	 * @param executorId id of the new executor.
	 * @param executor the executor to add.
	 */
	public synchronized void addExecutor(String executorId, Executor executor) {
		executors.put(executorId, executor);
	}


	/**
	 * Removes an executor.
	 * @param executorId id of the executor.
	 */
	public synchronized void removeExecutor(String executorId) {
		executors.remove(executorId);
	}
}

