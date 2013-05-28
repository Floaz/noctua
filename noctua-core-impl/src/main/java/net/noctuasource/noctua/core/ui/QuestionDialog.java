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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import org.apache.log4j.Logger;



public class QuestionDialog {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(QuestionDialog.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/QuestionDialog.fxml";
	private static final String	CSS_FILE = "/General.css";


	public enum Result {
		YES,
		NO,
		CANCEL
	}



	// ***** Members ******************************************************** //

	private Stage					stage;

	private Listener listener;





	// ***** FXML Nodes ***************************************************** //

	//@FXML private ImageView				icon;
	@FXML private Label					descriptionLabel;

	@FXML private Button				yesButton;
	@FXML private Button				noButton;




	// ***** Constructor **************************************************** //
	public static QuestionDialog create(Window parent, String title, String message,
										boolean yesDefault, Listener listener) {
		return new QuestionDialog(parent, title, message, yesDefault, listener);
	}


	protected QuestionDialog(Window parent, String title, String message,
							boolean yesDefault, Listener listener) {
		this.listener = listener;

    	VBox root = new VBox();

    	stage = new Stage();
    	stage.initModality(Modality.WINDOW_MODAL);
    	stage.initOwner(parent);
        stage.setTitle(title);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				onClose();
			}
		});

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

        descriptionLabel.setText(message);

        if(yesDefault) {
        	yesButton.requestFocus();
        	noButton.setCancelButton(true);
        } else {
        	noButton.requestFocus();
        	noButton.setCancelButton(true);
        }

        //stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
	}


	// ***** Methods ******************************************************** //

    @FXML
    protected void handleYesButtonAction(ActionEvent event) {
    	listener.finish(Result.YES);
    	stage.close();
    }


    @FXML
    protected void handleNoButtonAction(ActionEvent event) {
    	listener.finish(Result.NO);
    	stage.close();
    }


    private void onClose() {
    	listener.finish(Result.CANCEL);
    }



    public interface Listener {

    	void finish(Result result);

    }

}
