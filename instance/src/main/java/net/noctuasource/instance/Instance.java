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



public interface Instance {

	State getState();
	void setState(State state);

	InstanceManager getInstanceController();
	InstanceData getData();



	InstanceRunnable pollRunnable();
	void pushRunnable(InstanceRunnable initializer);
	void removeRunnable(InstanceRunnable initializer);


	InstanceRunnable pollShutdownHook();
	void pushShutdownHook(InstanceRunnable shutdownHook);
	void removeShutdownHook(InstanceRunnable shutdownHook);


	public enum State {
		RUNNING,
		QUITTING
	};

}
