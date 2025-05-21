package unipd.ddkk.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockGoogleAPICaller extends GoogleAPICaller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random random = new Random();

    @Override
    protected JsonNode callApi(String sentence) {
        ArrayNode categoriesArray = mapper.createArrayNode();

        String[] categoryNames = {
                "Toxic", "Insult", "Profanity", "Derogatory", "Sexual", "Death, Harm & Tragedy",
                "Violent", "Firearms & Weapons", "Public Safety", "Health", "Religion & Belief",
                "Illicit Drugs", "War & Conflict", "Politics", "Finance", "Legal"
        };

        for (String name : categoryNames) {
            ObjectNode categoryNode = mapper.createObjectNode();
            categoryNode.put("name", name);
            categoryNode.put("confidence", Math.round(random.nextFloat() * 51) / 100.0f); // Rounded to 2 decimals
            categoriesArray.add(categoryNode);
        }

        return categoriesArray;
    }

    // Optional: override getModerationCategories to log or simulate further
    // behavior if needed
    @Override
    public ArrayList<PhraseClassificationAttribute> getModerationCategories(String sentence) {
        System.out.println("[MOCK] Using mock moderation data for sentence: " + sentence);
        return super.getModerationCategories(sentence);
    }

    @Override
    public SentenceStructure getStructure(String sentence) {
        // Simulating the parsed structure from provided sample JSON
        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        nouns.add("Today");
        nouns.add("day");
        verbs.add("is");
        adjectives.add("good");

        return new SentenceStructure(
                nouns.toArray(new String[0]),
                verbs.toArray(new String[0]),
                adjectives.toArray(new String[0]));
    }
}
