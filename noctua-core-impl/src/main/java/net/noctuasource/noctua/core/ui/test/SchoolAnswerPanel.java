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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.CheckupContext;
import net.noctuasource.noctua.core.test.impl.QuestionContext;



public class SchoolAnswerPanel {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(SchoolAnswerPanel.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/SchoolTestAnswerPanel.fxml";
	private static final String	CSS_FILE = "/Test.css";



	// ***** Members ******************************************************** //

	private Node		node;




	// ***** FXML Nodes ***************************************************** //

	@FXML private Label					questionLabel;
	@FXML private TextField				answerTextField;
	@FXML private CheckBox				correctCheckbox;

	@FXML private HBox					answerCorrectBox;
	@FXML private HBox					answerWrongBox;



	// ***** Constructor **************************************************** //

	public SchoolAnswerPanel() {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setClassLoader(getClass().getClassLoader());
    	loader.setController(this);
    	loader.setLocation(getClass().getResource(FXML_FILE));

        try {
			node = (Node) loader.load();
		} catch (IOException e) {
			logger.error("Error while creating answer panel: ", e);
		}

        initStaticFields();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	answerTextField.setPromptText("Keine Antwort");

    	answerCorrectBox.managedProperty().bind(answerCorrectBox.visibleProperty());
    	answerWrongBox.managedProperty().bind(answerWrongBox.visibleProperty());

    	answerCorrectBox.setVisible(false);
    	answerWrongBox.setVisible(false);
    }



    @FXML
    protected void handleTextFieldReturnAction(ActionEvent event) {

    }


    public Node getNode() {
    	return node;
    }


	public void setQuestion(QuestionContext qc) {
		questionLabel.setText(qc.getQuestion());
	}


	public void setCheckup(CheckupContext cc) {
		if(cc.isAnswerCorrect()) {
			answerTextField.setStyle("-fx-border-color: green;");
			answerCorrectBox.setVisible(true);
		} else {
			answerTextField.setStyle("-fx-border-color: red;");
			answerWrongBox.setVisible(true);
		}

		correctCheckbox.setSelected(false);
		answerTextField.setDisable(true);
	}


	public String getAnswer() {
		return answerTextField.getText();
	}


	public boolean isCorrectCheckbox() {
		return correctCheckbox.isSelected();
	};

}
