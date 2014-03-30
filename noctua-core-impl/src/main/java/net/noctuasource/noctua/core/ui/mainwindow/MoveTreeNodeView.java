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

import net.noctuasource.noctua.core.ui.vocable.*;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.inject.Inject;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.TreeNodeManagerBo;

import org.apache.log4j.Logger;





public class MoveTreeNodeView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(MoveVocabularyView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/MoveVocabulary.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	@Inject
	TreeNodeManagerBo		treeNodeManagerBo;


	private Stage			stage;

	private TreeNodeDto		currentTreeNode;

	private UnitTreeView	treeView;


	// ***** FXML Nodes ***************************************************** //

	@FXML private Button		moveButton;

	@FXML private VBox			treeViewBox;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		this.currentTreeNode = getControllerParams().getOrThrow("treeNode", TreeNodeDto.class);

		Window parentWindow = getControllerParams().get("parentWindow", Window.class);

    	VBox root = new VBox();

    	stage = new Stage();
    	stage.initModality(Modality.WINDOW_MODAL);
    	stage.initOwner(parentWindow);
        stage.setTitle("Verschieben...");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        stage.setScene(scene);

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

        initStaticFields();
        initTree();
		updateButton();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	//headerTitle.setText("Testergebnis");
    }


    private void initTree() {
    	treeView = executeController(UnitTreeView.class);
		treeView.addFilter(new ObjectTreeViewItem.Filter() {
			@Override
			public boolean filter(TreeNodeDto node) {
				return !(node.getType().equals("Folder")) && !(node.getType().equals("Language"));
			}
		});
		treeView.addFilter(new ObjectTreeViewItem.Filter() {
			@Override
			public boolean filter(TreeNodeDto node) {
				return node.getId().equals(currentTreeNode.getId());
			}
		});

    	treeView.getTreeView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<TreeNodeDto>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<TreeNodeDto>> observableValue,
											TreeItem<TreeNodeDto> oldItem, TreeItem<TreeNodeDto> newItem) {
				updateButton();
			}
		});

		treeViewBox.getChildren().add(treeView.getTreeView());

		treeView.updateTreeView();
    }


	protected void updateButton() {
		TreeNodeDto treeNode = treeView.getSelectedNode();
		moveButton.setDisable(treeNode == null);
	}


    @FXML
    protected void handleMoveButtonAction(ActionEvent event) {
		TreeNodeDto treeNode = treeView.getSelectedNode();
		if(treeNode == null) {
			return;
		}

		treeNodeManagerBo.moveTreeNode(currentTreeNode, treeNode);

		destroy();
    }


    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	destroy();
    }


	@Override
	protected void onDestroy() {
		stage.close();
	}

	public void setTreeNodeManagerBo(TreeNodeManagerBo treeNodeManagerBo) {
		this.treeNodeManagerBo = treeNodeManagerBo;
	}

}
