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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import net.noctuasource.act.annotation.ControllerContext;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.VocabularyManagerBo;
import net.noctuasource.noctua.core.business.add.FlashCardGroupDto;
import net.noctuasource.noctua.core.dto.EditorEntry;
import net.noctuasource.noctua.core.model.Gender;
import net.noctuasource.noctua.core.model.PartOfSpeech;
import net.noctuasource.noctua.core.ui.vocable.GenderMap;
import net.noctuasource.noctua.core.ui.vocable.PartOfSpeechMap;
import org.apache.log4j.Logger;




/**
 * View controller for listing and editing the vocabulary.
 * @author Philipp Thomas
 */
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
	private VocabularyManagerBo			vocabularyManagerBo;

	private Stage						stage;

	private FlashCardGroupDto			flashCardGroup;

	private ObservableList<EditorEntry>	entries;

	private ObservableList<EditorEntry>	removedEntries = FXCollections.observableArrayList();

	private EditorEntry					currentSelectedEntry = null;

	private final GenderMap				genderMap = new GenderMap();
	private final PartOfSpeechMap		partOfSpeechMap = new PartOfSpeechMap();


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
			logger.error("Error while creating result view: ", e);
			stage.close();
		}

        initStaticFields();
		initChoiceBoxes();
        initVocabularyTable();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();

		handleTableSelectionChanged();
		updateButtons();
	}


	@PreDestroy
	public void onDestroy() {
		stage.close();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	//headerTitle.setText("Testergebnis");
    }


    private void initChoiceBoxes() {
    	genderChoiceBox.setItems(FXCollections.observableArrayList(genderMap.getGenderStrings()));
    	partOfSpeechChoiceBox.setItems(FXCollections.observableArrayList(partOfSpeechMap.getPartOfSpeechStrings()));
    }


	private void initVocabularyTable() {
		Callback<TableColumn<EditorEntry,String>, TableCell<EditorEntry,String>> cellFactory =
             new Callback<TableColumn<EditorEntry,String>, TableCell<EditorEntry,String>>() {
                 public TableCell call(TableColumn<EditorEntry,String> p) {
                    return new EditingCell();
                 }
             };

		Callback<TableColumn<EditorEntry,Integer>, TableCell<EditorEntry,Integer>> genderCellFactory =
             new Callback<TableColumn<EditorEntry,Integer>, TableCell<EditorEntry,Integer>>() {
                 public TableCell call(TableColumn<EditorEntry,Integer> p) {
                    return new GenderCell();
                 }
             };

		Callback<TableColumn<EditorEntry,Integer>, TableCell<EditorEntry,Integer>> partOfSpeechCellFactory =
             new Callback<TableColumn<EditorEntry,Integer>, TableCell<EditorEntry,Integer>>() {
                 public TableCell call(TableColumn<EditorEntry,Integer> p) {
                    return new PartOfSpeechCell();
                 }
             };

    	table.setEditable(true);

        TableColumn<EditorEntry,String> vocableCol = new TableColumn<>("Vokabel");
		vocableCol.setCellValueFactory(new PropertyValueFactory<>("vocable"));
		vocableCol.setCellFactory(cellFactory);
        TableColumn native1Col = new TableColumn("Übersetzung 1");
		native1Col.setCellValueFactory(new PropertyValueFactory<>("native1"));
		native1Col.setCellFactory(cellFactory);
        TableColumn native2Col = new TableColumn("Übersetzung 2");
		native2Col.setCellValueFactory(new PropertyValueFactory<>("native2"));
		native2Col.setCellFactory(cellFactory);
        TableColumn native3Col = new TableColumn("Übersetzung 3");
		native3Col.setCellValueFactory(new PropertyValueFactory<>("native3"));
		native3Col.setCellFactory(cellFactory);
        TableColumn exampleCol = new TableColumn("Beispielsatz");
		exampleCol.setCellValueFactory(new PropertyValueFactory<>("example"));
		exampleCol.setCellFactory(cellFactory);
        TableColumn exampleTranslationCol = new TableColumn("Beispielsatz Übersetzung");
		exampleTranslationCol.setCellValueFactory(new PropertyValueFactory<>("exampleTranslation"));
		exampleTranslationCol.setCellFactory(cellFactory);
        TableColumn tipCol = new TableColumn("Tip");
		tipCol.setCellValueFactory(new PropertyValueFactory<>("tip"));
		tipCol.setCellFactory(cellFactory);
        TableColumn infoCol = new TableColumn("Zusatzinfo");
		infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
		infoCol.setCellFactory(cellFactory);
        TableColumn genderCol = new TableColumn("Genius");
		genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
		genderCol.setCellFactory(genderCellFactory);
        TableColumn partOfSpeechCol = new TableColumn("Wortart");
		partOfSpeechCol.setCellValueFactory(new PropertyValueFactory<>("partOfSpeech"));
		partOfSpeechCol.setCellFactory(partOfSpeechCellFactory);


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
				handleTableSelectionChanged();
			}
		});

		entries = FXCollections.observableList(vocabularyManagerBo.getEditorEntries(flashCardGroup));
		table.setItems(entries);
    }


    private void updateButtons() {
		ObservableList<?> items = table.getSelectionModel().getSelectedItems();
        //addButton.setDisable(false);
		//editButton.setDisable(items.size() != 1);
		moveButton.setDisable(items.size() <= 0);
		deleteButton.setDisable(items.size() <= 0);
	}



	// ***** Handlers ***************************************************** //

    private void handleTableSelectionChanged() {
		ObservableList items = table.getSelectionModel().getSelectedItems();
		vocableEditPanel.setVisible(items.size() == 1);
		if(items.size() == 1) {
			EditorEntry newEntry = (EditorEntry) items.get(0);

			if(currentSelectedEntry != null) {
				vocableTextField.textProperty().unbindBidirectional(currentSelectedEntry.vocableProperty());
				translation1TextField.textProperty().unbindBidirectional(currentSelectedEntry.native1Property());
				translation2TextField.textProperty().unbindBidirectional(currentSelectedEntry.native2Property());
				translation3TextField.textProperty().unbindBidirectional(currentSelectedEntry.native3Property());
				sentenceTextField.textProperty().unbindBidirectional(currentSelectedEntry.exampleProperty());
				sentenceTranslationTextField.textProperty().unbindBidirectional(currentSelectedEntry.exampleTranslationProperty());
				tipTextField.textProperty().unbindBidirectional(currentSelectedEntry.tipProperty());
				addInfoTextField.textProperty().unbindBidirectional(currentSelectedEntry.infoProperty());
				currentSelectedEntry.genderProperty().unbind();
				currentSelectedEntry.partOfSpeechProperty().unbind();
			}

			// Set the gender and partofspeech manualy. Later only bind in one direction.
			genderChoiceBox.getSelectionModel().select(newEntry.getGender());
			partOfSpeechChoiceBox.getSelectionModel().select(newEntry.getPartOfSpeech());

			vocableTextField.textProperty().bindBidirectional(newEntry.vocableProperty());
			translation1TextField.textProperty().bindBidirectional(newEntry.native1Property());
			translation2TextField.textProperty().bindBidirectional(newEntry.native2Property());
			translation3TextField.textProperty().bindBidirectional(newEntry.native3Property());
			sentenceTextField.textProperty().bindBidirectional(newEntry.exampleProperty());
			sentenceTranslationTextField.textProperty().bindBidirectional(newEntry.exampleTranslationProperty());
			tipTextField.textProperty().bindBidirectional(newEntry.tipProperty());
			addInfoTextField.textProperty().bindBidirectional(newEntry.infoProperty());
			newEntry.genderProperty().bind(genderChoiceBox.getSelectionModel().selectedIndexProperty());
			newEntry.partOfSpeechProperty().bind(partOfSpeechChoiceBox.getSelectionModel().selectedIndexProperty());

			currentSelectedEntry = newEntry;
		}
	}


    @FXML
    protected void handleAddButtonAction(ActionEvent event) {
		EditorEntry entry = new EditorEntry();
		table.getItems().add(entry);
		table.getSelectionModel().clearAndSelect(table.getItems().size()-1);
		vocableTextField.requestFocus();
    }


    @FXML
    protected void handleMoveButtonAction(ActionEvent event) {
    	List<String> vocableIds = new LinkedList<>();
		table.getSelectionModel().getSelectedItems().forEach( (EditorEntry e) -> { vocableIds.add(e.getId()); } );
    	if(vocableIds.isEmpty()) {
    		return;
    	}

    	contextController.createController("moveVocabularyView", ControllerParamsBuilder.create()
																.add("flashCardIds", vocableIds)
																.add("parentWindow", stage).build());
    }


    @FXML
    protected void handleDeleteButtonAction(ActionEvent event) {
		List<EditorEntry> selected = new LinkedList<>(table.getSelectionModel().getSelectedItems());
		removedEntries.addAll(selected);
		entries.removeAll(selected);
		table.getSelectionModel().clearSelection();
    }


    @FXML
    protected void handleSaveButtonAction(ActionEvent event) {
		vocabularyManagerBo.saveModifiedEntries(entries, removedEntries);
		removedEntries.clear();
    }


    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	contextController.destroy();
    }


    @FXML
    protected void handleVocableTextFieldAction(ActionEvent event) {
		translation1TextField.requestFocus();
    }


    @FXML
    protected void handleTranslation1TextFieldAction(ActionEvent event) {
		if(translation1TextField.getText() == null || translation1TextField.getText().trim().isEmpty()) {
			return;
		}

		translation2TextField.requestFocus();
    }


    @FXML
    protected void handleTranslation2TextFieldAction(ActionEvent event) {
		if(translation2TextField.getText() == null || translation2TextField.getText().trim().isEmpty()) {
			return;
		}

		translation3TextField.requestFocus();
    }


    @FXML
    protected void handleTranslation3TextFieldAction(ActionEvent event) {
		if(translation3TextField.getText() == null || translation3TextField.getText().trim().isEmpty()) {
			return;
		}
    }








	/**
	 * Own implementation for editing cell.
	 */
	class EditingCell extends TableCell<EditorEntry, String> {

		private TextField textField;


		public EditingCell() {
		}


		@Override
		public void startEdit() {
			if(!isEmpty()) {
				super.startEdit();
				if (textField == null) {
					createTextField();
				}
				setText(null);
				setGraphic(textField);
				textField.selectAll();
				textField.requestFocus();
			}
		}


		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText((String) getItem());
			setGraphic(null);
		}


		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if(empty) {
				setText(null);
				setGraphic(null);
			} else {
				if(isEditing()) {
					if(textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(null);
				}
			}
		}


		private void createTextField() {
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0,
						Boolean arg1, Boolean arg2) {
					if(!arg2) {
						commitEdit(textField.getText());
					}
				}
			});
			textField.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					commitEdit(textField.getText());
				}
			});
		}


		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}

	}


	/**
	 * Own implementation for gender cell.
	 */
	class GenderCell extends TableCell<EditorEntry, Integer> {

		@Override
		public void updateItem(Integer item, boolean empty) {
			super.updateItem(item, empty);

			if(empty) {
				setText(null);
				setGraphic(null);
			} else {
				setText(genderMap.getStringByGender(Gender.values()[item]));
			}
		}
	}

	/**
	 * Own implementation for partOfSpeech cell.
	 */
	class PartOfSpeechCell extends TableCell<EditorEntry, Integer> {

		@Override
		public void updateItem(Integer item, boolean empty) {
			super.updateItem(item, empty);

			if(empty) {
				setText(null);
				setGraphic(null);
			} else {
				setText(partOfSpeechMap.getStringByPartOfSpeech(PartOfSpeech.values()[item]));
			}
		}
	}
}
