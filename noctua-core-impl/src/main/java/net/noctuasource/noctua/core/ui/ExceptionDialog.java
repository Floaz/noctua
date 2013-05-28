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

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.apache.log4j.Logger;



public class ExceptionDialog {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(ExceptionDialog.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/ExceptionDialog.fxml";
	private static final String	CSS_FILE = "/General.css";



	// ***** Members ******************************************************** //

	private Stage					stage;

	private Exception				exception;





	// ***** FXML Nodes ***************************************************** //

	@FXML private TextField				messageField;
	@FXML private TextArea				fullExceptionField;

	@FXML private Button				ignoreButton;
	@FXML private Button				exitButton;




	// ***** Constructor **************************************************** //

	public static ExceptionDialog create(Exception exception) {
		return new ExceptionDialog(exception);
	}


	protected ExceptionDialog(Exception exception) {
		this.exception = exception;

    	VBox root = new VBox();

    	stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Exception");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        stage.setScene(scene);

    	FXMLLoader loader = new FXMLLoader();
    	loader.setClassLoader(getClass().getClassLoader());
    	loader.setController(this);
    	loader.setLocation(getClass().getResource(FXML_FILE));

        try {
			Node node = (Node) loader.load();
			root.getChildren().add(node);
			VBox.setVgrow(node, Priority.ALWAYS);
		} catch (IOException e) {
			logger.error("Error while creating view: ", e);
			stage.close();
			return;
		}

        messageField.setText(exception.getLocalizedMessage());
        fullExceptionField.setText(ExceptionUtils.getFullStackTrace(exception));

        //stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
	}


	// ***** Methods ******************************************************** //

    @FXML
    protected void handleIgnoreButtonAction(ActionEvent event) {
    	stage.close();
    }

    @FXML
    protected void handleExitButtonAction(ActionEvent event) {
    	stage.close();
		System.exit(0);
    }
}
