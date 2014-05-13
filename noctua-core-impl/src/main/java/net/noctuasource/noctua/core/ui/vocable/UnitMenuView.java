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
package net.noctuasource.noctua.core.ui.vocable;

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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.annotation.Resource;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.noctua.core.business.TreeNodeDto;
import net.noctuasource.noctua.core.business.VocableManagerBo;
import net.noctuasource.noctua.core.business.add.FlashCardGroupDto;
import net.noctuasource.noctua.core.dto.VocableListElement;
import net.noctuasource.noctua.core.events.VocableEvent;

import org.apache.log4j.Logger;





public class UnitMenuView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(UnitMenuView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/UnitMenu.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	@Resource
	VocableManagerBo			vocableManagerBo;

	@Resource
	EventBus					eventBus;

	private Stage				stage;

	private FlashCardGroupDto	flashCardGroup;


	// ***** FXML Nodes ***************************************************** //

	@FXML private Label							flashCardGroupTitle;

	@FXML private Button						addButton;
	@FXML private Button						editButton;
	@FXML private Button						moveButton;
	@FXML private Button						deleteButton;
	@FXML private TableView<VocableListElement>	vocabularyTable;


	// ***** Constructor **************************************************** //

	@Override
	protected void onCreate() {
		TreeNodeDto treeNode = getControllerParams().get("treeNode", TreeNodeDto.class);
		flashCardGroup = new FlashCardGroupDto();
		flashCardGroup.setId(treeNode.getId());
		flashCardGroup.setName(treeNode.getName());

    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle("Testergebnis");
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
        initStatistics();
        initVocabularyTable();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();

		updateVocabularyTable();
		updateButtons();

		eventBus.register(this);
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	//headerTitle.setText("Testergebnis");
    }


    private void initStatistics() {
//    	Integer correct = history.getCorrectCount();
//    	Integer count = history.size();
//
//        ObservableList<PieChart.Data> pieChartData =
//    				FXCollections.observableArrayList(
//				           new PieChart.Data("Richtig beantwortet", correct),
//				           new PieChart.Data("Falsch beantwortet", count-correct));
//    	testResultChart.setData(pieChartData);
//    	testResultChart.setLabelsVisible(true);
//    	testResultChart.setPickOnBounds(true);
//    	testResultChart.setStartAngle(90);
//
//    	summaryTextLabel.setText("Blaaa");
//    	markLabel.setText("6");
//    	markLabel.setTextFill(Color.ORANGERED);
//
//    	dateValueLabel.setText(new SimpleDateFormat().format(resultData.getStartTime()));
//    	countValueLabel.setText(count.toString());
//    	correctValueLabel.setText(correct.toString());
//    	wrongValueLabel.setText(new Integer(count -correct).toString());
//    	tipsValueLabel.setText(Integer.toString(history.getTipsCount()));
//
//    	String elapsedTimeValue = String.format("%02d Min. %02d Sek.",
//    										timeElapsed/60, (timeElapsed%60));
//    	elapsedTimeValueLabel.setText(elapsedTimeValue);
    }


	private void initVocabularyTable() {
        TableColumn foreignCol = new TableColumn("Fremdsprache");
        foreignCol.setPrefWidth(260);
        foreignCol.setCellValueFactory(
        	    new PropertyValueFactory<VocableListElement,String>("foreignString")
        	);

        TableColumn nativeCol = new TableColumn("Muttersprache");
        nativeCol.setPrefWidth(280);
        nativeCol.setCellValueFactory(
        	    new PropertyValueFactory<VocableListElement,String>("nativeString")
        	);

        TableColumn sentenceCol = new TableColumn("Beispielsatz");
        sentenceCol.setPrefWidth(300);
        sentenceCol.setCellValueFactory(
        	    new PropertyValueFactory<VocableListElement,String>("sentence")
        	);

    	vocabularyTable.setEditable(false);
    	vocabularyTable.getColumns().addAll(foreignCol, nativeCol, sentenceCol);
		vocabularyTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		vocabularyTable.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Integer> change) {
				updateButtons();
			}
		});
    }


	@Subscribe
    public void updateVocabularyTable(VocableEvent event) {
        updateVocabularyTable();
	}


    private void updateVocabularyTable() {
        ObservableList<VocableListElement> data = FXCollections.observableArrayList();
        data.addAll(vocableManagerBo.getVocabularyOfFlashCardGroup(flashCardGroup.getId()));
    	vocabularyTable.setItems(data);
	}


    private void updateButtons() {
		ObservableList<VocableListElement> items = vocabularyTable.getSelectionModel().getSelectedItems();
        //addButton.setDisable(false);
		editButton.setDisable(items.size() != 1);
		moveButton.setDisable(items.size() <= 0);
		deleteButton.setDisable(items.size() <= 0);

	}


    private List<String> getSelectedIds() {
		ObservableList<VocableListElement> items = vocabularyTable.getSelectionModel().getSelectedItems();
        List<String> vocableIds = FXCollections.observableArrayList();

		for(VocableListElement element : items) {
			vocableIds.add(element.getId());
		}

		return vocableIds;
	}


    @FXML
    protected void handleAddButtonAction(ActionEvent event) {
    	createController("addVocabularyView", ControllerParamsBuilder.create()
																.add("flashCardGroup", flashCardGroup)
																.add("parentWindow", stage).build());
    }

    @FXML
    protected void handleEditButtonAction(ActionEvent event) {
    	List<String> vocableIds = getSelectedIds();
    	if(vocableIds.size() != 1) {
    		return;
    	}

    	createController("editVocableView", ControllerParamsBuilder.create()
																.add("vocableId", vocableIds.iterator().next())
																.add("parentWindow", stage).build());
    }

    @FXML
    protected void handleMoveButtonAction(ActionEvent event) {
    	List<String> vocableIds = getSelectedIds();
    	if(vocableIds.isEmpty()) {
    		return;
    	}

    	createController("moveVocabularyView", ControllerParamsBuilder.create()
																.add("flashCardIds", vocableIds)
																.add("parentWindow", stage).build());
    }

    @FXML
    protected void handleDeleteButtonAction(ActionEvent event) {
    	List<String> vocableIds = getSelectedIds();
    	if(vocableIds.isEmpty()) {
    		return;
    	}

    	createController("deleteVocabularyView", ControllerParamsBuilder.create()
																.add("vocableIds", vocableIds)
																.add("parentWindow", stage).build());
    }

    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
    	destroy();
    }


	@Override
	protected void onDestroy() {
		stage.close();
		eventBus.unregister(this);
	}






	public void setVocableManagerBo(VocableManagerBo vocableManagerBo) {
		this.vocableManagerBo = vocableManagerBo;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

}
