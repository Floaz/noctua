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
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.inject.Inject;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.business.add.FlashCardGroupDto;
import net.noctuasource.noctua.core.business.add.NewVocable;
import net.noctuasource.noctua.core.business.add.VocableAddBo;

import org.apache.log4j.Logger;





public class AddVocabularySimpleForm extends SubContextController implements AddVocabularyPanel {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(AddVocabularySimpleForm.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/AddVocabularySimple.fxml";


	// ***** Members ******************************************************** //

	@Inject
	private VocableAddBo		vocableAddBo;

	private VBox				root;



	// ***** FXML Nodes ***************************************************** //

	@FXML private TextField						vocableTextField;
	@FXML private TextField						translation1TextField;
	@FXML private TextField						translation2TextField;
	@FXML private TextField						translation3TextField;
	@FXML private TextField						sentenceTextField;



	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		Stage parentWindow = getControllerParams().get("parentWindow", Stage.class);

    	FXMLLoader loader = new FXMLLoader();
    	loader.setClassLoader(getClass().getClassLoader());
    	loader.setController(this);
    	loader.setLocation(getClass().getResource(FXML_FILE));

        try {
			root = new VBox();
			Node node = (Node) loader.load();
			root.getChildren().add(node);
			VBox.setVgrow(node, Priority.ALWAYS);
		} catch (IOException e) {
			logger.error("Error while creating result view: ", e);
		}

        initStaticFields();

		resetPanel();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    }


    protected void performSave() {
		postEvent(new ReadyToAddEvent());
    }


    @FXML
    protected void onVocableTextFieldAction(ActionEvent event) {
		translation1TextField.requestFocus();
    }


    @FXML
    protected void onTranslation1TextFieldAction(ActionEvent event) {
		if(translation1TextField.getText().trim().isEmpty()) {
			performSave();
			return;
		}

		translation2TextField.requestFocus();
    }


    @FXML
    protected void onTranslation2TextFieldAction(ActionEvent event) {
		if(translation2TextField.getText().trim().isEmpty()) {
			performSave();
			return;
		}

		translation3TextField.requestFocus();
    }


    @FXML
    protected void onTranslation3TextFieldAction(ActionEvent event) {
		if(translation3TextField.getText().trim().isEmpty()) {
			performSave();
		}
    }






    public Node getNode() {
		return root;
    }


	@Override
	public boolean isValidVocable() {
		return !vocableTextField.getText().trim().isEmpty() && !translation1TextField.getText().trim().isEmpty();
	}


	@Override
	public void save(FlashCardGroupDto group) {
		NewVocable newVocable = new NewVocable();
		newVocable.setForeignString(vocableTextField.getText().trim());
		newVocable.setNativeString(translation1TextField.getText().trim());
		newVocable.setSentence(sentenceTextField.getText().trim());

		vocableAddBo.addVocable(newVocable, group);
	}


	@Override
	public void resetPanel() {
		vocableTextField.setText("");
		translation1TextField.setText("");
		translation2TextField.setText("");
		translation3TextField.setText("");
		sentenceTextField.setText("");

		vocableTextField.requestFocus();
	}


	public void setVocableAddBo(VocableAddBo vocableAddBo) {
		this.vocableAddBo = vocableAddBo;
	}


}
