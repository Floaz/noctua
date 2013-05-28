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
package net.noctuasource.act.util;

import com.google.common.eventbus.Subscribe;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.controller.events.ControllerDestroyedEvent;



/**
 * Prints the a subtree to console or OutputStream.
 *
 * @author Philipp Thomas
 */
public abstract class AfterDestroyRunnable implements Runnable {


	// -- Members -------------------------------------

	private ContextController	controller;

	private Object				eventListener;


	/**
	 * Creates an AfterDestroyRunnable.
	 * @param controller
	 * @param runnable
	 * @return AfterDestroyRunnable
	 */
	public static AfterDestroyRunnable create(ContextController controller, final Runnable runnable) {
		return new AfterDestroyRunnable(controller) {
			@Override
			public void run() {
				runnable.run();
			}
		};
	}


	/**
	 * Constructor.
	 */
	public AfterDestroyRunnable(ContextController controller) {
		this.controller = controller;

		eventListener = new Object() {
			@Subscribe
			public void onDestroyEvent(ControllerDestroyedEvent event) {
				destroyEvent(event);
			}
		};

		controller.getParentController().registerEventListener(eventListener);
	}


	private void destroyEvent(ControllerDestroyedEvent event) {
		if(event.getDestroyedController() == controller) {
			run();
			controller.getParentController().unregisterEventListener(eventListener);
		}
	}
}

