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
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.noctuasource.act.controller.SubContextController;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.QuestionContext;
import net.noctuasource.noctua.core.test.impl.Test;
import net.noctuasource.noctua.core.test.impl.TestCountdown;
import net.noctuasource.noctua.core.test.impl.TestData;
import net.noctuasource.noctua.core.test.impl.TestView;
import net.noctuasource.noctua.core.test.school.CheckupContainer;
import net.noctuasource.noctua.core.test.school.QuestionContainer;
import net.noctuasource.noctua.core.test.school.SchoolTestView;



public class SchoolTestViewImpl extends SubContextController
								implements TestView, SchoolTestView {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(SchoolTestViewImpl.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/SchoolTestView.fxml";
	private static final String	CSS_FILE = "/Test.css";

	private static final double	STAGE_MIN_WIDTH = 500;
	private static final double	STAGE_MIN_HEIGHT = 450;


	// ***** Members ******************************************************** //

	private Stage						stage;

	private TestData					testData;

	private List<SchoolAnswerPanel>		answerPanels = new ArrayList<>();



	// ***** FXML Nodes ***************************************************** //

	@FXML private Label					headerTitle;
	@FXML private Button				nextButton;
	@FXML private Button				cancelButton;

	@FXML private VBox					answerPanelBox;

	@FXML private Label					timeLeftLabel;
	//@FXML private VBox					addonBox;



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
    	cancelButton.setText("Abbrechen");
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
    protected void handleNextButtonAction(ActionEvent event) {
    	Test test = (Test) testData.get(TestData.TEST_OBJECT);
    	test.next();
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
		QuestionContainer questions = (QuestionContainer) testData.get(SchoolTestView.QUESTIONS_TEST_DATA);

		for(QuestionContext qc : questions) {
			SchoolAnswerPanel panel = new SchoolAnswerPanel();
			panel.setQuestion(qc);
			answerPanels.add(panel);

			answerPanelBox.getChildren().add(panel.getNode());
		}

		nextButton.setText("Antworten überprüfen");
	}


	@Override
	public void showCheckup() {
		CheckupContainer checkups = (CheckupContainer) testData.get(SchoolTestView.CHECKUPS_TEST_DATA);

		for(int i = 0; i < checkups.size(); ++i) {
			SchoolAnswerPanel panel = answerPanels.get(i);
			panel.setCheckup(checkups.get(i));
		}

		timeLeftLabel.setVisible(false);

		nextButton.setText("Ergebnis ansehen");
		nextButton.requestFocus();
	}


	@Override
	public void dispose() {
		stage.close();
	}


	@Override
	public List<String> getAnswers() {
		List<String> answers = new ArrayList<>();

		for(int i = 0; i < answerPanels.size(); ++i) {
			answers.add(answerPanels.get(i).getAnswer());
		}

		return answers;
	}


	@Override
	public List<Boolean> getResultCorrection() {
		List<Boolean> resultCorrections = new ArrayList<>();

		for(int i = 0; i < answerPanels.size(); ++i) {
			resultCorrections.add(answerPanels.get(i).isCorrectCheckbox());
		}

		return resultCorrections;
	}

}
