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
package net.noctuasource.instance.internal;

import java.util.LinkedList;
import java.util.Queue;

import net.noctuasource.instance.Instance;
import net.noctuasource.instance.InstanceData;
import net.noctuasource.instance.InstanceManager;
import net.noctuasource.instance.InstanceRunnable;

import org.apache.log4j.Logger;



public class InstanceImpl implements Instance {

	private static Logger logger = Logger.getLogger(InstanceImpl.class);


	private State 					state = State.RUNNING;

	private InstanceManagerImpl		instanceManager;

	private Queue<InstanceRunnable>	initializers = new LinkedList<>();

	private Queue<InstanceRunnable>	shutdownHooks = new LinkedList<>();

	private InstanceData			data;



	InstanceImpl(InstanceManagerImpl instanceManager, InstanceData data) {
		this.instanceManager = instanceManager;
		this.data = data;
	}





	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public InstanceManager getInstanceController() {
		return instanceManager;
	}

	@Override
	public InstanceData getData() {
		return data;
	}







	@Override
	public synchronized InstanceRunnable pollRunnable() {
		return initializers.poll();
	}

	@Override
	public synchronized void pushRunnable(InstanceRunnable initializer) {
		initializers.offer(initializer);
	}

	@Override
	public void removeRunnable(InstanceRunnable initializer) {
		initializers.remove(initializer);
	};




	@Override
	public synchronized InstanceRunnable pollShutdownHook() {
		return shutdownHooks.poll();
	}

	@Override
	public synchronized void pushShutdownHook(InstanceRunnable shutdownHook) {
		shutdownHooks.offer(shutdownHook);
	}

	@Override
	public void removeShutdownHook(InstanceRunnable shutdownHook) {
		shutdownHooks.remove(shutdownHook);
	};

}
