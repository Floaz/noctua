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

import java.awt.event.ActionListener;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.noctuasource.act.controller.SubContextController;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.CheckupContext;
import net.noctuasource.noctua.core.test.impl.FlashCardFetcher;
import net.noctuasource.noctua.core.test.impl.QuestionContext;
import net.noctuasource.noctua.core.test.impl.Test;
import net.noctuasource.noctua.core.test.impl.TestCountdown;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.impl.TestHistory;
import net.noctuasource.noctua.core.test.impl.TestView;
import net.noctuasource.noctua.core.test.normal.AnswerContainer;
import net.noctuasource.noctua.core.test.normal.TipGenerator;



public class NormalTestView extends SubContextController
							implements TestView, AnswerContainer {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(NormalTestView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/NormalTestView.fxml";
	private static final String	CSS_FILE = "/General.css";

	private static final double	STAGE_MIN_WIDTH = 500;
	private static final double	STAGE_MIN_HEIGHT = 450;


	// ***** Members ******************************************************** //

	private Stage		stage;

	private TestData	testData;

	private int			tipsCount;



	// ***** FXML Nodes ***************************************************** //

	@FXML private Label					headerTitle;
	@FXML private Label					answerDescription;
	@FXML private Label					tipDescription;
	@FXML private Label					correctDescription;
	@FXML private Label					wrongDescription;
	@FXML private Label					remainingDescription;
	@FXML private Label					tipLabel;
	@FXML private Button				nextButton;
	@FXML private Button				tipButton;
	@FXML private Button				cancelButton;

	@FXML private Label					questionLabel;
	@FXML private TextField				answerTextField;
	@FXML private CheckBox				correctCheckbox;
	@FXML private Label					correctCounterLabel;
	@FXML private Label					wrongCounterLabel;
	@FXML private Label					timeLeftLabel;

	@FXML private HBox					tipBox;
	@FXML private HBox					answerCorrectBox;
	@FXML private HBox					answerWrongBox;

	@FXML private ProgressBar			progressBar;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		this.testData = getControllerParams().get("testData", TestData.class);

    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle("Test");
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
			logger.error("Error while creating test view: ", e);
			destroy();
		}

        initStaticFields();
        initClock();

        stage.sizeToScene();
        stage.setMinWidth(STAGE_MIN_WIDTH);
        stage.setMinHeight(STAGE_MIN_HEIGHT);
        stage.centerOnScreen();
        stage.show();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	headerTitle.setText("Test");
    	answerDescription.setText("Antwort");
    	answerTextField.setPromptText("Keine Antwort");
    	tipDescription.setText("Tipp:");
    	correctDescription.setText("Richtig:");
    	wrongDescription.setText("Falsch:");
    	remainingDescription.setText(""); // Wird später nochmal gesetzt!
    	nextButton.setText(""); // Wird später nochmal gesetzt!
    	tipButton.setText("Tipp geben");
    	cancelButton.setText("Abbrechen");

    	tipBox.managedProperty().bind(tipBox.visibleProperty());
    	answerCorrectBox.managedProperty().bind(answerCorrectBox.visibleProperty());
    	answerWrongBox.managedProperty().bind(answerWrongBox.visibleProperty());
    }


    private void initClock() {
    	final TestCountdown countdown = (TestCountdown) testData.get(TestData.COUNTDOWN_OBJECT);
    	if(countdown == null) {
    		timeLeftLabel.setVisible(false);
    		return;
    	}

    	countdown.addSecondsListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						int s = countdown.getTimeLeft();
						String text = String.format("%02d Min. %02d Sek.", s/60, (s%60));
						text += " verbleibend";
						timeLeftLabel.setText(text);
					}
				});
			}
		});
    }



    @FXML
    protected void handleTextFieldReturnAction(ActionEvent event) {
    	Test test = (Test) testData.get(TestData.TEST_OBJECT);
    	test.next();
    }


    @FXML
    protected void handleNextButtonAction(ActionEvent event) {
    	Test test = (Test) testData.get(TestData.TEST_OBJECT);
    	test.next();
    }


    @FXML
    protected void handleEditButtonAction(ActionEvent event) {

    }


    @FXML
    protected void handleTipButtonAction(ActionEvent event) {
    	QuestionContext qc = (QuestionContext) testData.get(TestData.QUESTION_CONTEXT);
    	TipGenerator gen = new TipGenerator(qc);

    	tipLabel.setText(gen.generate());
    	tipBox.setVisible(true);
    	tipButton.setDisable(true);

    	++tipsCount;
    }


    @FXML
    protected void handleCancelButtonAction(ActionEvent event) {
    	destroy();
    }


	@Override
    protected void onDestroy() {
		stage.close();

		Test test = (Test) testData.get(TestData.TEST_OBJECT);
		test.cancel();
    }


	@Override
	public void showQuestion() {
		QuestionContext qc = (QuestionContext) testData.get(TestData.QUESTION_CONTEXT);
		questionLabel.setText(qc.getQuestion());

		answerTextField.setText("");
		answerTextField.setDisable(false);
		answerTextField.requestFocus();
		answerTextField.setStyle("-fx-border-color: inherit;");

		tipBox.setVisible(false);
		answerCorrectBox.setVisible(false);
		answerWrongBox.setVisible(false);

		TestHistory history = (TestHistory) testData.get(TestData.TEST_HISTORY);
		int counter = history.size();
		int correctCount = history.getCorrectCount();

		FlashCardFetcher fcf = (FlashCardFetcher) testData.get(TestData.FLASH_CARD_FETCHER);
		int sum = counter + fcf.getRemainingCount() +1;

		progressBar.setProgress((double) counter / sum);
		remainingDescription.setText("Vokabel " + (counter + 1) + " von " + sum);
		correctCounterLabel.setText(Integer.toString(correctCount));
		wrongCounterLabel.setText(Integer.toString(counter - correctCount));

    	final TestCountdown countdown = (TestCountdown) testData.get(TestData.COUNTDOWN_OBJECT);
    	timeLeftLabel.setVisible(countdown != null);

    	tipsCount = 0;

		nextButton.setText("Antwort überprüfen");
		tipButton.setDisable(false);
	}


	@Override
	public void showCheckup() {
		CheckupContext cc = (CheckupContext) testData.get(TestData.CHECKUP_CONTEXT);

		answerTextField.setDisable(true);

		if(!cc.isAnswerCorrect()) {
			answerTextField.setStyle("-fx-border-color: red;");
			answerWrongBox.setVisible(true);
		} else {
			answerCorrectBox.setVisible(true);
			answerTextField.setStyle("-fx-border-color: green;");
		}

		correctCheckbox.setSelected(false);

		timeLeftLabel.setVisible(false);

		nextButton.setText("Weiter");
		nextButton.requestFocus();

		tipButton.setDisable(true);
	}


	@Override
	public void dispose() {
		stage.close();
	}


	@Override
	public String getAnswer() {
		return answerTextField.getText();
	}


	@Override
	public boolean isCorrectCheckbox() {
		return correctCheckbox.isSelected();
	};


	public int getTipsCount() {
		return tipsCount;
	};

}
