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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.model.ContentFlashCardElement;
import net.noctuasource.noctua.core.model.ExampleSentence;
import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.FlashCardElement;
import net.noctuasource.noctua.core.model.FlashCardElementType;
import net.noctuasource.noctua.core.model.VocableMetaInfo;
import net.noctuasource.noctua.core.util.VocableUtil;

import org.apache.log4j.Logger;





public class AddVocabularySimpleForm extends SubContextController implements AddVocabularyPanel {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(AddVocabularySimpleForm.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/AddVocabularySimple.fxml";


	// ***** Members ******************************************************** //

	private VBox			root;

	private GenderMap		genderMap = new GenderMap();
	private PartOfSpeechMap	partOfSpeechMap = new PartOfSpeechMap();



	// ***** FXML Nodes ***************************************************** //

	@FXML private TextField						vocableTextField;
	@FXML private TextField						translation1TextField;
	@FXML private TextField						translation2TextField;
	@FXML private TextField						translation3TextField;
	@FXML private TextField						sentenceTextField;
	@FXML private TextField						sentenceTranslationTextField;
	@FXML private TextField						addInfoTextField;
	@FXML private TextField						tipTextField;
	@FXML private ChoiceBox						genderChoiceBox;
	@FXML private ChoiceBox						partOfSpeechChoiceBox;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

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
		}

        initStaticFields();
		initChoiceBoxes();

		resetPanel();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    }


    private void initChoiceBoxes() {
    	genderChoiceBox.setItems(FXCollections.observableArrayList(genderMap.getGenderStrings()));
    	partOfSpeechChoiceBox.setItems(FXCollections.observableArrayList(partOfSpeechMap.getPartOfSpeechStrings()));
    }


    @FXML
    protected void onVocableTextFieldAction(ActionEvent event) {
		translation1TextField.requestFocus();
    }


    @FXML
    protected void onTranslation1FieldAction(ActionEvent event) {
		if(translation1TextField.getText().trim().isEmpty()) {
			return;
		}

		translation2TextField.requestFocus();
    }


    @FXML
    protected void onTranslation2FieldAction(ActionEvent event) {
		if(translation2TextField.getText().trim().isEmpty()) {
			return;
		}

		translation3TextField.requestFocus();
    }


    @FXML
    protected void onTranslation3FieldAction(ActionEvent event) {
		if(translation3TextField.getText().trim().isEmpty()) {
			return;
		}
    }







	@Override
	public boolean isValidVocable() {
		return VocableUtil.validateVocable(getVocable());
	}


	@Override
	public FlashCard getVocable() {
		FlashCard vocable = new FlashCard();

		ContentFlashCardElement vocableElement = new ContentFlashCardElement();
		vocableElement.setValue(vocableTextField.getText().trim());
		ExampleSentence sentence = new ExampleSentence(vocableElement,
													   sentenceTextField.getText().trim(),
													   sentenceTranslationTextField.getText().trim());
		vocable.addElement(vocableElement);

		vocable.addElement(new FlashCardElement(FlashCardElementType.TIP_CONTENT, tipTextField.getText().trim()));
		vocable.addElement(new FlashCardElement(FlashCardElementType.ADDITIONAL_INFO_CONTENT, addInfoTextField.getText().trim()));

		VocableMetaInfo metaInfo = new VocableMetaInfo();
		metaInfo.setGender(genderMap.getGenderByString((String)genderChoiceBox.getValue()));
		metaInfo.setPartOfSpeech(partOfSpeechMap.getPartOfSpeechByString((String)partOfSpeechChoiceBox.getValue()));
		vocable.addElement(metaInfo);

		vocable.setLevel(FlashCard.FIRST_LEVEL);

		return vocable;

	}


	@Override
	public void resetPanel() {
		vocableTextField.setText("");
		translation1TextField.setText("");
		translation2TextField.setText("");
		translation3TextField.setText("");
		sentenceTextField.setText("");
		sentenceTranslationTextField.setText("");
		addInfoTextField.setText("");
		tipTextField.setText("");
		genderChoiceBox.getSelectionModel().selectFirst();
		partOfSpeechChoiceBox.getSelectionModel().selectFirst();

		vocableTextField.requestFocus();
	}

}
