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
package net.noctuasource.noctua.core.ui.profiles;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.annotation.Resource;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.impl.ProfileChosenEvent;
import net.noctuasource.noctua.core.impl.launcher.NoctuaInstanceUtil;
import net.noctuasource.noctua.core.ui.Splash;
import net.noctuasource.noctua.core.ui.StyleConstants;
import net.noctuasource.profiles.Observer;

import org.apache.log4j.Logger;

import net.noctuasource.profiles.Profile;
import net.noctuasource.profiles.ProfileData;
import net.noctuasource.profiles.ProfileManager;



public class ProfileChooseView extends SubContextController implements Observer {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(ProfileChooseView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/ProfileChooseView.fxml";
	private static final String	CSS_FILE = "/General.css";

	private static final double	STAGE_MIN_WIDTH = 500;
	private static final double	STAGE_MIN_HEIGHT = 450;


	// ***** Members ******************************************************** //

	@Resource
	private ProfileManager 		profileManager;

	private Stage				stage;

	private Profile				chosenProfile = null;




	// ***** FXML Nodes ***************************************************** //

	@FXML private Label					headerTitle;
	//@FXML private Label					descriptionLabel;

	@FXML private Button				startButton;
	@FXML private Button				editButton;
	@FXML private Button				deleteButton;

	@FXML private TabPane				profileTabPane;
	@FXML private Tab					profilesTab;
	@FXML private Tab					newProfileTab;
	@FXML private TextField				newProfileNameTextField;


	@FXML private ListView<String>		profilesList;
	@FXML private CheckBox				autoLoginCheckbox;



	// ***** Constructor **************************************************** //


	@Override
	protected void onCreate() {
    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle("Profilauswahl");
		stage.getIcons().add(new Image(StyleConstants.FRAME_ICON));
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
			logger.error("Error while creating view: ", e);
			//stage.close();
			return;
		}

    	profileManager.registerObserver(this);

    	newProfileNameTextField.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent arg0) {
    			updateStartButton();
    		}
		});

        initStaticFields();
        initList();

        updateEditButtons();
        updateStartButton();

        stage.sizeToScene();
        stage.setMinWidth(STAGE_MIN_WIDTH);
        stage.setMinHeight(STAGE_MIN_HEIGHT);
        stage.centerOnScreen();
        stage.show();

		Splash splashScreen = getControllerData().get(Splash.class);
		splashScreen.finished();
	}


	@Override
    protected void onDestroy() {
		stage.close();

    	profileManager.removeObserver(this);

		if(chosenProfile != null) {
			postEvent(new ProfileChosenEvent(chosenProfile));
		} else {
			NoctuaInstanceUtil.destroyNoctuaInstance(this);
		}
    }




	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	headerTitle.setText("Test");
    	/// TODO Add other static labels.
    }


    private void initList() {
    	profilesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    	updateData();

    	Callback<ListView<String>, ListCell<String>> cellFactory =
    					new Callback<ListView<String>, ListCell<String>>() {
    	    @Override
    	    public ListCell<String> call(ListView<String> listView) {
                return new ListCell<String>() {
                    private final Image image;

                    {
                        setContentDisplay(ContentDisplay.LEFT);
                        image = new Image(ProfileChooseView.class.getResourceAsStream("/images/User.png"));
                    }

                    @Override protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(new ImageView(image));
                            setText(item);
                        }
                   }
                };
    	    }
    	};

    	profilesList.setCellFactory(cellFactory);

    }


    private Profile getCurrentSelectedProfile() {
    	if(profilesList.getSelectionModel().isEmpty()) {
    		return null;
    	}

    	String profileName = profilesList.getSelectionModel().getSelectedItem();
    	for(Profile profile : profileManager.getAllProfiles()) {
    		if(profileName.equals(profile.getData().getName())) {
    			return profile;
    		}
    	}

    	return null;
    }


    private Profile createProfile() {
		String newProfileName = newProfileNameTextField.getText().trim();
		if(newProfileName.isEmpty()) {
			return null;
		}

    	try {
        	ProfileData data = new ProfileData(newProfileName);
        	return profileManager.addProfile(data);
    	} catch(Exception e) {
    		logger.warn("Could not create profile!", e);
    		return null;
    	}
    }


    private void performSelection() {
    	Profile profile;

    	if(profileTabPane.getSelectionModel().getSelectedItem().equals(profilesTab)) {
    		profile = getCurrentSelectedProfile();
    	} else {
    		profile = createProfile();
    	}

    	if (profile == null) {
    		return;
    	}

    	logger.info("Selected profile: " + profile.getData().getName());

    	if(autoLoginCheckbox.isSelected()) {
    		profileManager.setDefaultProfile(profile);
    	}

		chosenProfile = profile;
		destroy();
    }



    @FXML
    protected void handleTextFieldChanged(KeyEvent event) {
    	updateStartButton();
    }


    @FXML
    protected void handleTextFieldReturnAction(ActionEvent event) {
    	performSelection();
    }


    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
    	performSelection();
    }


    @FXML
    protected void handleEditButtonAction(ActionEvent event) {
    	Profile profile = getCurrentSelectedProfile();
    	if(profile == null) {
    		return;
    	}

    	ProfileEditView.createAndShow(stage, profileManager, profile);
    }

    @FXML
    protected void handleDeleteButtonAction(ActionEvent event) {
	    Profile profile = getCurrentSelectedProfile();
		if(profile == null) {
			return;
		}

		ProfileDeleteDialogView.createAndShow(stage, profileManager, profile);
    }


    @FXML
    protected void handleTabPaneSelectionChanged(Event event) {
    	updateStartButton();
    }


    @FXML
    protected void handleListViewMouseClickChange(MouseEvent event) {
    	updateEditButtons();
    	updateStartButton();
    }


    @FXML
    protected void handleListViewKeyPressed(KeyEvent event) {
    	updateEditButtons();
    	updateStartButton();
    }


    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	destroy();
    }


    private void updateEditButtons() {
    	boolean disable =
    		profilesList.getSelectionModel().getSelectedIndices().size() == 0;

    	editButton.setDisable(disable);
    	deleteButton.setDisable(disable);
    }


    private void updateStartButton() {
    	if(startButton == null) {
    		return;
    	}

    	boolean disable =
    		(profileTabPane.getSelectionModel().getSelectedItem().equals(profilesTab)
        		&& profilesList.getSelectionModel().getSelectedIndices().size() == 0)
    		|| (profileTabPane.getSelectionModel().getSelectedItem().equals(newProfileTab)
    			&& newProfileNameTextField.getText().trim().isEmpty());

    	startButton.setDisable(disable);
    }


	@Override
	public void updateData() {
    	ObservableList<String> data = FXCollections.observableArrayList();
    	for(Profile profile : profileManager.getAllProfiles()) {
    		data.add(profile.getData().getName());
    	}

    	FXCollections.sort(data);

    	profilesList.setItems(data);
    	profilesList.getSelectionModel().clearSelection();

    	updateEditButtons();
    	updateStartButton();

    	if(data.size() <= 0) {
    		profileTabPane.getSelectionModel().select(newProfileTab);
    		profilesTab.setDisable(true);
    	}
	}



	public void setProfileManager(ProfileManager profileManager) {
		this.profileManager = profileManager;
	}


}
