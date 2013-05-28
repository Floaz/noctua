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
package net.noctuasource.noctua.core.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;




public final class ColorUtils {

	public static Color getGradientColor(Stop[] stops, double frac) {
		Stop lastStop = null;
		for(Stop stop : stops) {
			if(frac <= stop.getOffset()) {
				if(lastStop == null) {
					return stop.getColor();
				} else {
					double newFrac = (frac -lastStop.getOffset()) * (1 / (stop.getOffset() - lastStop.getOffset()));
					return interpolateColor(lastStop.getColor(), stop.getColor(), newFrac);
				}
			}

			lastStop = stop;
		}

		return lastStop.getColor();
	}


	private static Color interpolateColor(Color c1, Color c2, double frac) {
		double red = c1.getRed() + (c2.getRed() - c1.getRed()) * frac;
		double green = c1.getGreen() + (c2.getGreen() - c1.getGreen()) * frac;
		double blue = c1.getBlue() + (c2.getBlue() - c1.getBlue()) * frac;
		double alpha = c1.getOpacity() + (c2.getOpacity() - c1.getOpacity()) * frac;
		red = red < 0 ? 0 : (red > 1 ? 1 : red);
		green = green < 0 ? 0 : (green > 1 ? 1 : green);
		blue = blue < 0 ? 0 : (blue > 1 ? 1 : blue);
		alpha = alpha < 0 ? 0 : (alpha > 1 ? 1 : alpha);
		return Color.color(red, green, blue, alpha);
	}

}
