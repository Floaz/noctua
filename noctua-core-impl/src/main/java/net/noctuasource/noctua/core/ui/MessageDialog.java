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
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import org.apache.log4j.Logger;



public class MessageDialog {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(MessageDialog.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/MessageDialog.fxml";
	private static final String	CSS_FILE = "/General.css";



	// ***** Members ******************************************************** //

	private Stage					stage;

	private Listener listener;





	// ***** FXML Nodes ***************************************************** //

	//@FXML private ImageView				icon;
	@FXML private Label					descriptionLabel;

	//@FXML private Button				okButton;
	//@FXML private Button				cancelButton;




	// ***** Constructor **************************************************** //

	public static MessageDialog create(Window parent, String title, String message, Listener listener) {
		return new MessageDialog(parent, title, message, listener);
	}


	protected MessageDialog(Window parent, String title, String message, Listener listener) {
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
				onOk();
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

        //stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
	}


	// ***** Methods ******************************************************** //

    @FXML
    protected void handleOkButtonAction(ActionEvent event) {
    	onOk();
    }


    private void onOk() {
    	if(listener.onMessageClose()) {
    		stage.close();
    	}
    }



    public interface Listener {

    	boolean onMessageClose();

    }

}
