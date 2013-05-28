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
 * Event listener for controller events that are fired directly.
 * Use this listener only rarely and don't block, because this is executed
 * in the create and destroy methods.
 * @author Philipp Thomas
 */
public interface ControllerEventListener {

    void onBeforeControllerCreated(ContextController createdController);

    void onAfterControllerCreated(ContextController createdController);

    void onBeforeControllerDestroyed(ContextController createdController);

    void onAfterControllerDestroyed(ContextController createdController);

}
