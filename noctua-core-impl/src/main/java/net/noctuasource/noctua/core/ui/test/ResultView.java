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
package net.noctuasource.noctua.core.ui.test;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import net.noctuasource.act.annotation.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.noctua.core.test.impl.MarkGenerator;
import net.noctuasource.noctua.core.test.impl.SimpleGermanMarkGenerator;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.test.impl.TestHistory;
import net.noctuasource.noctua.core.test.impl.TestHistoryElement;
import net.noctuasource.noctua.core.test.impl.TestResultData;
import net.noctuasource.noctua.core.ui.ColorUtils;




/**
 * ResultView.
 * @author Philipp Thomas
 */
public class ResultView extends SubContextController {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(ResultView.class);


	// ***** Static Members ************************************************* //

	private static final String	FXML_FILE = "/ResultView.fxml";
	private static final String	CSS_FILE = "/General.css";


	// ***** Members ******************************************************** //

	private Stage			stage;

	private TestResultData 	resultData;
	private TestHistory		history;
	private long			timeElapsed;


	// ***** FXML Nodes ***************************************************** //

	@FXML private Label					headerTitle;
	@FXML private Label					testTypeText;
	@FXML private Tab					statisticsTab;
	@FXML private Tab					answersTab;
	@FXML private TitledPane			summaryTitledPane;
	@FXML private TitledPane			resultTtitledPane;
	@FXML private Label					dateDescLabel;
	@FXML private Label					countDescLabel;
	@FXML private Label					correctDescLabel;
	@FXML private Label					wrongDescLabel;
	@FXML private Label					tipsDescLabel;
	@FXML private Label					elapsedTimeDescLabel;

	@FXML private PieChart				testResultChart;
	@FXML private Label					summaryTextLabel;
	@FXML private Label					markLabel;
	@FXML private Label					dateValueLabel;
	@FXML private Label					countValueLabel;
	@FXML private Label					correctValueLabel;
	@FXML private Label					wrongValueLabel;
	@FXML private Label					tipsValueLabel;
	@FXML private Label					elapsedTimeValueLabel;
	@FXML private TableView<TestHistoryElement>				testAnswersTable;


	// ***** Constructor **************************************************** //

	@RunLater
	public void init() {
		this.resultData = getControllerParams().get(TestResultData.class);
		this.history = resultData.getTestHistory();
		this.timeElapsed = resultData.getTimeElapsed();

    	VBox root = new VBox();

    	stage = new Stage();
        stage.setTitle("Testergebnis");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				destroy();
			}
		});

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
			destroy();
		}

        initStaticFields();
        initStatistics();
        initAnswersTable();

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        stage.toFront();
	}


	// ***** Methods ******************************************************** //

    private void initStaticFields() {
    	headerTitle.setText("Testergebnis");
    	testTypeText.setText("Multiple Choice");
    	statisticsTab.setText("Statistiken");
    	answersTab.setText("Antworten");
    	summaryTitledPane.setText("Zusammenfassung");
    	resultTtitledPane.setText("Ergebnis");
    	dateDescLabel.setText("Abfragedatum:");
    	countDescLabel.setText("Anzahl Fragen:");
    	correctDescLabel.setText("Richtig beantwortet:");
    	wrongDescLabel.setText("Falsch beantwortet:");
    	tipsDescLabel.setText("Benötigte Tips:");
    	elapsedTimeDescLabel.setText("Benötigte Zeit:");
    }


    private void initStatistics() {
    	Integer correct = history.getCorrectCount();
    	Integer count = history.size();
		float correctness = correct * 100 / count;

        ObservableList<PieChart.Data> pieChartData =
    				FXCollections.observableArrayList(
				           new PieChart.Data("Richtig beantwortet", correct),
				           new PieChart.Data("Falsch beantwortet", count-correct));
    	testResultChart.setData(pieChartData);
    	testResultChart.setLabelsVisible(true);
    	testResultChart.setPickOnBounds(true);
    	testResultChart.setStartAngle(90);

    	summaryTextLabel.setText("Blaaa");

		MarkGenerator markGenerator = new SimpleGermanMarkGenerator();
    	markLabel.setText(markGenerator.generateMark(correctness));
    	markLabel.setTextFill(getColor(correctness));

    	dateValueLabel.setText(new SimpleDateFormat().format(resultData.getStartTime()));
    	countValueLabel.setText(count.toString());
    	correctValueLabel.setText(correct.toString());
    	wrongValueLabel.setText(new Integer(count -correct).toString());
    	tipsValueLabel.setText(Integer.toString(history.getTipsCount()));

    	String elapsedTimeValue = String.format("%02d Min. %02d Sek.",
    										timeElapsed/60, (timeElapsed%60));
    	elapsedTimeValueLabel.setText(elapsedTimeValue);
    }


    @SuppressWarnings("rawtypes")
	private void initAnswersTable() {

        TableColumn numberCol = new TableColumn("#");
        numberCol.setPrefWidth(20);
        numberCol.setCellValueFactory(
        	    new PropertyValueFactory<TestHistoryElement,Integer>("order")
        	);

        TableColumn questionCol = new TableColumn("Frage");
        questionCol.setPrefWidth(260);
        questionCol.setCellValueFactory(
        	    new PropertyValueFactory<TestHistoryElement,String>("question")
        	);

        TableColumn answerCol = new TableColumn("Deine Antwort");
        answerCol.setPrefWidth(280);
        answerCol.setCellValueFactory(
        	    new PropertyValueFactory<TestHistoryElement,String>("answer")
        	);

        TableColumn correctCol = new TableColumn("Richtig");
        correctCol.setPrefWidth(60);
        correctCol.setCellValueFactory(
        	    new PropertyValueFactory<TestHistoryElement,Boolean>("correct")
        	);
        Callback<TableColumn<TestHistoryElement, Boolean>, TableCell<TestHistoryElement, Boolean>> booleanCellFactory =
                new Callback<TableColumn<TestHistoryElement, Boolean>, TableCell<TestHistoryElement, Boolean>>() {
                @Override
                    public TableCell<TestHistoryElement, Boolean> call(TableColumn<TestHistoryElement, Boolean> p) {
                        return new BooleanCell();
                }
            };
        correctCol.setCellFactory(booleanCellFactory);

        final ObservableList<TestHistoryElement> data = FXCollections.observableArrayList();
        data.addAll(history);

    	testAnswersTable.setEditable(false);
    	testAnswersTable.setItems(data);

    	testAnswersTable.getColumns().addAll(numberCol, questionCol, answerCol, correctCol);
    }


    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
		destroy();
    }

	@Override
	protected void onDestroy() {
		if(stage != null) {
			stage.close();
		}
	}


	protected Color getColor(float correctness) {
		Stop[] stops = {
			new Stop(0, Color.WHITE),
			new Stop(0.01, Color.DARKGREEN),
			new Stop(0.27, Color.YELLOW),
			new Stop(0.4, Color.ORANGE),
			new Stop(0.6, Color.RED),
			new Stop(1, Color.MEDIUMVIOLETRED)
		};

		return ColorUtils.getGradientColor(stops, 1 - correctness / 100);
	}




	class BooleanCell extends TableCell<TestHistoryElement, Boolean> {

		private ImageView imageView;

		public BooleanCell() {
			imageView = new ImageView();
			imageView.setFitWidth(16);
			imageView.setFitHeight(16);

			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(imageView);

			this.setGraphic(vbox);
			this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		}


		@Override
		public void updateItem(Boolean item, boolean empty) {
			super.updateItem(item, empty);

			if (isEmpty()) {
				return;
			}

			String file = item ? "/images/Correct.png" : "/images/Wrong.png";
			imageView.setImage(new Image(ResultView.class.getResourceAsStream(file)));
		}
	}

}
