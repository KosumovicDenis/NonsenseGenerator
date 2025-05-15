package unipd.ddkk.gui;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import unipd.ddkk.core.*;

public class MainGUI extends Application {
    private Service service;
    private Controller controller;
    private final TextArea renderArea = new TextArea();
    private final TextArea historyArea = new TextArea();
    private final TextArea syntaxTreeArea = new TextArea();
    private final ProgressIndicator loadingIndicator = new ProgressIndicator();
    private Button submitButton;

    @Override
    public void start(Stage stage) {
        this.service = new Service();
        this.controller = new Controller(service);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter your text");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        submitButton = new Button("Submit");
        Spinner<Integer> spinner = new Spinner<>(1, 100, 1);
        spinner.setPrefWidth(70);

        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(30, 30);

        HBox inputBox = new HBox(10, inputField, submitButton, spinner, loadingIndicator);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        CheckBox treeCheckBox = new CheckBox("Get syntactic tree");

        VBox inputArea = new VBox(10, inputBox, treeCheckBox);
        inputArea.setPadding(new Insets(15));

        renderArea.setEditable(false);
        renderArea.setWrapText(true);
        renderArea.setPrefHeight(200);
        renderArea.setPromptText("Rendered output will appear here...");
        VBox.setVgrow(renderArea, Priority.ALWAYS);

        syntaxTreeArea.setEditable(false);
        syntaxTreeArea.setWrapText(true);
        syntaxTreeArea.setVisible(false);
        syntaxTreeArea.setPrefHeight(150);
        syntaxTreeArea.setPromptText("Syntactic tree output will appear here...");
        VBox.setVgrow(syntaxTreeArea, Priority.ALWAYS);

        Button copyButton = new Button("Copy");
        Button showHistoryButton = new Button("Show history");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonRow = new HBox(10, copyButton, spacer, showHistoryButton);
        buttonRow.setAlignment(Pos.CENTER);

        VBox outputSection = new VBox(10, renderArea, syntaxTreeArea, buttonRow);
        outputSection.setPadding(new Insets(0, 15, 15, 15));
        VBox.setVgrow(outputSection, Priority.ALWAYS);
        VBox root = new VBox(10, inputArea, outputSection);

        Stage historyStage = new Stage();
        historyStage.setTitle("Submission History");
        historyStage.setMinWidth(500);
        historyStage.setMinHeight(300);

        historyArea.setEditable(false);
        historyArea.setWrapText(true);
        VBox historyLayout = new VBox(historyArea);
        historyLayout.setPadding(new Insets(10));
        VBox.setVgrow(historyArea, Priority.ALWAYS);
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
            e -> handleSubmission(inputField.getText(), treeCheckBox.isSelected(), spinner.getValue())
        );
        inputField.setOnAction(
            e -> handleSubmission(inputField.getText(), treeCheckBox.isSelected(), spinner.getValue())
        );

        Scene scene = new Scene(root, 500, 450);
        stage.setScene(scene);
        stage.setTitle("Nonsense Generator");
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.show();
    }

    private void handleSubmission(String input, boolean tree, int count) {
        renderArea.clear();
        syntaxTreeArea.clear();
        syntaxTreeArea.setVisible(false);

        loadingIndicator.setVisible(true);
        submitButton.setDisable(true);

        Task<GenerationResult> task = new Task<>() {
            @Override
            protected GenerationResult call() {
                return controller.generate(input, count);
            }

            @Override
            protected void succeeded() {
                GenerationResult res = getValue();

                // Display generated sentences
                renderArea.clear();
                for (GeneratedSentence s : res.sentences) {
                    renderArea.appendText(s.content + "\n");
                }

                // Display syntax tree structure if requested
                if (tree && res.syntaxTree != null) {
                    syntaxTreeArea.setText(formatStructure(res.syntaxTree));
                    syntaxTreeArea.setVisible(true);
                }

                loadingIndicator.setVisible(false);
                submitButton.setDisable(false);
            }

            @Override
            protected void failed() {
                renderArea.appendText("Errore during generation\n");
                loadingIndicator.setVisible(false);
                submitButton.setDisable(false);
            }
        };

        new Thread(task).start();
    }

    private String formatStructure(SentenceStructure s) {
        return "Noun: " + Arrays.toString(s.names) + "\n" +
               "Verbs: " + Arrays.toString(s.verbs) + "\n" +
               "adjectives: " + Arrays.toString(s.adjectives);
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

