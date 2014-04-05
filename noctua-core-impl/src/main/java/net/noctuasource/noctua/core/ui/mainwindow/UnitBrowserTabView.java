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

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javax.annotation.Resource;
import javax.inject.Inject;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.TreeNodeManagerBo;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.events.TreeNodeEvent;
import net.noctuasource.noctua.core.business.GroupList;
import net.noctuasource.noctua.core.test.impl.TestTypes;



public class UnitBrowserTabView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(UnitBrowserTabView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/UnitBrowserTabView.fxml";



	// ***** Members ******************************************************** //

	@Resource
	EventBus						eventBus;

	@Inject
	TreeNodeManagerBo				treeNodeManagerBo;

	private Node					node;

	private TreeView<TreeNodeDto>	treeView;




	// ***** FXML Nodes ***************************************************** //

	@FXML private Button				normalTestButton;
	@FXML private Button				mcTestButton;
	@FXML private Button				schoolTestButton;
	@FXML private Button				newUnitButton;
	@FXML private Button				newFolderButton;
	@FXML private Button				newLanguageButton;
	@FXML private Button				renameButton;
	@FXML private Button				moveButton;
	@FXML private Button				deleteButton;
	@FXML private Button				openButton;
	@FXML private CheckBox				multipleSelectionCheckbox;
	@FXML private VBox					treeViewBox;



	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		eventBus.register(this);

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
        initTreeView();

        updateButtons();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
//    	normalTestButton.setAlignment(Pos.CENTER_LEFT);
//    	mcTestButton.setAlignment(Pos.CENTER_LEFT);
//    	schoolTestButton.setAlignment(Pos.CENTER_LEFT);
//    	newUnitButton.setAlignment(Pos.CENTER_LEFT);
//    	newFolderButton.setAlignment(Pos.CENTER_LEFT);
//    	newLanguageButton.setAlignment(Pos.CENTER_LEFT);
//    	renameButton.setAlignment(Pos.CENTER_LEFT);
//    	deleteButton.setAlignment(Pos.CENTER_LEFT);
    }


    private void initTreeView() {
    	treeView = new TreeView<>();

		treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				handleMouseClicked(t);
			}
		});

    	treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<TreeNodeDto>>() {
			@Override
			public void changed(
					ObservableValue<? extends TreeItem<TreeNodeDto>> observableValue,
					TreeItem<TreeNodeDto> oldItem, TreeItem<TreeNodeDto> newItem) {
				updateButtons();
			}
		});

    	treeViewBox.getChildren().add(treeView);

    	updateTreeView();
    }


	@Subscribe
    public void updateTreeView(TreeNodeEvent event) {
    	updateTreeView();
    }


    private void updateTreeView() {
    	TreeItem<TreeNodeDto> item = new ObjectTreeViewItem(treeNodeManagerBo);
    	treeView.setRoot(item);
    	treeView.setShowRoot(false);

    	treeView.getSelectionModel().clearSelection();
    }


    public GroupList getSelectedFlashCardGroups() {
    	List<TreeItem<TreeNodeDto>> items = treeView.getSelectionModel().getSelectedItems();

		if(items.size() <= 0) {
			return null;
		}

		GroupList groups = new GroupList();

		for(TreeItem<TreeNodeDto> item : items) {
			TreeNodeDto node = item.getValue();
			if(node.getType().equals("FlashCardGroup")) {
				groups.add(node);
			}
		}

		return groups;
    }


    public void performOpen() {
		if(treeView.getSelectionModel().getSelectedItems().size() != 1) {
			logger.debug("Ignore open button, count selected != 1.");
			return;
		}

    	TreeItem<TreeNodeDto> item = treeView.getSelectionModel().getSelectedItem();

		executeController("unitMenuView", ControllerParamsBuilder.create()
																.add("treeNode", item.getValue())
																.add("parentWindow", null).build());
    }



    public void handleMouseClicked(MouseEvent event) {
    	if(event.getClickCount() == 2) {
			performOpen();
		}
    }


    @FXML
    protected void handleNormalTestButtonAction(ActionEvent event) {
		GroupList groups = getSelectedFlashCardGroups();
		if(groups == null) {
			return;
		}

    	executeController("testInit", ControllerParamsBuilder.create().add("testType", TestTypes.NORMAL_TEST)
																.add("groupList", groups)
																.add("parentWindow", null).build());
    }

    @FXML
    protected void handleMCTestButtonAction(ActionEvent event) {
		GroupList groups = getSelectedFlashCardGroups();
		if(groups == null) {
			return;
		}

    	executeController("testInit", ControllerParamsBuilder.create().add("testType", TestTypes.MULTIPLE_CHOICE_TEST)
																.add("groupList", groups)
																.add("parentWindow", null).build());
    }

    @FXML
    protected void handleSchoolTestButtonAction(ActionEvent event) {
		GroupList groups = getSelectedFlashCardGroups();
		if(groups == null) {
			return;
		}

    	executeController("testInit", ControllerParamsBuilder.create().add("testType", TestTypes.SCHOOL_TEST)
																.add("groupList", groups)
																.add("parentWindow", null).build());
    }


    @FXML
    protected void handleNewUnitButtonAction(ActionEvent event) {
    	TreeNodeDto currentNode = getSelectedNode();
    	if(currentNode == null) {
    		return;
    	}

    	executeController("addFlashCardGroupView", ControllerParamsBuilder.create()
																.add("treeNode", currentNode)
																.add("parentWindow", null).build());
    }


    @FXML
    protected void handleNewFolderButtonAction(ActionEvent event) {
    	TreeNodeDto currentNode = getSelectedNode();
    	if(currentNode == null) {
    		return;
    	}

    	executeController("addFolderView", ControllerParamsBuilder.create()
																.add("treeNode", currentNode)
																.add("parentWindow", null).build());
    }


    @FXML
    protected void handleNewLanguageButtonAction(ActionEvent event) {
    	executeController("addLanguageView", ControllerParamsBuilder.create()
																.add("parentWindow", null).build());
    }


    @FXML
    protected void handleRenameButtonAction(ActionEvent event) {
    	TreeNodeDto currentNode = getSelectedNode();
    	if(currentNode == null) {
    		return;
    	}

    	executeController("renameTreeNodeView", ControllerParamsBuilder.create()
																.add("treeNode", currentNode)
																.add("parentWindow", null).build());
    }


    @FXML
    protected void handleMoveButtonAction(ActionEvent event) {
    	TreeNodeDto currentNode = getSelectedNode();
    	if(currentNode == null) {
    		return;
    	}

    	executeController("moveTreeNodeView", ControllerParamsBuilder.create()
																.add("treeNode", currentNode)
																.add("parentWindow", null).build());
    }


    @FXML
    protected void handleDeleteButtonAction(ActionEvent event) {
    	TreeNodeDto currentNode = getSelectedNode();
    	if(currentNode == null) {
    		return;
    	}

    	executeController("deleteTreeNodeView", ControllerParamsBuilder.create()
																.add("treeNode", currentNode)
																.add("parentWindow", null).build());
    }


    @FXML
    protected void handleOpenButtonAction(ActionEvent event) {
    	performOpen();
    }


    @FXML
    protected void handleMultipleSelectionCheckbox(ActionEvent event) {
    	updateTreeViewSelection();
    }


    protected TreeNodeDto getSelectedNode() {
    	TreeItem<TreeNodeDto> item = treeView.getSelectionModel().getSelectedItem();
    	if(item != null) {
    		return item.getValue();
    	}
    	return null;
    }


    protected void updateTreeViewSelection() {
    	treeView.getSelectionModel().setSelectionMode(
    								multipleSelectionCheckbox.isSelected()
    								? SelectionMode.MULTIPLE
    								: SelectionMode.SINGLE);
    }


    protected void updateButtons() {
    	boolean selected = false;
    	boolean multipleSelected = false;
    	boolean languageSelected = false;
    	boolean groupSelected = false;

    	List<TreeItem<TreeNodeDto>> items = treeView.getSelectionModel().getSelectedItems();
    	if(items.size() == 1) {
    		selected = true;
    		TreeItem<TreeNodeDto> item = items.iterator().next();
    		if(item != null && item.getValue() != null && item.getValue().getType().equals("FlashCardGroup")) {
    			groupSelected = true;
    		}
    		if(item != null && item.getValue() != null && item.getValue().getType().equals("Language")) {
    			languageSelected = true;
    		}
    	} else if(!items.isEmpty()) {
    		selected = true;
    		multipleSelected = true;
			languageSelected = true;
    		groupSelected = true;
    		for(TreeItem<TreeNodeDto> item : items) {
        		if(!(item.getValue().getType().equals("FlashCardGroup"))) {
        			groupSelected = false;
        		}
        		if(!(item.getValue().getType().equals("Language"))) {
        			languageSelected = false;
        		}
    		}
    	}

    	normalTestButton.setDisable(!groupSelected);
    	mcTestButton.setDisable(!groupSelected);
    	schoolTestButton.setDisable(!groupSelected);
    	newUnitButton.setDisable(!selected || !(!multipleSelected && !groupSelected));
    	newFolderButton.setDisable(!selected || !(!multipleSelected && !groupSelected));
    	newLanguageButton.setDisable(false);
    	renameButton.setDisable(!selected || multipleSelected);
		moveButton.setDisable(!selected || multipleSelected || languageSelected);
    	deleteButton.setDisable(!selected || multipleSelected);
    	openButton.setDisable(!groupSelected);
    }




    public Node getNode() {
    	return node;
    }

	@Override
	protected void onDestroy() {
		eventBus.unregister(this);
	}





	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setTreeNodeManagerBo(TreeNodeManagerBo treeNodeManagerBo) {
		this.treeNodeManagerBo = treeNodeManagerBo;
	}

}
