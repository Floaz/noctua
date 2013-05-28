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
import java.util.concurrent.Executor;
import net.noctuasource.act.controller.events.ControllerDestroyedEvent;
import net.noctuasource.act.data.ControllerParams;




/**
 * Abstract superclass for building a context-oriented application controller tree (ACT).
 *
 * http://www.contextualprogramming.org/articles/the-controller-model-view-architecture-and-the-application-controller-tree/
 *
 * @author Philipp Thomas
 */
public class RootContextController extends AbstractContextController {

	private Executor	executor = new DefaultExecutor();



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
     * Constructor.
     */
    protected RootContextController() {
	}


    /**
     * Constructor.
     */
    protected RootContextController(Executor executer) {
        this.executor = executer;
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


	public synchronized Executor getExecutor() {
		return executor;
	}

	public synchronized void setExecutor(Executor executor) {
		this.executor = executor;
	}



	/**
	 * Does nothing.
	 */
	class DefaultExecutor implements Executor {

		@Override
		public void execute(Runnable command) {
			// Do nothing
		}
	}
}

