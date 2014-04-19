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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import net.noctuasource.noctua.core.events.FlashCardEvent;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;





public class EditorView {

	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/EditorView.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	@ControllerContext
	private ContextController			contextController;

	@Resource
	private FlashCardManagerBo			flashCardBo;

	@Resource
	EventBus					eventBus;

	private Stage				stage;

	private FlashCardGroupDto	flashCardGroup;
	
	private SpreadsheetView		spreadsheetView;
	
	private GridBase			grid;


	// ***** FXML Nodes ***************************************************** //

	@FXML private Label							flashCardGroupTitle;

	@FXML private Button						addButton;
	@FXML private Button						editButton;
	@FXML private Button						moveButton;
	@FXML private Button						deleteButton;
	@FXML private VBox							tableEditorBox;


	// ***** Constructor **************************************************** //

	@PostConstruct
	public void onCreate() {
		//TreeNodeDto treeNode = contextController.getControllerParams().get("treeNode", TreeNodeDto.class);
		flashCardGroup = new FlashCardGroupDto();
		//flashCardGroup.setId(treeNode.getId());
		//flashCardGroup.setName(treeNode.getName());

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
			//logger.error("Error while creating result view: ", e);
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

		//eventBus.register(this);
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
		grid = new GridBase(50, 10);
		buildGrid(grid, true);
		
		spreadsheetView = new SpreadsheetView(grid);
		grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight()));
		spreadsheetView.setShowRowHeader(true);
		spreadsheetView.setShowColumnHeader(true);
    	spreadsheetView.setEditable(true);
				
    	//spreadsheetView.getColumns().addAll(foreignColumn);
		//spreadsheetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//		spreadsheetView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
//			@Override
//			public void onChanged(ListChangeListener.Change<? extends Integer> change) {
//				updateButtons();
//			}
//		});
		
		spreadsheetView.setFixingColumnsAllowed(false);
		spreadsheetView.setFixingRowsAllowed(false);
		VBox.setVgrow(spreadsheetView, Priority.ALWAYS);
		tableEditorBox.getChildren().add(spreadsheetView);
    }

	
    /**
     * FIXME need to be removed after Compute RowHeight for test
     *
     * @return
     */
    private Map<Integer, Double> generateRowHeight() {
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(1, 100.0);
        rowHeight.put(5, 50.0);
        rowHeight.put(8, 70.0);
        rowHeight.put(12, 40.0);
        return rowHeight;
    }
	
	
    /**
     * Build the grid with the type specifying for normal(0) or Both span(1).
     *
     * @param grid
     * @param type
     */
    private void buildGrid(GridBase grid, boolean span) {
		normalGrid(grid);
		buildBothGrid(grid);
		grid.getColumnHeaders().add("Vokabel");
		grid.getColumnHeaders().add("Übersetzung 1");
		grid.getColumnHeaders().add("Übersetzung 2");
//		grid.getColumnHeaders().add("Übersetzung 3");
//		grid.getColumnHeaders().add("Beispielsatz");
//		grid.getColumnHeaders().add("Beispielsatz Übersetzung");
    }
 
	
    /**
     * Build the grid with no span.
     *
     * @param grid
     */
    private void normalGrid(GridBase grid) {
        ArrayList<ObservableList<SpreadsheetCell>> rows = new ArrayList<>(grid.getRowCount());
 
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> dataRow = FXCollections.observableArrayList(); // new
                                                                                                 // DataRow(row,
                                                                                                 // grid.getColumnCount());
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                dataRow.add(generateCell(row, column, 1, 1));
            }
            rows.add(dataRow);
        }
        grid.setRows(rows);
 
//        // FIXME When setting at the very first row, the display is
//        // huumm..wrong.
//        final ObservableList<SpreadsheetCell> imageRow = FXCollections.observableArrayList();
//        for (int column = 0; column < grid.getColumnCount(); ++column) {
//            SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(1, column, 1, 1, null);
//            if (column % 3 == 0) {
//                cell.setGraphic(new ImageView(new Image(SpreadsheetView.class.getResourceAsStream("Koala.jpg"))));
//            } else if (column % 3 == 1) {
//                cell.setGraphic(new ImageView(new Image(SpreadsheetView.class.getResourceAsStream("Penguins.jpg"))));
//            } else {
//                cell.setGraphic(new ImageView(new Image(SpreadsheetView.class.getResourceAsStream("Jellyfish.jpg"))));
//            }
// 
//            cell.setEditable(false);
//            imageRow.add(cell);
//        }
//        grid.getRows().set(1, imageRow);
    }
	
	
	/**
     * Build a sample RowSpan and ColSpan grid
     *
     * @param grid
     */
    private void buildBothGrid(GridBase grid) {
//        grid.spanRow(2, 2, 2);
//        grid.spanColumn(2, 2, 2);
// 
//        grid.spanRow(4, 2, 4);
// 
//        grid.spanColumn(5, 8, 2);
// 
//        grid.spanRow(15, 3, 8);
// 
//        grid.spanRow(3, 5, 5);
//        grid.spanColumn(3, 5, 5);
// 
//        grid.spanRow(2, 10, 4);
//        grid.spanColumn(3, 10, 4);
// 
//        grid.spanRow(2, 12, 3);
//        grid.spanColumn(3, 22, 3);
// 
//        grid.spanRow(1, 27, 4);
// 
//        grid.spanColumn(4, 30, 3);
//        grid.spanRow(4, 30, 3);
    }
	
	
    /**
     * Randomly generate a {@link SpreadsheetCell}. Also use the value inside
     * {@link #typeOfCell} to display all cells, only numbers or only dates.
     */
    private SpreadsheetCell generateCell(int row, int column, int rowSpan, int colSpan) {
        SpreadsheetCell cell;

		List<String> stringListTextCell = Arrays.asList("Shanghai", "Paris", "New York City", "Bangkok",
				"Singapore", "Johannesburg", "Berlin", "Wellington", "London", "Montreal");
		final double random = Math.random();
		if (random < 0.25) {
			List<String> stringList = Arrays.asList("China", "France", "New Zealand", "United States", "Germany",
					"Canada");
			cell = SpreadsheetCellType.LIST(stringList).createCell(row, column, rowSpan, colSpan,
					stringList.get((int) (Math.random() * 6)));
		} else {
			cell = SpreadsheetCellType.STRING.createCell(row, column, rowSpan, colSpan,
					stringListTextCell.get((int) (Math.random() * 10)));
		}

        return cell;
    }
	
	
    /**
     * Build the grid with no span.
     *
     * @param grid
     */
    private void addLine() {
		final ObservableList<SpreadsheetCell> dataRow = FXCollections.observableArrayList(); // new DataRow(row, grid.getColumnCount());
		for (int column = 0; column < grid.getColumnCount(); ++column) {
			dataRow.add(generateCell(grid.getRowCount(), column, 1, 1));
		}
		
		grid.getRows().add(dataRow);
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
		ObservableList<?> items = spreadsheetView.getSelectionModel().getSelectedItems();
        //addButton.setDisable(false);
		editButton.setDisable(items.size() != 1);
		moveButton.setDisable(items.size() <= 0);
		deleteButton.setDisable(items.size() <= 0);

	}


    private List<String> getSelectedIds() {
		ObservableList<?> items = spreadsheetView.getSelectionModel().getSelectedItems();
        List<String> vocableIds = FXCollections.observableArrayList();
//
//		for(VocableListElement element : items) {
//			vocableIds.add(element.getId());
//		}

		return vocableIds;
	}


    @FXML
    protected void handleAddButtonAction(ActionEvent event) {
    	addLine();
    }

    @FXML
    protected void handleEditButtonAction(ActionEvent event) {
    	List<String> vocableIds = getSelectedIds();
    	if(vocableIds.size() != 1) {
    		return;
    	}

    	contextController.createController("editVocableView", ControllerParamsBuilder.create()
																.add("vocableId", vocableIds.iterator().next())
																.add("parentWindow", stage).build());
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
    protected void handleCloseButtonAction(ActionEvent event) {
    	contextController.destroy();
    }


	@PreDestroy
	protected void onDestroy() {
		stage.close();
		eventBus.unregister(this);
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

}
