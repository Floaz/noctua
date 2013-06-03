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
package net.noctuasource.noctua.core.ui.test;

import net.noctuasource.act.controller.RunLater;
import net.noctuasource.noctua.core.ui.mainwindow.AddFolderView;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.ExecutorIdentifiers;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.ui.MessageDialog;



public class TooFewFlashCardsMessageDialog extends SubContextController implements MessageDialog.Listener {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(AddFolderView.class);


	// ***** Constructor **************************************************** //

	@RunLater(executor=ExecutorIdentifiers.JAVAFX_EXECUTOR)
	public void init() {
    	String message = "In den ausgewählten Lektionen sind leider zu wenige Vokabeln enthalten, um zu testen. " +
							"Bitte wähle andere Lektionen aus, die mindestens " + 3 + " Vokabeln enthalten!";

    	MessageDialog.create(null, "Zu wenige Vokabeln", message, this);
	}


	// ***** Methods ******************************************************** //

	@Override
	public boolean onMessageClose() {
		destroy();
		return true;
	}

}
