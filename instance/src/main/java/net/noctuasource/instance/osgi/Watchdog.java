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
package net.noctuasource.instance.osgi;

import java.util.Timer;
import java.util.TimerTask;

import net.noctuasource.instance.InstanceManager;

import org.apache.log4j.Logger;



public class Watchdog {
	
	private static final long WATCHDOG_TIMER_DELAY = 10000; // 10 seconds
	private static final long WATCHDOG_TIMER_PERIOD = 5000; // 5 seconds
	private static final long WATCHDOG_COUNTER_MAX = 5;

	private static Logger logger = Logger.getLogger(Watchdog.class);
	

	private Timer 					timer = new Timer();
	
	private int					watchdogCounter = 0;
	
	private InstanceManager 		instanceManager;
	
	private Runnable				zeroInstanceHook;
		
	
	
	public Watchdog( InstanceManager instanceManager, Runnable zeroInstanceHook) {
		this.instanceManager = instanceManager;
		this.zeroInstanceHook = zeroInstanceHook;
	}
	
	
	public synchronized void start() {
		logger.debug("Starting watchdog...");
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				watchInstances();
			}
		}, WATCHDOG_TIMER_DELAY, WATCHDOG_TIMER_PERIOD);
	}


	public synchronized void stop() {
		timer.cancel();
	}


	public synchronized void watchInstances() {
		if(instanceManager.getInstancesCount() <= 0) {
			++watchdogCounter;

			logger.warn("No instance running. Count=" + watchdogCounter);
			
			if(watchdogCounter >= WATCHDOG_COUNTER_MAX) {
				zeroInstanceHook.run();
			}
		}
		else {
			watchdogCounter = 0;
		}
	}
}
