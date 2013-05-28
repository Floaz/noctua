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

import net.noctuasource.act.controller.ContextController;




/**
 * An abstract adapter class for receiving controller events.
 * The methods in this class are empty. This class exists as convenience
 * for creating listener objects.
 * @author Philipp Thomas
 */
public abstract class DefaultControllerEventListener implements ControllerEventListener {

	@Override
    public void onBeforeControllerCreated(ContextController createdController) {}

	@Override
	public void onAfterControllerCreated(ContextController createdController) {}

	@Override
    public void onBeforeControllerDestroyed(ContextController createdController) {}

	@Override
	public void onAfterControllerDestroyed(ContextController createdController) {}

}
