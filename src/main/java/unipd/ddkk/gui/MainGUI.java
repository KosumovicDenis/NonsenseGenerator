package unipd.ddkk.gui;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import unipd.ddkk.core.*;
import atlantafx.base.theme.*;

public class MainGUI extends Application {
    private Controller controller;
    private final VBox renderContainer = new VBox(1); // Spacing between items
    private final TextArea historyArea = new TextArea();
    private TreeView<String> treeView = null;
    private final Label renderPlaceholder = new Label("Rendered output will appear here...");
    private Popup classificationPopup = new Popup();
    private ArrayList<GeneratedSentence> latestGeneratedSentences = new ArrayList<>();
    private VBox outputSection;

    CheckBox addToDict;
    CheckBox treeCheckBox;

    final String noTemplateSelectedText = "Select a template (optional)";
    private final ComboBox<String> templateDropdown = new ComboBox<>();

    private final ProgressIndicator loadingIndicator = new ProgressIndicator();
    private Button submitButton;

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        this.controller = new Controller();

        TextField inputField = new TextField();
        inputField.setPromptText("Enter your text");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        submitButton = new Button("Submit");
        submitButton.getStyleClass().addAll(Styles.ACCENT);
        Spinner<Integer> spinner = new Spinner<>(1, 100, 1);
        spinner.setPrefWidth(70);

        classificationPopup.setAutoHide(true);

        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(30, 30);

        HBox inputBox = new HBox(10, inputField, submitButton, spinner, loadingIndicator);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        templateDropdown.getItems().add(noTemplateSelectedText);
        templateDropdown.getItems().addAll(controller.getAvailableTemplates());
        templateDropdown.getSelectionModel().select(0);
        templateDropdown.setMaxWidth(Double.MAX_VALUE);

        treeCheckBox = new CheckBox("Get syntactic tree");

        addToDict = new CheckBox("Add to dictionary");

        // Create info icon label
        Label infoIcon = new Label("ⓘ");
        infoIcon.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-cursor: hand;");
        infoIcon.setTooltip(new Tooltip(
                "Temporarily adds words to the internal dictionary.\nThey will be cleared when the app closes."));

        // Group checkbox and info icon together
        HBox checkboxWithInfo = new HBox(2, addToDict, infoIcon);
        checkboxWithInfo.setAlignment(Pos.CENTER_LEFT);

        HBox checkboxesRow = new HBox(15, treeCheckBox, checkboxWithInfo);
        checkboxesRow.setAlignment(Pos.CENTER_LEFT);

        VBox inputArea = new VBox(10, inputBox, templateDropdown, checkboxesRow);
        inputArea.setPadding(new Insets(15));

        inputArea.setPadding(new Insets(15));

        renderPlaceholder.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");

        // Inner container for sentence rows (inside scroll area)
        renderContainer.setPadding(new Insets(10));
        renderContainer.getChildren().add(renderPlaceholder);
        VBox.setVgrow(renderContainer, Priority.ALWAYS);
        
        ScrollPane scrollPane = new ScrollPane(renderContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);

        // Outer container adds border & background
        VBox scrollWrapper = new VBox(scrollPane);
        scrollWrapper.setPrefHeight(200);
        scrollWrapper.setMinHeight(100);
        scrollWrapper.setMaxHeight(350);
        scrollWrapper.setPadding(new Insets(0)); // Optional, adjust spacing
        scrollWrapper.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.web("#161b22"), new CornerRadii(3), Insets.EMPTY)));
        scrollWrapper.setBorder(new Border(new BorderStroke(
                javafx.scene.paint.Color.web("#30363d"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(3),
                new BorderWidths(1))));
        VBox.setVgrow(scrollWrapper, Priority.ALWAYS);

        Button copyButton = new Button("Copy");
        Button showHistoryButton = new Button("Show history");
        copyButton.getStyleClass().addAll(Styles.BG_NEUTRAL_EMPHASIS_PLUS);
        showHistoryButton.getStyleClass().addAll(Styles.ACCENT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonRow = new HBox(10, copyButton, spacer, showHistoryButton);
        buttonRow.setAlignment(Pos.CENTER);

        outputSection = new VBox(10, scrollWrapper, buttonRow);
        outputSection.setPadding(new Insets(0, 15, 15, 15));
        VBox.setVgrow(outputSection, Priority.ALWAYS);
        VBox root = new VBox(10, inputArea, outputSection);

        Stage historyStage = new Stage();
        historyStage.setTitle("Submission History");
        historyStage.setMinWidth(700);
        historyStage.setMinHeight(900);

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
            if (latestGeneratedSentences.isEmpty())
                return;

            StringBuilder sb = new StringBuilder();
            for (GeneratedSentence sentence : latestGeneratedSentences) {
                sb.append(sentence.getContent()).append("\n");
            }

            ClipboardContent content = new ClipboardContent();
            content.putString(sb.toString().trim());
            Clipboard.getSystemClipboard().setContent(content);
        });

        submitButton.setOnAction(
                e -> handleSubmission(inputField.getText(), spinner.getValue()));
        inputField.setOnAction(
                e -> handleSubmission(inputField.getText(), spinner.getValue()));

        Scene scene = new Scene(root, 500, 450);
        stage.setScene(scene);
        stage.setTitle("Nonsense Generator");
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.show();
    }

    private void handleSubmission(String input, int count) {
        loadingIndicator.setVisible(true);
        submitButton.setDisable(true);

        Task<GenerationResult> task = new Task<>() {
            @Override
            protected GenerationResult call() {
                return controller.generate(input, addToDict.isSelected(), count,
                        templateDropdown.getValue().equals(noTemplateSelectedText) ? "" : templateDropdown.getValue());
            }

            @Override
            protected void succeeded() {
                GenerationResult res = getValue();
                latestGeneratedSentences = res.sentences;

                renderContainer.getChildren().clear(); // Clear previous content
                if (res.sentences.isEmpty()) {
                    renderContainer.getChildren().add(renderPlaceholder);
                } else {
                    for (GeneratedSentence s : res.sentences) {
                        Label sentenceLabel = new Label(s.getContent());
                        HBox.setHgrow(sentenceLabel, Priority.ALWAYS);
                        sentenceLabel.setMaxWidth(Double.MAX_VALUE);

                        Button detailsButton = new Button("ⓘ");
                        detailsButton.setOnAction(e -> {
                            // Close if already showing (toggle behavior)
                            if (classificationPopup.isShowing()) {
                                classificationPopup.hide();
                                return;
                            }

                            ArrayList<PhraseClassificationAttribute> classification = s.getClassification();
                            if (classification == null || classification.isEmpty())
                                return;

                            StringBuilder tooltipText = new StringBuilder("Classification:\n");
                            for (PhraseClassificationAttribute attr : classification) {
                                tooltipText.append("- ").append(attr.toString()).append("\n");
                            }

                            Label content = new Label(tooltipText.toString());
                            content.setStyle(
                                    "-fx-background-color: #161b22; -fx-text-fill: white; -fx-padding: 10; -fx-border-color: #30363d; -fx-border-radius: 8;");
                            content.setWrapText(true);
                            content.setMaxWidth(300);

                            classificationPopup.getContent().clear(); // Prevent stacking content
                            classificationPopup.getContent().add(content);

                            Bounds bounds = detailsButton.localToScreen(detailsButton.getBoundsInLocal());
                            classificationPopup.show(detailsButton, bounds.getMinX(), bounds.getMaxY());
                        });

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);
                        HBox row = new HBox(10, detailsButton, sentenceLabel, spacer);
                        row.setAlignment(Pos.CENTER_LEFT);

                        renderContainer.getChildren().add(row);
                    }
                }

                // Remove old tree if it exists
                if (treeView != null) {
                    outputSection.getChildren().remove(treeView);
                    treeView = null;
                }

                if (treeCheckBox.isSelected() && res.syntaxTree != null && !res.syntaxTree.structure.get("sentences").isEmpty()) {
                    SyntaxTreeNodeGUI parseRoot = SyntaxTreeNodeGUI.buildTree(res.syntaxTree.structure);
                    TreeItem<String> fxRoot = SyntaxTreeNodeGUI.toTreeItem(parseRoot);
                    fxRoot.setExpanded(true);

                    treeView = new TreeView<>(fxRoot);
                    treeView.setMinHeight(100);
                    treeView.setMaxHeight(350);

                    int insertIndex = outputSection.getChildren().indexOf(outputSection.getChildren().getLast());
                    outputSection.getChildren().add(insertIndex, treeView);
                }

                loadingIndicator.setVisible(false);
                submitButton.setDisable(false);
            }

            @Override
            protected void failed() {
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

    public void updateHistory(String record) {
        historyArea.appendText(record + "\n");
    }
}
