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
package net.noctuasource.act.events;

import java.util.Set;
import java.util.HashSet;
import net.noctuasource.act.controller.ContextController;




/**
 * It holds the processing data of an event.
 * @author Philipp Thomas
 */
public abstract class AbstractControllerEvent {

	private ContextController				sourceController = null;

    private Set<ContextController>			firedInControllers = new HashSet<>();

	/**
	 *
	 */
	protected AbstractControllerEvent() {
	}

    public ContextController getSourceController() {
        return sourceController;
    }

	public void setSourceController(ContextController sourceController) {
		this.sourceController = sourceController;
	}

    void addFiredInController(ContextController seenController) {
        firedInControllers.add(seenController);
    }

    boolean alreadyFired(ContextController controller) {
        return firedInControllers.contains(controller);
    }
}
