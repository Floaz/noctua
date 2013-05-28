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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import javax.annotation.Resource;
import net.noctuasource.noctua.core.bo.FlashCardBo;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.model.Vocable;

import org.apache.log4j.Logger;





public class EditVocabularyForm extends SubContextController implements AddVocabularyPanel {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(EditVocabularyForm.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/EditVocabularyForm.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	@Resource
	FlashCardBo				flashCardBo;


	private Node			node;

	private Long			flashCardGroupId;

	private GenderMap		genderMap = new GenderMap();
	private PartOfSpeechMap	partOfSpeechMap = new PartOfSpeechMap();



	// ***** FXML Nodes ***************************************************** //

	@FXML private TextArea						elementInputTextField;
	@FXML private ChoiceBox						elementTypeChoiceBox;
	@FXML private Button						elementAddButton;

	@FXML private TabPane						tabPane;

	@FXML private TableView						wordsTableView;
	@FXML private Button						deleteWordsButton;

	@FXML private ChoiceBox						genderChoiceBox;
	@FXML private ChoiceBox						partOfSpeechChoiceBox;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		this.flashCardGroupId = getControllerParams().get("flashCardGroupId", Long.class);

		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

    	FXMLLoader loader = new FXMLLoader();
    	loader.setClassLoader(getClass().getClassLoader());
    	loader.setController(this);
    	loader.setLocation(getClass().getResource(FXML_FILE));

        try {
			node = (Node) loader.load();
		} catch (IOException e) {
			logger.error("Error while creating result view: ", e);
			return;
		}

        initStaticFields();
		updateElementAddButton();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	//headerTitle.setText("Testergebnis");
    }


    private void initChoiceBoxes() {
    	genderChoiceBox.setItems(FXCollections.observableArrayList(genderMap.getGenderStrings()));
    	partOfSpeechChoiceBox.setItems(FXCollections.observableArrayList(partOfSpeechMap.getPartOfSpeechStrings()));
    }


    private void updateElementAddButton() {
        elementAddButton.setDisable(elementInputTextField.getText().trim().isEmpty());
	}




    @FXML
    protected void handleElementInputTextFieldKeyTyped(KeyEvent event) {
		updateElementAddButton();
    }


    @FXML
    protected void handleElementAddButtonAction(ActionEvent event) {
    }


    @FXML
    protected void handleDeleteWordsButtonAction(ActionEvent event) {
    }


	@Override
	protected void onDestroy() {
	}




    public Node getNode() {
    	return node;
    }



	@Override
	public boolean isValidVocable() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Vocable getVocable() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void resetPanel() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}




	public void setFlashCardBo(FlashCardBo flashCardBo) {
		this.flashCardBo = flashCardBo;
	}

}
