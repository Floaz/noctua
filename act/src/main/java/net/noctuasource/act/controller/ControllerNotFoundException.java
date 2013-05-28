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
package net.noctuasource.act.controller;






/**
 * Exception that could be thrown by an DatabaseInitializer.
 * @author Philipp Thomas
 */
public class ControllerNotFoundException extends RuntimeException {

	public ControllerNotFoundException() {
	}

	public ControllerNotFoundException(String message) {
		super(message);
	}

	public ControllerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ControllerNotFoundException(Throwable cause) {
		super(cause);
	}

	public ControllerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
