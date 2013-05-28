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
package net.noctuasource.act.routine;

import com.google.common.eventbus.Subscribe;
import net.noctuasource.act.controller.*;
import net.noctuasource.act.controller.events.ControllerDestroyedEvent;




/**
 * A controller which executes a list of controllers consecutively.
 * If a child controller is destroyed, the next controller in the queue would be executed.
 * At the end, when no controller is left in the queue, the SequenceContextController
 * will be destroyed.
 *
 * @author Philipp Thomas
 */
public abstract class SequenceContextController extends SubContextController {

	private SubContextController currentSubController = null;



    /**
     * Constructor.
     */
    protected SequenceContextController() {
        super();
	}


	@Override
	protected final void onCreate() {
		registerEventListener(new Object() {
			@Subscribe
			public void onControllerDestroyed(ControllerDestroyedEvent event) {
				if(event.getDestroyedController() == currentSubController) {
					nextExecute();
				}
			}
		});

		nextExecute();
	}



	private synchronized void nextExecute() {
		Class<? extends SubContextController> clazz = getNextController();
		if(clazz == null) {
			currentSubController = null;
			destroy();
		} else {
			currentSubController = executeController(clazz);
		}
	}


	protected abstract Class<? extends SubContextController> getNextController();

}

