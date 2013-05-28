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

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.noctuasource.act.controller.SubContextController;

import org.apache.log4j.Logger;




public class AboutView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(AboutView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/AboutView.fxml";
	private static final String	CSS_FILE = "/General.css";

	private static final String	CREDITS_FILE = "/CREDITS.txt";
	private static final String	HISTORY_FILE = "/HISTORY.txt";

	private static final String	DOCUMENTATION_URL = "http://www.noctuasource.net/doku/";
	private static final String	FORUM_URL = "http://www.noctuasource.net/forum/";
	private static final String	HOMEPAGE_URL = "http://www.noctuasource.net";
	private static final String	CONTACT_URL = "http://www.noctuasource.net/kontakt/";
	private static final String	DONATE_URL = "http://www.noctuasource.net/spenden/";


	// ***** Members ******************************************************** //

	private Stage				stage;



	// ***** FXML Nodes ***************************************************** //

	@FXML private Tab						bundlesTab;
	@FXML private ListView					bundlesList;
	@FXML private TextArea					bundleLicenseTextArea;
	@FXML private Label						bundleNameLabel;
	@FXML private Label						bundleVendorLabel;
	@FXML private Label						bundleVersionLabel;

	@FXML private WebView					creditsView;
	@FXML private WebView					historyView;




	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		Stage parentStage = getControllerParams().get("stage", Stage.class);

    	VBox root = new VBox();

    	stage = new Stage();
    	stage.initModality(Modality.WINDOW_MODAL);
    	stage.initOwner(parentStage);
        stage.setTitle("Über Noctua");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				onClose();
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

        bundlesTab.getTabPane().getTabs().remove(bundlesTab);

        //stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	creditsView.getEngine().loadContent(readStreamToString(CREDITS_FILE), "text/html");
    	historyView.getEngine().loadContent(readStreamToString(HISTORY_FILE), "text/plain");
    }


    private static String readStreamToString(String file) {
        try {
            return readFileAsString(file);
        } catch (Exception e) {
            throw new RuntimeException("Error: ", e);
        }
    }

    private static String readFileAsString(String file) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
		try (	InputStream stream = AboutView.class.getResourceAsStream(file);
				InputStreamReader isr = new InputStreamReader(stream);
				BufferedReader reader = new BufferedReader(isr)) {
			char[] buf = new char[1024];
			int numRead;
			while((numRead=reader.read(buf)) != -1){
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		}
        return fileData.toString();
    }


    @FXML
    protected void handleDocumentationButton(ActionEvent event) {
    	try {
    		Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(DOCUMENTATION_URL));
		} catch (URISyntaxException | IOException e) {
			logger.warn("Exception while pressing donate: ", e);
		}
    }


    @FXML
    protected void handleForumButton(ActionEvent event) {
    	try {
    		Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(FORUM_URL));
		} catch (URISyntaxException | IOException e) {
			logger.warn("Exception while pressing donate: ", e);
		}
    }


    @FXML
    protected void handleHomepageButton(ActionEvent event) {
    	try {
    		Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(HOMEPAGE_URL));
		} catch (URISyntaxException | IOException e) {
			logger.warn("Exception while pressing donate: ", e);
		}
    }


    @FXML
    protected void handleContactButton(ActionEvent event) {
    	try {
    		Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(CONTACT_URL));
		} catch (URISyntaxException | IOException e) {
			logger.warn("Exception while pressing donate: ", e);
		}
    }


    @FXML
    protected void handleDonateButton(ActionEvent event) {
    	try {
    		Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(DONATE_URL));
		} catch (URISyntaxException | IOException e) {
			logger.warn("Exception while pressing donate: ", e);
		}
    }


    @FXML
    protected void handleCloseButton(ActionEvent event) {
    	onClose();
    }


    private void updateBundleInfo() {

    }


    private void onClose() {
		destroy();
    }


	@Override
	protected void onDestroy() {
    	stage.close();
	}
}
