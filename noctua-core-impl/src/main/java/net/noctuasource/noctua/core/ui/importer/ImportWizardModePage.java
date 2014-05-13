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

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.annotation.PostConstruct;




/**
 * ImportWizardModePage.
 * @author Philipp Thomas
 */
public class ImportWizardModePage extends VBox {


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/ImportWizardModePage.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //


	// ***** FXML Nodes ***************************************************** //

	@FXML private RadioButton					csvRadioButton;
	@FXML private RadioButton					nocvRadioButton;




	// ***** Inject setters ***************************************************** //



	// ***** Constructor **************************************************** //

	@PostConstruct
	public void init() {

    	FXMLLoader loader = new FXMLLoader();
    	loader.setClassLoader(getClass().getClassLoader());
    	loader.setController(this);
    	loader.setLocation(getClass().getResource(FXML_FILE));

        try {
			Node node = (Node) loader.load();
			getChildren().add(node);
			VBox.setVgrow(node, Priority.ALWAYS);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	// ***** Methods ******************************************************** //



	// ***** Handlers ***************************************************** //

}
