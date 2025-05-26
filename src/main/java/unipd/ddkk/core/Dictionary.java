package unipd.ddkk.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Dictionary {
    private final Random random = new Random();
    private final Map<GrammaticalElement, List<String>> data = new EnumMap<>(GrammaticalElement.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public Dictionary() {
        try {
            loadElements();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dictionary data", e);
        }
    }

    private void loadElements() throws IOException {
        File basePath = Paths.get("assets").toFile();

        List<String> adjectives = mapper.readValue(new File(basePath, "adjective.json"), new TypeReference<List<String>>() {});
        List<String> nouns = mapper.readValue(new File(basePath, "noun.json"), new TypeReference<List<String>>() {});
        List<String> templates = mapper.readValue(new File(basePath, "template.json"), new TypeReference<List<String>>() {});
        List<String> thirdPersonVerbs = mapper.readValue(new File(basePath, "verb.json"), new TypeReference<List<String>>() {});

        data.put(GrammaticalElement.VERB_PRESENT_THIRD_PERSON, thirdPersonVerbs);
        data.put(GrammaticalElement.ADJECTIVE, adjectives);
        data.put(GrammaticalElement.NOUN, nouns);
        data.put(GrammaticalElement.SENTENCE_STRUCTURE, templates);
    }

    public void updateDictionary(SentenceStructure inputSentenceStructure) {
        // Add unique adjectives
        for (String adj : inputSentenceStructure.adjectives) {
            if (!data.get(GrammaticalElement.ADJECTIVE).contains(adj)) {
                data.get(GrammaticalElement.ADJECTIVE).add(adj);
            }
        }

        // Add unique nouns
        for (String noun : inputSentenceStructure.names) {
            if (!data.get(GrammaticalElement.NOUN).contains(noun)) {
                data.get(GrammaticalElement.NOUN).add(noun);
            }
        }

        JsonNode tokens = inputSentenceStructure.structure.get("tokens");

        for (JsonNode token : tokens) {
            String text = token.get("text").get("content").asText();
            String tag = token.get("partOfSpeech").get("tag").asText();
            String person = token.get("partOfSpeech").get("person").asText();
            String tense = token.get("partOfSpeech").get("tense").asText();

            if (tag.equals("VERB")) {
                GrammaticalElement ge = null;
                if (tense.equals("PRESENT") && person.equals("THIRD")) {
                    ge = GrammaticalElement.VERB_PRESENT_THIRD_PERSON;
                }

                if (ge != null && !data.get(ge).contains(text)) {
                    data.get(ge).add(text);
                }
            }
        }
    }

    public String getRandom(GrammaticalElement e) {
        List<String> elements = data.get(e);
        if (elements == null || elements.isEmpty()) {
            return "[no data for " + e + "]";
        }
        return elements.get(random.nextInt(elements.size()));
    }

    public List<String> getAllElements(GrammaticalElement e) {
        return data.get(e);
    }

}
