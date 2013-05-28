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
package net.noctuasource.act.javafx;



import java.util.concurrent.Executor;
import javafx.application.Platform;




/**
 * An executor for root controller that executes Runnables in JavaFX-Thread.
 * @author Philipp Thomas
 */
public class JfxPlatformExecutor implements Executor {

	private ExceptionHandler	exceptionHandler;




	public JfxPlatformExecutor() {
		this.exceptionHandler = null;
	}

	public JfxPlatformExecutor(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}


	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}




	@Override
	public void execute(final Runnable command) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					command.run();
				}
				catch(Exception e) {
					if(exceptionHandler != null) {
						exceptionHandler.handleException(e);
					}
				}
			}
		});
	}

}
