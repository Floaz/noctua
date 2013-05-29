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
package net.noctuasource.noctua.core.ui;

import java.awt.SplashScreen;
import javafx.animation.FadeTransition;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import org.apache.log4j.Logger;



public class Splash {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(Splash.class);


	// ***** Static Members ************************************************* //

	//private static final String	FXML_FILE = "/MessageDialog.fxml";
	//private static final String	CSS_FILE = "/General.css";

	private static final int SPLASH_WIDTH = 400;
	private static final int SPLASH_HEIGHT = 400;



	// ***** Members ******************************************************** //

	private VBox				splashLayout;
	private ProgressBar			loadProgress;
	private Label				progressText;
	private Stage				stage;




	// ***** Constructor **************************************************** //

	public static Splash create() {
		return new Splash();
	}


	protected Splash() {
		Image image = new Image("/images/splash.png");
		ImageView splash = new ImageView(image);

		loadProgress = new ProgressBar();
		loadProgress.setPrefWidth(image.getWidth() - 20);
		loadProgress.setPrefHeight(20);

		progressText = new Label("Federn werden zurecht gezupft . . .");
		progressText.setAlignment(Pos.CENTER);
		progressText.setFont(Font.font(null, FontWeight.BOLD, 16));

		splashLayout = new VBox();
		splashLayout.setAlignment(Pos.CENTER);

		VBox infoLayout = new VBox();
		infoLayout.setAlignment(Pos.CENTER);

		splashLayout.getChildren().addAll(splash, infoLayout);
		infoLayout.getChildren().addAll(loadProgress, progressText);
		infoLayout.setStyle("-fx-padding: 5; -fx-background-color: #EEF2FF; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, #AAAACC, derive(#AAAACC, 50%));");
		splashLayout.setEffect(new DropShadow());


    	stage = new Stage();
		//stage.initModality(Modality.APPLICATION_MODAL);
    	stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Loading...");

		Scene splashScene = new Scene(splashLayout);
		splashScene.setFill(Color.TRANSPARENT);
		stage.setScene(splashScene);

//		final Rectangle2D bounds = Screen.getPrimary().getBounds();
//		stage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
//		stage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);

		SplashScreen splashScreen = SplashScreen.getSplashScreen();

		if(splashScreen != null) {
			stage.setX(splashScreen.getBounds().x);
			stage.setY(splashScreen.getBounds().y);
		} else {
			stage.centerOnScreen();
		}

		stage.show();

		if(splashScreen != null) {
			splashScreen.close();
		}
	}




	// ***** Methods ******************************************************** //

	public void finished() {
		if(stage.isShowing()) {
			loadProgress.progressProperty().unbind();
			loadProgress.setProgress(1);
			progressText.setText("Federkleid fertig");

			stage.setIconified(false);
			stage.toFront();

			FadeTransition transition = new FadeTransition(Duration.seconds(1), splashLayout);
			transition.setFromValue(1.0);
			transition.setToValue(0.0);
			transition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					stage.hide();
				}
			});
			transition.play();
		}
	}

}
