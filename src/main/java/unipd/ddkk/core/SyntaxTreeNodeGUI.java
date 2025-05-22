package unipd.ddkk.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.control.TreeItem;

public class SyntaxTreeNodeGUI {
    String text;
    String details;
    List<SyntaxTreeNodeGUI> children = new ArrayList<>();

    public SyntaxTreeNodeGUI(String text, String details) {
        this.text = text;
        this.details = details;
    }

    public static SyntaxTreeNodeGUI buildTree(JsonNode json) {
        Map<Integer, SyntaxTreeNodeGUI> map = new HashMap<>();
        SyntaxTreeNodeGUI root = new SyntaxTreeNodeGUI("Phrase: " + json.get("sentences").get(0).path("text").get("content").asText(), "");

        JsonNode tokens = json.get("tokens");

        // Create nodes with filtered linguistic info
        for (int i = 0; i < tokens.size(); i++) {
            JsonNode token = tokens.get(i);
            String word = token.get("text").get("content").asText();
            JsonNode pos = token.get("partOfSpeech");

            List<String> features = new ArrayList<>();

            String tag = pos.get("tag").asText();
            if (!"UNKNOWN".equals(tag)) features.add("POS: " + tag);

            String tense = pos.get("tense").asText();
            if (!"TENSE_UNKNOWN".equals(tense)) features.add("Tense: " + tense);

            String mood = pos.get("mood").asText();
            if (!"MOOD_UNKNOWN".equals(mood)) features.add("Mood: " + mood);

            String lemma = token.get("lemma").asText();
            if (lemma != null && !lemma.isEmpty()) features.add("Lemma: " + lemma);

            String details = features.isEmpty() ? "" : "[" + String.join(", ", features) + "]";
            map.put(i, new SyntaxTreeNodeGUI(word, details));
        }

        // Attach children to their parents
        for (int i = 0; i < tokens.size(); i++) {
            JsonNode edge = tokens.get(i).get("dependencyEdge");
            int parentIdx = edge.get("headTokenIndex").asInt();
            SyntaxTreeNodeGUI parent = (parentIdx == i) ? root : map.get(parentIdx);
            parent.children.add(map.get(i));
        }

        return root;
    }

    public static TreeItem<String> toTreeItem(SyntaxTreeNodeGUI node) {
        TreeItem<String> item = new TreeItem<>(node.text + (node.details.isEmpty() ? "" : " " + node.details));
        for (SyntaxTreeNodeGUI child : node.children) {
            item.getChildren().add(toTreeItem(child));
        }
        return item;
    }
}
