package unipd.ddkk.core;
import com.fasterxml.jackson.core.type.TypeReference;
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
        List<List<String>> verbPairs = mapper.readValue(new File(basePath, "verb.json"), new TypeReference<List<List<String>>>() {});

        List<String> baseVerbs = new ArrayList<>();
        List<String> thirdPersonVerbs = new ArrayList<>();

        for (List<String> pair : verbPairs) {
            if (pair.size() == 2) {
                baseVerbs.add(pair.get(0));
                thirdPersonVerbs.add(pair.get(1));
            }
        }

        data.put(GrammaticalElement.VERB_PRESENT, baseVerbs);
        data.put(GrammaticalElement.VERB_PRESENT_THIRD_PERSON, thirdPersonVerbs);
        data.put(GrammaticalElement.ADJECTIVE, adjectives);
        data.put(GrammaticalElement.NOUN, nouns);
        data.put(GrammaticalElement.SENTENCE_STRUCTURE, templates);
    
    }

    public void updateDictionary(SentenceStructure inputSentenceStructure) {
        // data.put(GrammaticalElement.VERB_PRESENT, Arrays.asList(inputSentenceStructure.verbs));
        data.put(GrammaticalElement.ADJECTIVE, Arrays.asList(inputSentenceStructure.adjectives));
        data.put(GrammaticalElement.NOUN, Arrays.asList(inputSentenceStructure.names));
    }

    public String getRandom(GrammaticalElement e) {
        List<String> elements = data.get(e);
        if (elements == null || elements.isEmpty()) {
            return "[no data for " + e + "]";
        }
        return elements.get(random.nextInt(elements.size()));
    }
}

