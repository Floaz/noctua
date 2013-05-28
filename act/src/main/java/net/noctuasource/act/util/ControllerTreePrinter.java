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
package net.noctuasource.act.util;

import java.io.OutputStream;
import java.io.PrintStream;
import net.noctuasource.act.controller.ContextController;



/**
 * Prints the a subtree to console or OutputStream.
 *
 * @author Philipp Thomas
 */
public class ControllerTreePrinter {


	// -- Members -------------------------------------

	private ContextController	controller;

	private boolean				fullClassName;


	/**
	 * Constructor.
	 */
	public ControllerTreePrinter(ContextController controller, boolean fullClassName) {
		this.controller = controller;
		this.fullClassName = fullClassName;
	}



	public void printToConsole() {
		print(System.out);
	}


	public void print(OutputStream stream) {
		print(controller, stream, 0);
	}

	public void print(ContextController controller, OutputStream stream, int depth) {
		String tabs = "";
		for(int i = 0 ; i < depth; i++) {
			tabs += "\t";
		}

		String classname = fullClassName ? controller.getClass().getName() : controller.getClass().getSimpleName();
		String symbol = controller.hasSubControllers() ? "+" : "-";
		String output = tabs + symbol + " " + classname;

		PrintStream printStream = new PrintStream(stream);
		printStream.println(output);

		for(ContextController childController : controller.getSubControllers()) {
			print(childController, stream, depth+1);
		}
	}

}

