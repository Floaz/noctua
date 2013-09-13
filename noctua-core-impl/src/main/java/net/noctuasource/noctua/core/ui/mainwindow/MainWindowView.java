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
package net.noctuasource.noctua.core.ui.mainwindow;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.annotation.Resource;
import net.noctuasource.act.controller.RunLater;
import net.noctuasource.noctua.core.datastore.ProfilesContext;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.noctua.core.ExecutorIdentifiers;
import net.noctuasource.noctua.core.NoctuaInstanceUtil;
import net.noctuasource.noctua.core.impl.SignOffProfileEvent;
import net.noctuasource.noctua.core.ui.loading.LoadingScreenManager;

import org.apache.log4j.Logger;




public class MainWindowView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(MainWindowView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/MainWindowView.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	@Resource
	ProfilesContext						profilesContext;

	private Stage						stage;

	private boolean						destroyInstanceOnClose = true;


	// ***** FXML Nodes ***************************************************** //

	@FXML private Label					headerTitle;

	@FXML private MenuButton			superButton;
	@FXML private TabPane				tabPane;



	// ***** Constructor **************************************************** //

	@RunLater(executor=ExecutorIdentifiers.JAVAFX_EXECUTOR)
	public void createView() {
    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle("Test");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				onFrameClose();
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
			stage.close();
			return;
		}

        initStaticFields();
        createUnitTab();

        //stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();

		LoadingScreenManager.get().hideLoadingScreen();
	}


	@Override
	protected void onDestroy() {
		if(stage != null) {
			stage.close();
		}

		// Close noctua
		if(destroyInstanceOnClose) {
			NoctuaInstanceUtil.destroyNoctuaInstance(this);
		}
	}



	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	headerTitle.setText("Noctua");
    	//superButton.setText(profilesContext.getProfile().getData().getName());
    }


    private void createUnitTab() {
    	UnitBrowserTabView tabView = executeController(UnitBrowserTabView.class);
    	Tab tab = new Tab();
    	tab.setClosable(false);
    	tab.setText("Lektionen");
    	ImageView icon = new ImageView(
    			new Image(MainWindowView.class.getResourceAsStream(
    													"/images/Units.png")));
    	icon.setFitWidth(24);
    	icon.setFitHeight(24);
    	tab.setGraphic(icon);
    	tab.setContent(tabView.getNode());
    	tabPane.getTabs().add(tab);
    }


    @FXML
    protected void handleSettingsMenuItem(ActionEvent event) {

    }


    @FXML
    protected void handleAboutMenuItem(ActionEvent event) {
    	executeController("aboutView", ControllerParamsBuilder.create().add("stage", stage).build());
    }


    @FXML
    protected void handleSignOffMenuItem(ActionEvent e) {
		// Do not destroy instance when window was closed!
		destroyInstanceOnClose = false;

		SignOffProfileEvent event = new SignOffProfileEvent(profilesContext.getProfile());
    	postEvent(event);
    }


    @FXML
    protected void handleExitMenuItem(ActionEvent event) {
    	onFrameClose();
    }


    private void onFrameClose() {
    	destroy();
    }



	// ***** injection Setters *************************************************** //

	public void setProfilesContext(ProfilesContext profilesContext) {
		this.profilesContext = profilesContext;
	}

}
