package unipd.ddkk.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.*;

public class GoogleAPICaller implements APICaller {

    private final String apiKey;

    public GoogleAPICaller() {
        String key = "";
        try {
            Map<String, String> config = EnvLoader.loadEnv(".env");
            key = config.get("API_KEY");
            if (key == null || key.isEmpty()) {
                throw new RuntimeException("API_KEY not found in .env file.");
            }
        } catch (EnvFileNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        this.apiKey = key;
    }

    @Override
    public SentenceStructure getStructure(String sentence) {
        try {
            URL url = URI.create("https://language.googleapis.com/v1/documents:analyzeSyntax?key=" + apiKey).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonInput = String.format("""
                {
                "document": {
                "type": "PLAIN_TEXT",
                "content": "%s"
                },
                "encodingType": "UTF8"
                }
                """, sentence.replace("\"", "\\\""));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(conn.getInputStream());
            JsonNode tokens = root.get("tokens");

            List<String> nouns = new ArrayList<>();
            List<String> verbs = new ArrayList<>();
            List<String> adjectives = new ArrayList<>();

            for (JsonNode token : tokens) {
                String text = token.get("text").get("content").asText();
                String tag = token.get("partOfSpeech").get("tag").asText();

                switch (tag) {
                    case "NOUN", "PROPN" -> nouns.add(text);
                    case "VERB" -> verbs.add(text);
                    case "ADJ" -> adjectives.add(text);
                }
            }

            return new SentenceStructure(
                nouns.toArray(new String[0]),
                verbs.toArray(new String[0]),
                adjectives.toArray(new String[0])
            );

        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
            return new SentenceStructure(new String[0], new String[0], new String[0]);
        }
    }

    protected JsonNode callApi(String sentence) {
        try {
            URL url = URI.create("https://language.googleapis.com/v1/documents:moderateText?key=" + apiKey).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonInput = String.format("""
                {
                "document": {
                "type": "PLAIN_TEXT",
                "content": "%s"
                }
                }
                """, sentence.replace("\"", "\\\""));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode >= 400) {
                System.out.println("[ERROR] ModerateText API returned code: " + responseCode);
                return null; // Meglio bloccare in caso di errore
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(conn.getInputStream());
            return root.get("moderationCategories");

        } catch (Exception e) {
            System.out.println("[ERROR] ModerateText check failed: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<PhraseClassificationAttribute> getModerationCategories(String sentence) {
        ArrayList<PhraseClassificationAttribute> arr = new ArrayList<>();
        
        JsonNode moderationCategories = callApi(sentence);
        if (moderationCategories == null) {
            return arr;
        }

        for (JsonNode category : moderationCategories) {
            String name = category.get("name").asText();
            float confidence = category.get("confidence").floatValue();
            arr.add(new PhraseClassificationAttribute(name, confidence));
        }

        return arr;
    }

}
