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
package net.noctuasource.noctua.core.test.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class TestCountdown {

	// ***** Static Members ************************************************* //

	private static Logger logger = Logger.getLogger(TestCountdown.class);

	
	
	
	// ***** Members ******************************************************** //
	
	private final int					initialSeconds;
	
	private Timer						timer;
	
	private int 						seconds = 0;
	
	private boolean						stopped = false;
	
	private Set<ActionListener>			listeners = new HashSet<ActionListener>();
	private Set<ActionListener>			secondsListeners = new HashSet<ActionListener>();
	
	
	
	
	
	// ***** Constructor **************************************************** //

	public TestCountdown(TestData testData) {
		TestSettings testSettings = (TestSettings) testData.get(TestData.TEST_SETTINGS);
		if(testSettings == null || !testSettings.isTimeLimit()) {
			initialSeconds = 0;
		}
		else {
			initialSeconds = testSettings.getTimeLimitSeconds();
		}
	}

	
	public TestCountdown(int seconds) {
		initialSeconds = seconds;
	}
	
	
	
	
	
	
	
	// ***** Methods ******************************************************** //

	public void start() {
		if(initialSeconds <= 0) {
			return;
		}
		
		logger.debug("Start countdown...");
		
		seconds = initialSeconds + 1;
		
		if(timer != null) {
			timer.cancel();
		}
		
		
		timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				--seconds;

				logger.debug("Countdown: " + seconds);
				
				notifySecondsListeners();

				if(seconds <= 0) {
					if(stopped) {
						return;
					}

					notifyListeners();
					stop();
				}
			}
		};
		
		final long oneSecond = 1000L;
		timer.schedule(task, 0, oneSecond);
		
		stopped = false;
	}
	
	
	public void stop() {
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
		
		stopped = true;
	}
	
	
	public int getTimeLeft() {
		return seconds;
	}
	
	
	public void addListener(ActionListener listener) {
		listeners.add(listener);
	}

	
	public void removeListener(ActionListener listener) {
		listeners.remove(listener);
	}
	
	
	
	private void notifyListeners() {
		logger.debug("Notify listeners.");
					
		for(ActionListener listener : listeners) {
			ActionEvent event = new ActionEvent(this, 0, null);
			listener.actionPerformed(event);
		}
	}
	
	
	public void addSecondsListener(ActionListener listener) {
		secondsListeners.add(listener);
	}

	
	public void removeSecondsListener(ActionListener listener) {
		secondsListeners.remove(listener);
	}
	
	
	
	private void notifySecondsListeners() {				
		for(ActionListener listener : secondsListeners) {
			ActionEvent event = new ActionEvent(this, 0, null);
			listener.actionPerformed(event);
		}
	}
	
	
	
	
	
	
	
	
}
