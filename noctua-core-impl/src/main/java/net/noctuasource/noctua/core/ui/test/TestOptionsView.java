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
package net.noctuasource.noctua.core.ui.test;

import java.io.IOException;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.annotation.Resource;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.scene.control.ListSpinnerIntegerList;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.bo.TreeNodeBo;
import net.noctuasource.noctua.core.test.GroupList;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.QuestionDirection;
import net.noctuasource.noctua.core.test.impl.Test;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.impl.TestSettings;





/**
 *
 * @author Philipp Thomas
 */
public class TestOptionsView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(TestOptionsView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/TestOptionsView.fxml";
	private static final String	CSS_FILE = "/General.css";

	public static final int MIN_NUMBER_WORDS = 3;
	public static final int DEFAULT_NUMBER_WORDS = 30;

	public static final int MIN_TIME_LIMIT_SECONDS = 1;
	public static final int DEFAULT_TIME_LIMIT_SECONDS = 10;
	public static final int MAX_TIME_LIMIT_SECONDS = 180;


	// ***** Members ******************************************************** //

	@Resource
	TreeNodeBo treeNodeBo;

	private Stage		stage;

	private TestData	testData;

	private boolean		continueTestOnClose = false;



	// ***** FXML Nodes ***************************************************** //

	@FXML private Label					headerTitle;
	@FXML private Label					testTypeText;
	@FXML private Tab					optionsTab;
	@FXML private Tab					moreTab;
	@FXML private TitledPane			fetchTitledPane;
	@FXML private TitledPane			timeTitledPane;
	@FXML private TitledPane			directionTitledPane;
	@FXML private Label					chooseMethodDescLabel;
	@FXML private Label					numberDescLabel;
	@FXML private CheckBox				timeLimitCheckbox;
	@FXML private Label					timeLimitDescLabel;
	@FXML private RadioButton			toNativeRadioButton;
	@FXML private RadioButton			toForeignRadioButton;

	@FXML private ChoiceBox<String>		chooseMethodBox;
	@FXML private ListSpinner<Integer>	numberSpinner;
	@FXML private ListSpinner<Integer>	timeLimitField;

	@FXML private CheckBox				affectCardBoxCheckbox;
	@FXML private CheckBox				showImagesCheckbox;
	@FXML private CheckBox				showAddInfoCheckbox;
	@FXML private CheckBox				caseSensitiveCheckbox;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		this.testData = getControllerParams().get("testData", TestData.class);

    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle("Testoptionen");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				destroy();
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
			logger.error("Error while creating test options view: ", e);
			destroy();
		}

        initStaticFields();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	headerTitle.setText("Testoptionen");
    	testTypeText.setText("Multiple Choice");

    	optionsTab.setText("Optionen");
    	moreTab.setText("Erweitert");

    	fetchTitledPane.setText("Auswahl der Fragen");
    	timeTitledPane.setText("Zeitlimit");
    	directionTitledPane.setText("Abfragerichtung");

    	chooseMethodDescLabel.setText("Auswahl:");
    	numberDescLabel.setText("Anzahl Fragen:");
    	timeLimitCheckbox.setText("Zeitlimit einschalten");
    	timeLimitDescLabel.setText("Anzahl Sekunden pro Frage:");
    	toForeignRadioButton.setText("Erklärung erfragen");
    	toNativeRadioButton.setText("Frage erfragen");

    	toForeignRadioButton.setSelected(true);
    	timeLimitCheckbox.setSelected(true);

    	timeLimitCheckbox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				timeLimitDescLabel.setDisable(!timeLimitCheckbox.isSelected());
				timeLimitField.setDisable(!timeLimitCheckbox.isSelected());
			}
		});

    	final ToggleGroup group = new ToggleGroup();
    	toNativeRadioButton.setToggleGroup(group);
    	toForeignRadioButton.setToggleGroup(group);

    	affectCardBoxCheckbox.setText("Karteikasten beeinflussen");
    	showImagesCheckbox.setText("Bilder anzeigen");
    	showAddInfoCheckbox.setText("Zusatzinformationen anzeigen");
    	caseSensitiveCheckbox.setText("Groß-/Kleinschreibung beachten");


		int maxFlashCards = treeNodeBo.getNumberFlashCardsOfGroup(testData.get(TestData.GROUP_LIST, GroupList.class));

    	numberSpinner.setItems(FXCollections.observableList(new ListSpinnerIntegerList(3, maxFlashCards)));
		numberSpinner.setValue(maxFlashCards < 30 ? maxFlashCards : 30);

    	timeLimitField.setItems(FXCollections.observableList(new ListSpinnerIntegerList(1, 200)));
		timeLimitField.setValue(15);

		chooseMethodBox.getItems().clear();
    	chooseMethodBox.getItems().add("Zufallsauswahl");
		chooseMethodBox.getSelectionModel().selectFirst();
    }



    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
    	continueTestOnClose = true;
    	destroy();
    }


    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	destroy();
    }


    private void saveSettings() {
		TestSettings testSettings = new TestSettings();

		testSettings.setNumberOfWords(numberSpinner.getValue());

		//testSettings.setFetchMethod(FetchMethod.RANDOM);

		testSettings.setTimeLimit(timeLimitCheckbox.isSelected());

		testSettings.setTimeLimitSeconds(timeLimitField.getValue());

		testSettings.setDirection( toForeignRadioButton.isSelected()
									? QuestionDirection.CONTENT_TO_EXPLANATION
									: QuestionDirection.EXPLANATION_TO_CONTENT);

		testData.put(TestData.TEST_SETTINGS, testSettings);
    }


	@Override
    protected void onDestroy() {
		stage.close();

		Test test = (Test) testData.get(TestData.TEST_OBJECT);

		if(continueTestOnClose) {
			saveSettings();
			test.next();
		} else {
			test.cancel();
		}
    };





	class NumberKeyFilter implements EventHandler<KeyEvent> {
		   @Override
		   public void handle( KeyEvent t ) {
			  char ar[] = t.getCharacter().toCharArray();
			  char ch = ar[t.getCharacter().toCharArray().length - 1];
			  if (!(ch >= '0' && ch <= '9')) {
				 System.out.println("The char you entered is not a number");
				 t.consume();
			  }
		   }
		}
}
