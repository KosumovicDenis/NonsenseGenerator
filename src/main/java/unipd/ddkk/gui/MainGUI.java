package unipd.ddkk.gui;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import unipd.ddkk.core.*;

public class MainGUI extends Application {
    private final TextArea renderArea = new TextArea();
    private final TextArea historyArea = new TextArea();
    Controller controller;

    @Override
    public void start(Stage stage) {
        Service service = new Service();
        controller = new Controller(service);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter your text");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button submitButton = new Button("Submit");
        Spinner<Integer> spinner = new Spinner<>(1, 100, 1);
        spinner.setPrefWidth(70);

        HBox inputBox = new HBox(10, inputField, submitButton, spinner);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        CheckBox treeCheckBox = new CheckBox("Get syntactic tree");

        VBox inputArea = new VBox(10, inputBox, treeCheckBox);
        inputArea.setPadding(new Insets(15));

        renderArea.setEditable(false);
        renderArea.setWrapText(true);
        renderArea.setPrefHeight(200);
        renderArea.setPromptText("Rendered output will appear here...");
        VBox.setVgrow(renderArea, Priority.ALWAYS); // Allow renderArea to expand and push buttonRow down

        Button copyButton = new Button("Copy");
        Button showHistoryButton = new Button("Show history");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonRow = new HBox(10, copyButton, spacer, showHistoryButton);
        buttonRow.setAlignment(Pos.CENTER);

        VBox outputSection = new VBox(10, renderArea, buttonRow);
        outputSection.setPadding(new Insets(0, 15, 15, 15));
        VBox.setVgrow(outputSection, Priority.ALWAYS); // Let outputSection grow within root
        VBox root = new VBox(10, inputArea, outputSection);

        Stage historyStage = new Stage();
        historyStage.setTitle("Submission History");
        historyStage.setMinWidth(500);
        historyStage.setMinHeight(300);

        historyArea.setEditable(false);
        historyArea.setWrapText(true);

        VBox historyLayout = new VBox(historyArea);
        historyLayout.setPadding(new Insets(10)); // Apply constant padding around the history layout
        VBox.setVgrow(historyArea, Priority.ALWAYS); // Allow the TextArea to grow vertically
        historyStage.setScene(new Scene(historyLayout));

        showHistoryButton.setOnAction(e -> {
            historyArea.clear();
            for (GeneratedSentence s : controller.getHistory()) {
                historyArea.appendText(s.toString() + "\n");
            }
            historyStage.show();
        });

        copyButton.setOnAction(e -> {
            String text = renderArea.getText();
            if (!text.isEmpty()) {
                ClipboardContent content = new ClipboardContent();
                content.putString(text);
                Clipboard.getSystemClipboard().setContent(content);
            }
        });

        submitButton.setOnAction(
                e -> handleSubmission(inputField.getText(), treeCheckBox.isSelected(), spinner.getValue()));
        inputField.setOnAction(
                e -> handleSubmission(inputField.getText(), treeCheckBox.isSelected(), spinner.getValue()));

        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.setTitle("Nonsense Generator");
        stage.setMinWidth(500);
        stage.setMinHeight(300);
        stage.show();
    }

    private void handleSubmission(String input, boolean tree, int count) {
        renderArea.clear();
        displayGenerated(controller.generate(input, count));
    }

    public void displayGenerated(ArrayList<GeneratedSentence> sentences) {
        for (GeneratedSentence sentence : sentences) {
            renderArea.appendText(sentence.content + "\n");
        }
    }

    public void updateHistory(String record) {
        historyArea.appendText(record + "\n");
    }
}
