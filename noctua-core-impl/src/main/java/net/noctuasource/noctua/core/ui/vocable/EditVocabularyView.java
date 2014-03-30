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
package net.noctuasource.noctua.core.ui.vocable;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.annotation.Resource;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.business.FlashCardBo;

import org.apache.log4j.Logger;





public class EditVocabularyView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(EditVocabularyView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/EditVocabulary.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	@Resource
	FlashCardBo					flashCardBo;


	private Stage				stage;

	private EditVocabularyForm	form;

	private Long				flashCardId;


	// ***** FXML Nodes ***************************************************** //

	@FXML private VBox			formBox;

	@FXML private Button		editButton;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		this.flashCardId = getControllerParams().get("flashCardId", Long.class);

		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

    	VBox root = new VBox();

    	stage = new Stage();
    	stage.initModality(Modality.WINDOW_MODAL);
    	stage.initOwner(parentWindow);
        stage.setTitle("Vokabeln hinzufügen");
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
			logger.error("Error while creating result view: ", e);
			stage.close();
		}

        initForm();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();

		updateButtons();
	}


	// ***** Methods ******************************************************** //

    private void initForm() {
    	form = executeController(EditVocabularyForm.class);
    	formBox.getChildren().add(form.getNode());
    }


    private void updateButtons() {
        editButton.setDisable(false);
	}


    @FXML
    protected void handleEditButtonAction(ActionEvent event) {
//		Vocable newVocable = getVocable();
//		if(!VocableUtil.validateVocable(newVocable)) {
//			return;
//		}
//
//		flashCardBo.addFlashCard(newVocable, flashCardGroupId);
//
//		resetPanel();
    	destroy();
    }

    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	destroy();
    }


	@Override
	protected void onDestroy() {
		stage.close();
	}


	public void setFlashCardBo(FlashCardBo flashCardBo) {
		this.flashCardBo = flashCardBo;
	}
}
