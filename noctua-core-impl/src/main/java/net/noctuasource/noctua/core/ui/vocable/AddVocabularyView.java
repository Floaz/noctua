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

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.SkinBase;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.annotation.Resource;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.bo.FlashCardBo;
import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.Vocable;
import net.noctuasource.noctua.core.ui.mainwindow.MainWindowView;
import net.noctuasource.noctua.core.util.VocableUtil;

import org.apache.log4j.Logger;





public class AddVocabularyView extends SubContextController implements AddVocabularyPanel {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(AddVocabularyView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/AddVocabulary.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	@Resource
	FlashCardBo				flashCardBo;


	private Stage			stage;

	private String			flashCardGroupId;

	private GenderMap		genderMap = new GenderMap();
	private PartOfSpeechMap	partOfSpeechMap = new PartOfSpeechMap();



	// ***** FXML Nodes ***************************************************** //

	@FXML private TabPane						tabbedPane;

	@FXML private TextArea						foreignTextArea;
	@FXML private TextArea						nativeTextArea;
	@FXML private TextArea						foreignSentenceTextArea;
	@FXML private TextField						addInfoTextField;
	@FXML private ChoiceBox						genderChoiceBox;
	@FXML private ChoiceBox						partOfSpeechChoiceBox;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		this.flashCardGroupId = getControllerParams().get("flashCardGroupId", String.class);

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

        initStaticFields();
        //initTabs();
		initChoiceBoxes();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();

		resetPanel();
		updateButtons();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	//headerTitle.setText("Testergebnis");
		foreignTextArea.addEventFilter(KeyEvent.KEY_PRESSED, new TabKeyEventHandler());
		nativeTextArea.addEventFilter(KeyEvent.KEY_PRESSED, new TabKeyEventHandler());
		foreignSentenceTextArea.addEventFilter(KeyEvent.KEY_PRESSED, new TabKeyEventHandler());
    }


    private void initChoiceBoxes() {
    	genderChoiceBox.setItems(FXCollections.observableArrayList(genderMap.getGenderStrings()));
    	partOfSpeechChoiceBox.setItems(FXCollections.observableArrayList(partOfSpeechMap.getPartOfSpeechStrings()));
    }


    private void initTabs() {
    	EditVocabularyForm form = executeController(EditVocabularyForm.class);

		Tab tab = new Tab();
    	tab.setClosable(false);
    	tab.setText("Lektionen");
    	ImageView icon = new ImageView(
    			new Image(MainWindowView.class.getResourceAsStream(
    													"/images/Units.png")));
    	icon.setFitWidth(24);
    	icon.setFitHeight(24);
    	tab.setGraphic(icon);
    	tab.setContent(form.getNode());
    	tabbedPane.getTabs().add(tab);
    }


    private void updateButtons() {
        //addButton.setDisable(false);
	}


    @FXML
    protected void handleAddButtonAction(ActionEvent event) {
		Vocable newVocable = getVocable();
		if(!VocableUtil.validateVocable(newVocable)) {
			return;
		}

		flashCardBo.addFlashCard(newVocable, flashCardGroupId);

		resetPanel();
    }

    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	destroy();
    }


	@Override
	protected void onDestroy() {
		stage.close();
	}







	@Override
	public boolean isValidVocable() {
		return VocableUtil.validateVocable(getVocable());
	}


	@Override
	public Vocable getVocable() {
		String[] foreignWords = foreignTextArea.getText().split("\n");
		String[] nativeWords = nativeTextArea.getText().split("\n");
		String[] sentences = foreignSentenceTextArea.getText().split("\n");
		String[] addInfo = addInfoTextField.getText().split("\n");

		Vocable vocable = new Vocable();

		VocableUtil.addWordsToVocable(vocable, foreignWords, nativeWords);
		VocableUtil.addSentencesToVocable(vocable, sentences);
		VocableUtil.addAddInfoToVocable(vocable, addInfo);

		vocable.setGender(genderMap.getGenderByString((String)genderChoiceBox.getValue()));
		vocable.setPartOfSpeech(partOfSpeechMap.getPartOfSpeechByString((String)partOfSpeechChoiceBox.getValue()));

		vocable.setLevel(FlashCard.FIRST_LEVEL);
		return vocable;

	}


	@Override
	public void resetPanel() {
		foreignTextArea.setText("");
		nativeTextArea.setText("");
		foreignSentenceTextArea.setText("");
		addInfoTextField.setText("");
		genderChoiceBox.getSelectionModel().selectFirst();
		partOfSpeechChoiceBox.getSelectionModel().selectFirst();

		foreignTextArea.requestFocus();
	}



	public void setFlashCardBo(FlashCardBo flashCardBo) {
		this.flashCardBo = flashCardBo;
	}





	class TabKeyEventHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.TAB) {
                SkinBase skin = (SkinBase) ((TextArea)event.getSource()).getSkin();
                if (skin.getBehavior() instanceof TextAreaBehavior) {
                    TextAreaBehavior behavior = (TextAreaBehavior) skin.getBehavior();
                    if (event.isControlDown()) {
                        behavior.callAction("InsertTab");
                    } else {
                        behavior.callAction("TraverseNext");
                    }
                    event.consume();
                }

            }
        }
    }
}
