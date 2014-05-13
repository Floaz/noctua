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
package net.noctuasource.noctua.core.ui.importer;

import java.util.Stack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.apache.log4j.Logger;




/**
 * View controller for listing and editing the vocabulary.
 * @author Philipp Thomas
 */
public class WizardPane extends StackPane {

	// ***** Basic Static Members ******************************************* //

	private static final Logger logger = Logger.getLogger(WizardPane.class);


	// ***** Static Members ************************************************* //

	private static final int UNDEFINED = -1;


	// ***** Members ******************************************************** //

	private ObservableList<Node> pages = FXCollections.observableArrayList();

	private Stack<Integer> history = new Stack<>();

	private int curPageIdx = UNDEFINED;


	// ***** Constructor **************************************************** //


	// ***** Methods ******************************************************** //
 	void nextPage() {
		if(hasNextPage()) {
			navTo(curPageIdx + 1);
		}
	}


	void priorPage() {
		if(hasPriorPage()) {
			navTo(history.pop(), false);
		}
	}


	boolean hasNextPage() {
		return (curPageIdx < pages.size() - 1);
	}


	boolean hasPriorPage() {
		return !history.isEmpty();
	}


	void navTo(int nextPageIdx, boolean pushHistory) {
		if(nextPageIdx < 0 || nextPageIdx >= pages.size()) {
			return;
		}
		if(curPageIdx != UNDEFINED) {
			if(pushHistory) {
				history.push(curPageIdx);
			}
		}

		Node nextPage = pages.get(nextPageIdx);
		curPageIdx = nextPageIdx;
		getChildren().clear();
		getChildren().add(nextPage);
	}


	void navTo(int nextPageIdx) {
		navTo(nextPageIdx, true);
	}


	void navTo(String id) {
		Node page = lookup("#" + id);
		if(page != null) {
			int nextPageIdx = pages.indexOf(page);
			if(nextPageIdx != UNDEFINED) {
				navTo(nextPageIdx);
			}
		}
	}
}
