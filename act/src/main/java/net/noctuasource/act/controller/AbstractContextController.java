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

import net.noctuasource.act.annotation.RunLater;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import net.noctuasource.act.factory.ControllerFactoryRegistry;
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
import net.noctuasource.act.factory.ControllerFactory;
import net.noctuasource.act.factory.DefaultControllerFactory;
import net.noctuasource.act.util.AnnotationRunUtils;

import org.apache.log4j.Logger;



/**
 * Abstract superclass for building a hierarchical controller structure
 * based on Application Controller Tree pattern(ACT).
 *
 * @author Philipp Thomas
 */
abstract class AbstractContextController implements ContextController, ControllerExecutor {

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

	private String									controllerName = null;

	private ControllerState							state = ControllerState.CONSTRUCTED;

    private RootContextController					rootController = null;

    private AbstractContextController				parentController;

    private final Set<ContextController>			subControllers = new HashSet<>();

	private final Set<ControllerEventListener>		controllerEventListeners = new HashSet<>();

	private final List<ControllerFactoryRegistry>	lookupRegistries = new LinkedList<>();

	private ReadonlyControllerParams				params;

	private final ControllerData					data = new ControllerDataImpl();

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

	@Override
	public String getControllerName() {
		if(controllerName == null) {
			return "";
		}

		return controllerName;
	}

	@Override
	public void setControllerName(String newName) {
		controllerName = newName;
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
            this.parentController.addChildController(this);
        }

		eventBus = new AsyncEventBus(new Executor() {
			@Override
			public void execute(Runnable command) {
				executeLater(command);
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

		try {
			onCreate();
			AnnotationRunUtils.runPostConstructMethods(getController());
		}
		catch(RuntimeException e) {
			state = ControllerState.CREATED;
			//destroy();
			throw new ControllerExecutionException("Error while creating controller!", e);
		}

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

		executeRunLaterMethods();
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

		try {
			AnnotationRunUtils.runPreDestroyMethods(getController());
			onDestroy();
		}
		catch(RuntimeException e) {
			logger.error("Exception while destroying controller. ", e);
		}

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
            parentController.removeChildController(this);
        }

		// Process events
		ControllerDestroyedEvent event = new ControllerDestroyedEvent(this, parentController);
		postEvent(event);
    }



	protected void onCreate() {}
	protected void onDestroy() {}


	@Override
	public ReadonlyControllerParams getControllerParams() {
		return params;
	}



	private synchronized void addChildController(AbstractContextController controller) {
		subControllers.add(controller);
	}

	private synchronized void removeChildController(AbstractContextController controller) {
		subControllers.remove(controller);
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
	public synchronized void addControllerLookupRegistry(ControllerFactoryRegistry registry) {
		lookupRegistries.add(registry);
	}

	@Override
	public synchronized void removeControllerLookupRegistry(ControllerFactoryRegistry registry) {
		lookupRegistries.remove(registry);
	}




	protected ControllerFactory lookupControllerFactory(String lookupName) {
		AbstractContextController currentController = this;
		while(currentController != null) {
			for(ControllerFactoryRegistry registry : currentController.lookupRegistries) {
				ControllerFactory foundControllerFactory = registry.lookup(lookupName);
				if(foundControllerFactory != null) {
					return foundControllerFactory;
				}
			}

			currentController = currentController.getParentController();
		}

		throw new ControllerNotFoundException("Controller with name \"" + lookupName + "\" not found!");
	}


	@Override
	public ContextController createController(String lookupName) {
		return createController(lookupName, null);
	}

	@Override
	public ContextController createController(String lookupName, ReadonlyControllerParams params) {
		ControllerFactory factory = lookupControllerFactory(lookupName);
		return createController(factory, params);
	}

	@Override
	public  ContextController createController(Class<?> clazz) {
		return createController(clazz, null);
	}

	@Override
	public ContextController createController(Class<?> clazz, ReadonlyControllerParams params) {
		return createController(new DefaultControllerFactory(clazz), params);
	}


	public ContextController createController(ControllerFactory factory, ReadonlyControllerParams params) {
		if(state != ControllerState.CREATING && state != ControllerState.CREATED) {
			throw new IllegalStateException("Controller can not be executed.");
		}

		if(params == null) {
			params = new ControllerParamsBuilder().build();
		}


		Object controller = factory.create(params);
		ContextController contextController = null;
		if(controller instanceof AbstractContextController) {
			contextController = (ContextController) controller;
		} else {
			contextController = new SubContextProxyController(controller);
		}

		contextController.create(this, params);
		return contextController;
	}



	@Override
	public void postEvent(Object event) {
		logger.debug("Post event: " + event.getClass().getSimpleName());

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
		executeLater(runnable, null);
	}

	@Override
	public void executeLater(Runnable runnable, String executorId) {
		RootContextController currentRootController = getRootController();
		if(currentRootController == null) {
			return;
		}

		Executor executor = executorId == null || executorId.isEmpty()
							? currentRootController.getDefaultExecutor()
							: currentRootController.getExecutor(executorId);

		if(executor == null) {
			throw new IllegalArgumentException("Illegal executor id!");
		}

		executor.execute(runnable);
	}


	@Override
	public Object getController() {
		return this;
	}




	private void executeRunLaterMethods() {
		Class<?> currentClazz = getController().getClass();

		while(currentClazz != null && currentClazz != AbstractContextController.class) {

			for(Method method : currentClazz.getMethods()) {
				if (method.isAnnotationPresent(RunLater.class)) {
					executeRunLaterMethod(method);
				}
			}

			currentClazz = currentClazz.getSuperclass();
		}
	}


	private void executeRunLaterMethod(final Method method) {
		RunLater runLaterAnnotation = method.getAnnotation(RunLater.class);

		try {
			executeLater(new Runnable() {
				@Override
				public void run() {
					try {
						method.invoke(getController());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						logger.warn("Could not execute RunLater method.", ex);
					}
				}
			}, runLaterAnnotation.executor());
		}
		catch(RuntimeException e) {
			logger.error("Error while invoke run later method. ", e);
		}
	}
}

