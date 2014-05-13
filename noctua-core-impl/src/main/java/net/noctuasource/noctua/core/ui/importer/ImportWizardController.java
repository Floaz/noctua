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
package net.noctuasource.noctua.core.ui.importer;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import net.noctuasource.act.annotation.ControllerContext;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.VocabularyManagerBo;
import net.noctuasource.noctua.core.business.add.FlashCardGroupDto;
import net.noctuasource.noctua.core.dto.EditorEntry;
import org.apache.log4j.Logger;




/**
 * View controller for importing vocabulary.
 * @author Philipp Thomas
 */
public class ImportWizardController {

	// ***** Basic Static Members ******************************************* //

	private static final Logger logger = Logger.getLogger(ImportWizardController.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/WizardDialog.fxml";
	private static final String	CSS_FILE = "/General.css";
	private static final String	WINDOW_TITLE = "Importer";


	// ***** Members ******************************************************** //

	@ControllerContext
	private ContextController			contextController;

	@Resource
	private VocabularyManagerBo			vocabularyManagerBo;

	private Stage						stage;

	private FlashCardGroupDto			flashCardGroup;

	private ObservableList<EditorEntry>	entries;

	private WizardPane					wizardPane;


	// ***** FXML Nodes ***************************************************** //

	@FXML private VBox							wizardPagesPanel;

	@FXML private Button						nextButton;
	@FXML private Button						backButton;
	@FXML private Button						cancelButton;




	// ***** Inject setters ***************************************************** //

	public void setContextController(ContextController contextController) {
		this.contextController = contextController;
	}


	public void setVocabularyManagerBo(VocabularyManagerBo vocabularyManagerBo) {
		this.vocabularyManagerBo = vocabularyManagerBo;
	}



	// ***** Constructor **************************************************** //

	@PostConstruct
	public void onCreate() {
		TreeNodeDto treeNode = contextController.getControllerParams().get("treeNode", TreeNodeDto.class);
		flashCardGroup = new FlashCardGroupDto();
		flashCardGroup.setId(treeNode.getId());
		flashCardGroup.setName(treeNode.getName());

    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle(WINDOW_TITLE);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        stage.setScene(scene);
		stage.setOnCloseRequest((WindowEvent event) -> { contextController.destroy(); });

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
			stage.close();
		}

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();

		initWizardPane();
		updateButtons();
	}


	@PreDestroy
	public void onDestroy() {
		stage.close();
	}


	// ***** Methods ******************************************************** //


    private void initWizardPane() {
		wizardPane = new WizardPane();
		wizardPagesPanel.getChildren().add(wizardPane);

		ImportWizardModePage modePage = new ImportWizardModePage();
		modePage.init();
		wizardPane.getChildren().add(modePage);
	}


    private void updateButtons() {
		//nextButton.setDisable(items.size() <= 0);
		//backButton.setDisable(items.size() <= 0);
	}



	// ***** Handlers ***************************************************** //

    @FXML
    protected void handleNextButtonAction(ActionEvent event) {
    }


    @FXML
    protected void handleBackButtonAction(ActionEvent event) {
    }


    @FXML
    protected void handleCancelButtonAction(ActionEvent event) {
		contextController.destroy();
    }

}
