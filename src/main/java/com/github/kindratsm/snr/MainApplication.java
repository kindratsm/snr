package com.github.kindratsm.snr;

import com.github.kindratsm.snr.engines.SNREngine;
import com.github.kindratsm.snr.enums.SNRCountry;
import com.github.kindratsm.snr.enums.SNRLanguage;
import com.github.kindratsm.snr.models.SNRPhoneNumber;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main JavaFX application
 * 
 * @author Stanislav Kindrat
 *
 */
public class MainApplication extends Application {

	/**
	 * Entry point to initialize SNR GUI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Input
		final TextField inputField = new TextField();

		// Console
		final TextArea consoleField = new TextArea();
		consoleField.setEditable(false);

		// Output table
		final TableColumn<SNRPhoneNumber, SNRCountry> countryColumn = new TableColumn<>("Country");
		countryColumn.setCellValueFactory(cell -> new SimpleObjectProperty<SNRCountry>(cell.getValue().getCountry()));

		final TableColumn<SNRPhoneNumber, String> interpretationColumn = new TableColumn<>("Interpretation");
		interpretationColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getInterpretation()));

		final TableColumn<SNRPhoneNumber, String> validColumn = new TableColumn<>("Is Valid");
		validColumn.setCellValueFactory(cell -> {
			if (cell.getValue().isValid()) {
				return new SimpleStringProperty("[phone number: VALID]");
			} else {
				return new SimpleStringProperty("[phone number: INVALID]");
			}
		});

		final TableView<SNRPhoneNumber> outputTable = new TableView<SNRPhoneNumber>();
		outputTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		outputTable.getColumns().add(countryColumn);
		outputTable.getColumns().add(interpretationColumn);
		outputTable.getColumns().add(validColumn);

		// Set row factory to control row background color
		outputTable.setRowFactory(row -> new TableRow<SNRPhoneNumber>() {
			@Override
			public void updateItem(SNRPhoneNumber item, boolean empty) {
				super.updateItem(item, empty);

				String style;
				if (item == null) {
					style = "";
				} else if (item.isValid()) {
					style = "-fx-base: lightgreen;";
				} else {
					style = "-fx-base: lightcoral;";
				}

				setStyle(style);
			}
		});

		// Recognize button
		final Button recognizeButton = new Button("Recognize");
		recognizeButton.setOnAction(event -> {
			recognize(inputField.textProperty(), outputTable.getItems(), consoleField.textProperty());
		});

		// Top pane
		final BorderPane topPane = new BorderPane();
		topPane.setLeft(new Label("Input:"));
		topPane.setCenter(inputField);
		topPane.setBottom(recognizeButton);

		// Root pane
		final BorderPane pane = new BorderPane();
		pane.setTop(topPane);
		pane.setCenter(outputTable);
		pane.setBottom(consoleField);
		pane.setPadding(new Insets(5));

		// Create scene
		final Scene scene = new Scene(pane);

		// Bindings
		recognizeButton.prefWidthProperty().bind(topPane.widthProperty());

		// Set alignment and margin
		BorderPane.setAlignment(topPane.getLeft(), Pos.CENTER_LEFT);
		BorderPane.setMargin(topPane.getLeft(), new Insets(0, 5, 0, 0));
		BorderPane.setMargin(recognizeButton, new Insets(5, 0, 5, 0));

		// Init stage
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Recognize button action
	 * 
	 * @param input   the input string property
	 * @param output  the output observable list
	 * @param console the console string property
	 */
	private void recognize(StringProperty input, ObservableList<SNRPhoneNumber> output, StringProperty console) {
		// Clear
		output.clear();
		console.setValue("");

		final StringBuilder log = new StringBuilder();
		final long startMillis = System.currentTimeMillis();
		final long startNano = System.nanoTime();

		// Convert input
		try {
			output.addAll(SNREngine.getInstance(SNRLanguage.ENGLISH)
					.recognize(SNRCountry.GREECE, input.getValue()));
		} catch (Exception ex) {
			log.append(String.format("%s: %s\n", ex.getClass().getName(), ex.getMessage()));
		}

		log.append(String.format("Done in: %d ms (%d ns)",
				(System.currentTimeMillis() - startMillis),
				(System.nanoTime() - startNano)));

		console.setValue(log.toString());
	}

	/**
	 * Entry point of Java SNR program
	 * 
	 * @param args the Java program arguments
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

}
