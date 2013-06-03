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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;





/**
 * The generator for german school mark system.
 * @author Philipp Thomas
 */
public abstract class DefaultMarkGenerator implements MarkGenerator {

	// ***** Members ******************************************************** //

	List<Stop>	stops = new ArrayList<>();


	// ***** Constructor **************************************************** //


	// ***** Methods ******************************************************** //

	protected final void addStop(float correctness, String mark) {
		stops.add(new Stop(correctness, mark));
	}


	@Override
	public String generateMark(float correctness) {
		Collections.sort(stops, new Comparator<Stop>() {
			@Override
			public int compare(Stop o1, Stop o2) {
				return  (int) o2.getCorrectnessStop() - (int) o1.getCorrectnessStop();
			}
		});

		Stop lastStop = null;
		for(Stop stop : stops) {
			if(correctness >= stop.getCorrectnessStop()) {
				return stop.getMark();
			}

			lastStop = stop;
		}

		return lastStop.getMark();
	}





	class Stop {
		float correctnessStop;
		String mark;

		public Stop(float correctnessStop, String mark) {
			this.correctnessStop = correctnessStop;
			this.mark = mark;
		}

		public float getCorrectnessStop() {
			return correctnessStop;
		}

		public String getMark() {
			return mark;
		}

	}

}
