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

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import net.noctuasource.act.registry.ControllerLookupRegistry;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import net.noctuasource.act.controller.events.ControllerCreatedEvent;
import net.noctuasource.act.controller.events.ControllerDestroyedEvent;
import net.noctuasource.act.data.ControllerData;
import net.noctuasource.act.data.ControllerDataImpl;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.act.data.ReadonlyControllerParams;
import net.noctuasource.act.events.AbstractControllerEvent;
import net.noctuasource.act.events.ControllerEventListener;

import org.apache.log4j.Logger;



/**
 * Abstract superclass for building a hierarchical controller structure
 * based on Application Controller Tree pattern(ACT).
 *
 * @author Philipp Thomas
 */
abstract class AbstractContextController implements ContextController, ContextControllerExecuter {

	// -- Enum ----------------------------------------

	public enum ControllerState {
		CONSTRUCTED,
		CREATING,
		CREATED,
		DESTROYING,
		DESTROYED
	}


	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(AbstractContextController.class);



	// -- Members -------------------------------------

	private ControllerState							state = ControllerState.CONSTRUCTED;

    private RootContextController					rootController = null;

    private AbstractContextController				parentController;

    private Set<ContextController>					subControllers = new HashSet<>();

	private Set<ControllerEventListener>			controllerEventListeners = new HashSet<>();

	private List<ControllerLookupRegistry>			lookupRegistries = new LinkedList<>();

	private ReadonlyControllerParams				params;

	private ControllerData							data = new ControllerDataImpl();

	private EventBus								eventBus;



    /**
     * Subclass wants to control own view and is root controller.
     */
    protected AbstractContextController() {
	}


	@Override
    public synchronized RootContextController getRootController() {
		if(rootController == null) {
			ContextController current = this;
			while(current.getParentController() != null) {
				current = current.getParentController();
			}

			rootController = (RootContextController) current;
		}

		return rootController;
    }


	@Override
    public synchronized AbstractContextController getParentController() {
        return parentController;
    }


	@Override
    public synchronized Iterable<ContextController> getSubControllers() {
        return new LinkedList<>(subControllers);
    }

	@Override
	public boolean hasSubControllers() {
		return !subControllers.isEmpty();
	}


	@Override
	public synchronized ControllerData getLocalControllerData() {
		return data;
	}

	@Override
	public synchronized ControllerData getControllerData() {
		return new ControllerDataTreeImpl(this);
	}




    /**
     * Close controller and all children, detach it from parent controller.
     */
	@Override
    public final synchronized void create(ContextController parentController, ReadonlyControllerParams params) {
		if(state != ControllerState.CONSTRUCTED) {
			throw new IllegalStateException("Controller was already created.");
		}

        logger.debug("Create controller: " + getClass().getSimpleName());

		state = ControllerState.CREATING;

		this.params = params;

        // Check if this is a subcontroller or a root controller
        if (parentController != null) {
            this.parentController = (AbstractContextController) parentController;
            this.parentController.subControllers.add(this);
        }

		eventBus = new AsyncEventBus(new Executor() {
			@Override
			public void execute(Runnable command) {
				getRootController().getExecutor().execute(command);
			}
		});


		// Fire controller before creating events.
		AbstractContextController currentController = this;
		while(currentController != null) {
			for(ControllerEventListener listener : currentController.controllerEventListeners) {
				listener.onBeforeControllerCreated(this);
			}
			currentController = currentController.getParentController();
		}

		onCreate();

		state = ControllerState.CREATED;

		// Fire controller after created events.
		currentController = this;
		while(currentController != null) {
			for(ControllerEventListener listener : currentController.controllerEventListeners) {
				listener.onAfterControllerCreated(this);
			}
			currentController = currentController.getParentController();
		}


		// Process events
		ControllerCreatedEvent event = new ControllerCreatedEvent(this);
		postEvent(event);


		// Process RunLater methods
		final List<Method> runLaterMethods = new LinkedList<>();
		Class<?> currentClazz = this.getClass();
		while(currentClazz != null && currentClazz != AbstractContextController.class) {
			for(Method method : currentClazz.getMethods()) {
				if (method.isAnnotationPresent(RunLater.class)) {
					runLaterMethods.add(method);
				}
			}
			currentClazz = currentClazz.getSuperclass();
		}

		executeLater(new Runnable() {
			@Override
			public void run() {
				for(Method method : runLaterMethods) {
					try {
						method.invoke(AbstractContextController.this);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						logger.warn("Could not invoke RunLater method.", ex);
					}
				}
			}
		});
    }


    /**
     * Close controller and all children, detach it from parent controller.
     */
	@Override
    public final synchronized void destroy() {
		if(state == ControllerState.CREATING) {
			throw new IllegalStateException("Controller can't be destroyed while it is created.");
		}
		else if(state != ControllerState.CREATED) {
			return;
		}

		state = ControllerState.DESTROYING;


        logger.debug("Destroy controller: " + getClass().getSimpleName());

		// Fire controller before creating events.
		AbstractContextController currentController = this;
		while(currentController != null) {
			for(ControllerEventListener listener : currentController.controllerEventListeners) {
				listener.onBeforeControllerDestroyed(this);
			}
			currentController = currentController.getParentController();
		}

		// Destroy all childs.
		List<ContextController> clonedSubControllers = new ArrayList<>(subControllers);
        for(ContextController subController : clonedSubControllers) {
            subController.destroy();
        }

		onDestroy();

		state = ControllerState.DESTROYED;

		// Fire controller after destroy events.
		currentController = this;
		while(currentController != null) {
			for(ControllerEventListener listener : currentController.controllerEventListeners) {
				listener.onAfterControllerDestroyed(this);
			}
			currentController = currentController.getParentController();
		}

		// Detach from parent.
        if (parentController != null) {
            parentController.subControllers.remove(this);
        }

		// Process events
		ControllerDestroyedEvent event = new ControllerDestroyedEvent(this, parentController);
		postEvent(event);
    }



	protected void onCreate() {}
	protected void onDestroy() {}


	protected ReadonlyControllerParams getControllerParams() {
		return params;
	}


	@Override
	public void addControllerEventListener(ControllerEventListener listener) {
		controllerEventListeners.add(listener);
	}

	@Override
	public void removeControllerEventListener(ControllerEventListener listener) {
		controllerEventListeners.remove(listener);
	}

	@Override
	public synchronized void addControllerLookupRegistry(ControllerLookupRegistry registry) {
		lookupRegistries.add(registry);
	}

	@Override
	public synchronized void removeControllerLookupRegistry(ControllerLookupRegistry registry) {
		lookupRegistries.remove(registry);
	}




	protected Class<? extends ContextController> lookupController(String lookupName) {
		AbstractContextController currentController = this;
		while(currentController != null) {
			for(ControllerLookupRegistry registry : currentController.lookupRegistries) {
				Class<? extends ContextController> foundControllerClass = registry.lookup(lookupName);
				if(foundControllerClass != null) {
					return foundControllerClass;
				}
			}

			currentController = currentController.getParentController();
		}

		throw new ControllerNotFoundException("Controller with name \"" + lookupName + "\" not found!");
	}


	@Override
	public ContextController executeController(String lookupName) {
		return executeController(lookupName, null);
	}

	@Override
	public ContextController executeController(String lookupName, ReadonlyControllerParams params) {
		Class<? extends ContextController> clazz = lookupController(lookupName);
		return executeController(clazz, params);
	}

	@Override
	public <T extends ContextController> T executeController(Class<T> clazz) {
		return executeController(clazz, null);
	}

	@Override
	public <T extends ContextController> T executeController(Class<T> clazz, ReadonlyControllerParams params) {
		if(state != ControllerState.CREATING && state != ControllerState.CREATED) {
			throw new IllegalStateException("Controller can not be executed.");
		}

		if(params == null) {
			params = new ControllerParamsBuilder().build();
		}


		ContextController controller = null;
		try {
			controller = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			logger.error("Could not intantiate controller.", ex);
			throw new RuntimeException("Could not intantiate controller.", ex);
		}
		controller.create(this, params);
		return (T) controller;
	}



	@Override
	public void postEvent(Object event) {
		if(event instanceof AbstractControllerEvent) {
			((AbstractControllerEvent)event).setSourceController(this);
		}

		AbstractContextController currentController = this;
		while(currentController != null) {
			currentController.postEventLocal(event);
			currentController = currentController.getParentController();
		}
	}

	@Override
	public void postEventLocal(Object event) {
		if(event instanceof AbstractControllerEvent) {
			((AbstractControllerEvent)event).setSourceController(this);
		}

		eventBus.post(event);
	}

	@Override
	public synchronized void registerEventListener(Object listener) {
		eventBus.register(listener);
	}

	@Override
	public synchronized void unregisterEventListener(Object listener) {
		eventBus.unregister(listener);
	}

	@Override
	public void executeLater(Runnable runnable) {
		getRootController().getExecutor().execute(runnable);
	}

}

