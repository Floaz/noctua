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
package net.noctuasource.noctua.core.ui.loading;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.log4j.Logger;



/**
 * Loading screen, that shows a prograss indicator.
 * @author Philipp Thomas
 */
public class LoadingScreenView implements LoadingScreen {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(LoadingScreenView.class);


	// ***** Static Members ************************************************* //

	//private static final String	FXML_FILE = "/MessageDialog.fxml";
	//private static final String	CSS_FILE = "/General.css";

	private static final int WIDTH = 200;
	private static final int HEIGHT = 200;

	private static LoadingScreenView	instance = null;


	// ***** Members ******************************************************** //

	private VBox				layout;
	private ProgressIndicator	progressInidicator;
	private Label				progressText;
	private Stage				stage;




	// ***** Constructor **************************************************** //

	public static LoadingScreenView get() {
		if(instance == null) {
			instance = new LoadingScreenView();
		}

		return instance;
	}


	protected LoadingScreenView() {
		progressInidicator = new ProgressIndicator();
		progressInidicator.setPrefWidth(WIDTH);
		progressInidicator.setPrefHeight(HEIGHT);

		progressText = new Label("Bitte warten . . .");
		progressText.setAlignment(Pos.CENTER);
		progressText.setFont(Font.font(null, FontWeight.BOLD, 16));

		layout = new VBox();
		layout.setSpacing(10);
		layout.setAlignment(Pos.CENTER);

		VBox infoLayout = new VBox();
		infoLayout.setAlignment(Pos.CENTER);

		layout.getChildren().addAll(progressInidicator, infoLayout);
		infoLayout.getChildren().addAll(progressText);
		infoLayout.setStyle("-fx-padding: 5; -fx-background-color: #EEF2FF; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, #AAAACC, derive(#AAAACC, 50%));");
		layout.setEffect(new DropShadow());


    	stage = new Stage();
		//stage.initModality(Modality.APPLICATION_MODAL);
    	stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Loading...");

		Scene scene = new Scene(layout);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.centerOnScreen();
	}




	// ***** Methods ******************************************************** //

	@Override
	public void show() {
		stage.setIconified(false);
		stage.toFront();
		stage.show();
	}

	@Override
	public void hide() {
		if(stage.isShowing()) {
			stage.hide();
		}
	}
}
