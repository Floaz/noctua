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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.noctuasource.instance.Instance;
import net.noctuasource.instance.InstanceData;
import net.noctuasource.instance.InstanceManager;
import net.noctuasource.instance.InstanceRunnable;

import org.apache.log4j.Logger;





public class InstanceManagerImpl implements InstanceManager {


	// -- Basic Static Members ------------------------

	static Logger logger = Logger.getLogger(InstanceManagerImpl.class);



	// -- Members ------------------------------

	private int					instanceCounter = 0;

	private Set<Instance>		instances = new HashSet<>();

	private List<Runnable>		shutdownHooks = new LinkedList<>();




	@Override
	public Instance createInstance(InstanceData data, InstanceRunnable entryPoint) {
		return createInstance(data, new InstanceRunnable[]{entryPoint});
	}


	@Override
	public Instance createInstance(InstanceData data, InstanceRunnable[] entryPoints) {
		if(data == null) {
			data = new InstanceData();
		}

        final Instance instance = new InstanceImpl(this, data);
        instances.add(instance);
		++instanceCounter;

		for(InstanceRunnable r : entryPoints) {
			instance.pushRunnable(r);
		}

		Thread instanceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				runInstance(instance);
			}
		});
		instanceThread.setName("Instance" + instanceCounter);
		instanceThread.start();

        return instance;
	}


	@Override
	public synchronized int getInstancesCount() {
		return instances.size();
	}


	protected synchronized void detachInstance(Instance instance) {
		logger.info("Detach instance...");

		if(!instances.contains(instance)) {
			return;
		}

		instances.remove(instance);

		if(instances.isEmpty()) {
			logger.debug("No more instances running.");
			callShutdownHooks();
		}
	}


	@Override
	public synchronized void addShutdownHook(Runnable runnable) {
		shutdownHooks.add(runnable);
	}


	@Override
	public synchronized void removeShutdownHook(Runnable runnable) {
		shutdownHooks.remove(this);
	}


	@Override
	public synchronized List<Runnable> getShutdownHooks() {
		return new LinkedList<>(shutdownHooks);
	}


	protected synchronized void callShutdownHooks() {
		for(Runnable hook : shutdownHooks) {
			hook.run();
		}
	}



	protected void runInstance(Instance instance) {
		instance.setState(Instance.State.RUNNING);

		// Run instance.
		InstanceRunnable runnable = instance.pollRunnable();

		try {
			boolean again = true;
			while(again && runnable != null) {
				again = runnable.run(instance);
				runnable = instance.pollRunnable();
			}

			// Clean runnables.
			while(instance.pollRunnable() != null) {}
		}
		catch(Exception e) {
			logger.error("Exception while running instance!", e);
		}


		instance.setState(Instance.State.QUITTING);

		// Run shutdown hooks.
		// Exception try for every runnable. So every shutdown will be called.
		runnable = instance.pollShutdownHook();

		while(runnable != null) {
			try {
				runnable.run(instance);
			}
			catch(Exception e) {
				logger.error("Exception while running shutdown hook!", e);
			}

			runnable = instance.pollShutdownHook();
		}

		detachInstance(instance);
	}

}

