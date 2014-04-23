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
package net.noctuasource.noctua.core.ui.editor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import net.noctuasource.act.annotation.ControllerContext;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.noctua.core.business.FlashCardManagerBo;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.noctua.core.business.add.FlashCardGroupDto;
import net.noctuasource.noctua.core.dto.EditorEntry;
import net.noctuasource.noctua.core.events.FlashCardEvent;
import org.apache.log4j.Logger;





public class EditorView {

	// ***** Basic Static Members ******************************************* //

	private static final Logger logger = Logger.getLogger(EditorView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/EditorView.fxml";
	private static final String	CSS_FILE = "/General.css";
	private static final String	WINDOW_TITLE = "Lektionseditor";


	// ***** Members ******************************************************** //

	@ControllerContext
	private ContextController			contextController;

	@Resource
	private FlashCardManagerBo			flashCardBo;

	@Resource
	EventBus					eventBus;

	private Stage				stage;

	private FlashCardGroupDto	flashCardGroup;

	private TableColumn<EditorEntry,String> firstColumn;


	// ***** FXML Nodes ***************************************************** //

	@FXML private Button						addButton;
	@FXML private Button						moveButton;
	@FXML private Button						deleteButton;
	@FXML private TableView<EditorEntry>		table;

	@FXML private HBox							vocableEditPanel;

	@FXML private TextField						vocableTextField;
	@FXML private TextField						translation1TextField;
	@FXML private TextField						translation2TextField;
	@FXML private TextField						translation3TextField;
	@FXML private TextField						sentenceTextField;
	@FXML private TextField						sentenceTranslationTextField;
	@FXML private TextField						addInfoTextField;
	@FXML private TextField						tipTextField;
	@FXML private ChoiceBox						genderChoiceBox;
	@FXML private ChoiceBox						partOfSpeechChoiceBox;


	// ***** Constructor **************************************************** //

	@PostConstruct
	public void onCreate() {
		//TreeNodeDto treeNode = contextController.getControllerParams().get("treeNode", TreeNodeDto.class);
		flashCardGroup = new FlashCardGroupDto();
		//flashCardGroup.setId(treeNode.getId());
		//flashCardGroup.setName(treeNode.getName());

    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle(WINDOW_TITLE);
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
			logger.error("Error while creating result view: ", e);
			stage.close();
		}

        initStaticFields();
        initVocabularyTable();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();

		updateVocabularyTable();
		updateButtons();

		//eventBus.register(this);
	}


	@PreDestroy
	protected void onDestroy() {
		stage.close();
		eventBus.unregister(this);
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	//headerTitle.setText("Testergebnis");
    }


	private void initVocabularyTable() {
//		Callback<TableColumn<EditorEntry,String>, TableCell<EditorEntry,String>> cellFactory =
//             new Callback<TableColumn<EditorEntry,String>, TableCell<EditorEntry,String>>() {
//                 public TableCell call(TableColumn<EditorEntry,String> p) {
//                    return new EditingCell();
//                 }
//             };

    	table.setEditable(false);

        TableColumn<EditorEntry,String> vocableCol = new TableColumn<>("Vokabel");
		vocableCol.setCellValueFactory(new PropertyValueFactory<>("vocable"));
		//vocableCol.setCellFactory(cellFactory);
		firstColumn = vocableCol;
        TableColumn native1Col = new TableColumn("Übersetzung 1");
		native1Col.setCellValueFactory(new PropertyValueFactory<>("native1"));
        TableColumn native2Col = new TableColumn("Übersetzung 2");
		native2Col.setCellValueFactory(new PropertyValueFactory<>("native2"));
        TableColumn native3Col = new TableColumn("Übersetzung 3");
		native3Col.setCellValueFactory(new PropertyValueFactory<>("native3"));
        TableColumn exampleCol = new TableColumn("Beispielsatz");
		exampleCol.setCellValueFactory(new PropertyValueFactory<>("example"));
        TableColumn exampleTranslationCol = new TableColumn("Beispielsatz Übersetzung");
		exampleTranslationCol.setCellValueFactory(new PropertyValueFactory<>("exampleTranslation"));
        TableColumn tipCol = new TableColumn("Tip");
		tipCol.setCellValueFactory(new PropertyValueFactory<>("tip"));
        TableColumn infoCol = new TableColumn("Zusatzinfo");
		infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
        TableColumn genderCol = new TableColumn("Genius");
		genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn partOfSpeechCol = new TableColumn("Wortart");
		partOfSpeechCol.setCellValueFactory(new PropertyValueFactory<>("partOfSpeech"));

		vocableCol.setPrefWidth(200);
		native1Col.setPrefWidth(150);
		native2Col.setPrefWidth(100);
		native3Col.setPrefWidth(100);
		exampleCol.setPrefWidth(200);

        table.getColumns().addAll(vocableCol, native1Col, native2Col, native3Col, exampleCol, exampleTranslationCol);
        table.getColumns().addAll(tipCol, infoCol, genderCol, partOfSpeechCol);

		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		table.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Integer> change) {
				updateButtons();
				onSelectionChanged();
			}
		});


		EditorEntry entry;
		entry= new EditorEntry();
		entry.setVocable("to eat");
		entry.setNative1("essen");
		table.getItems().add(entry);
		entry= new EditorEntry();
		entry.setVocable("mouse");
		entry.setNative1("die Maus");
		table.getItems().add(entry);
		entry= new EditorEntry();
		entry.setVocable("car");
		entry.setNative1("das Auto");
		table.getItems().add(entry);
    }


	@Subscribe
    public void updateVocabularyTable(FlashCardEvent event) {
        updateVocabularyTable();
	}


    private void updateVocabularyTable() {
        ObservableList<?> data = FXCollections.observableArrayList();
//        data.addAll(flashCardBo.getVocabularyOfFlashCardGroup(flashCardGroup.getId()));
//    	vocabularyTable.setItems(data);
	}


    private void updateButtons() {
		ObservableList<?> items = table.getSelectionModel().getSelectedItems();
        //addButton.setDisable(false);
		//editButton.setDisable(items.size() != 1);
		moveButton.setDisable(items.size() <= 0);
		deleteButton.setDisable(items.size() <= 0);
	}


    private void onSelectionChanged() {
		ObservableList items = table.getSelectionModel().getSelectedItems();
		vocableEditPanel.setVisible(items.size() == 1);
		if(items.size() == 1) {
			EditorEntry entry = (EditorEntry) items.get(0);
			vocableTextField.textProperty().unbind();
			vocableTextField.textProperty().bind(entry.vocableProperty());
		}
	}


    private List<String> getSelectedIds() {
		ObservableList<?> items = table.getSelectionModel().getSelectedItems();
        List<String> vocableIds = FXCollections.observableArrayList();
//
//		for(VocableListElement element : items) {
//			vocableIds.add(element.getId());
//		}

		return vocableIds;
	}


    @FXML
    protected void handleAddButtonAction(ActionEvent event) {
		EditorEntry entry = new EditorEntry();
		table.getItems().add(entry);
		table.getSelectionModel().clearAndSelect(table.getItems().size()-1);
		vocableTextField.requestFocus();
		//Platform.runLater(() -> { table.edit(5, table.getColumns().get(0)); });
    }


    @FXML
    protected void handleMoveButtonAction(ActionEvent event) {
    	List<String> vocableIds = getSelectedIds();
    	if(vocableIds.isEmpty()) {
    		return;
    	}

    	contextController.createController("moveVocabularyView", ControllerParamsBuilder.create()
																.add("flashCardIds", vocableIds)
																.add("parentWindow", stage).build());
    }


    @FXML
    protected void handleDeleteButtonAction(ActionEvent event) {
    	List<String> vocableIds = getSelectedIds();
    	if(vocableIds.isEmpty()) {
    		return;
    	}

    	contextController.createController("deleteVocabularyView", ControllerParamsBuilder.create()
																.add("vocableIds", vocableIds)
																.add("parentWindow", stage).build());
    }


    @FXML
    protected void handleSaveButtonAction(ActionEvent event) {
    }


    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	contextController.destroy();
    }


    @FXML
    protected void onVocableTextFieldAction(ActionEvent event) {
		translation1TextField.requestFocus();
    }


    @FXML
    protected void onTranslation1TextFieldAction(ActionEvent event) {
		if(translation1TextField.getText().trim().isEmpty()) {
			return;
		}

		translation2TextField.requestFocus();
    }


    @FXML
    protected void onTranslation2TextFieldAction(ActionEvent event) {
		if(translation2TextField.getText().trim().isEmpty()) {
			return;
		}

		translation3TextField.requestFocus();
    }


    @FXML
    protected void onTranslation3TextFieldAction(ActionEvent event) {
		if(translation3TextField.getText().trim().isEmpty()) {
			return;
		}
    }


	public void setContextController(ContextController contextController) {
		this.contextController = contextController;
	}


	public void setFlashCardBo(FlashCardManagerBo flashCardBo) {
		this.flashCardBo = flashCardBo;
	}


	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

//
//	class EditingCell extends TableCell<EditorEntry, String> {
//
//		private TextField textField;
//
//
//		public EditingCell() {
//		}
//
//
//		@Override
//		public void startEdit() {
//			if(!isEmpty()) {
//				super.startEdit();
//				if (textField == null) {
//					createTextField();
//				}
//				setText(null);
//				setGraphic(textField);
//				textField.selectAll();
//				textField.requestFocus();
//			}
//		}
//
//
//		@Override
//		public void cancelEdit() {
//			super.cancelEdit();
//
//			setText((String) getItem());
//			setGraphic(null);
//		}
//
//
//		@Override
//		public void updateItem(String item, boolean empty) {
//			super.updateItem(item, empty);
//
//			if(empty) {
//				setText(null);
//				setGraphic(null);
//			} else {
//				if(isEditing()) {
//					if(textField != null) {
//						textField.setText(getString());
//					}
//					setText(null);
//					setGraphic(textField);
//				} else {
//					setText(getString());
//					setGraphic(null);
//				}
//			}
//		}
//
//
//		private void createTextField() {
//			textField = new TextField(getString());
//			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
//			textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
//				@Override
//				public void changed(ObservableValue<? extends Boolean> arg0,
//						Boolean arg1, Boolean arg2) {
//					if(!arg2) {
//						commitEdit(textField.getText());
//					}
//				}
//			});
//			textField.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent event) {
//					commitEdit(textField.getText());
//				}
//			});
//		}
//
//
//		private String getString() {
//			return getItem() == null ? "" : getItem().toString();
//		}
//
//	}
}
