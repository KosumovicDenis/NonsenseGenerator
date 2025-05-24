package unipd.ddkk.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {

    private Dictionary dictionary;

    @BeforeEach
    public void setUp() throws Exception {
        dictionary = new Dictionary();
        injectEmptyData(dictionary);
    }

    // Injects a clean EnumMap into the private `data` field using reflection avoiding the need for file I/O.
    private void injectEmptyData(Dictionary dictionary) throws Exception {
        Field field = Dictionary.class.getDeclaredField("data");
        field.setAccessible(true);

        Map<GrammaticalElement, List<String>> testData = new EnumMap<>(GrammaticalElement.class);
        for (GrammaticalElement ge : GrammaticalElement.values()) {
            testData.put(ge, new ArrayList<>());
        }

        field.set(dictionary, testData);
    }


    // Verifies that unique adjectives and nouns from SentenceStructure are added to the dictionary
    // and that duplicates are not added.
    @Test
    public void testUpdateDictionaryAdjectivesNouns() throws Exception {
        String[] adjectives = {"brave", "strong"};
        String[] names = {"lion", "tiger"};
        String[] verbs = {};

        JsonNode structure = new ObjectMapper().readTree("{ \"tokens\": [] }");
        SentenceStructure sentence = new SentenceStructure(names, verbs, adjectives, structure);

        dictionary.updateDictionary(sentence);

        assertTrue(dictionary.getAllElements(GrammaticalElement.ADJECTIVE).contains("brave"));
        assertTrue(dictionary.getAllElements(GrammaticalElement.NOUN).contains("tiger"));
    }

    // Checks that verbs from the input structure are categorized correctly by tense and person
    // into the respective grammatical elements.
    @Test
    public void testUpdateDictionaryVerbs() throws Exception {
        JsonNode structure = new ObjectMapper().readTree("""
        {
          "tokens": [
            {
              "text": { "content": "run" },
              "partOfSpeech": {
                "tag": "VERB",
                "tense": "PRESENT",
                "person": "FIRST"
              }
            },
            {
              "text": { "content": "runs" },
              "partOfSpeech": {
                "tag": "VERB",
                "tense": "PRESENT",
                "person": "THIRD"
              }
            },
            {
              "text": { "content": "ran" },
              "partOfSpeech": {
                "tag": "VERB",
                "tense": "PAST",
                "person": "FIRST"
              }
            },
            {
              "text": { "content": "will run" },
              "partOfSpeech": {
                "tag": "VERB",
                "tense": "FUTURE",
                "person": "SECOND"
              }
            }
          ]
        }
        """);

        SentenceStructure sentence = new SentenceStructure(new String[0], new String[0], new String[0], structure);

        dictionary.updateDictionary(sentence);

        assertTrue(dictionary.getAllElements(GrammaticalElement.VERB_PRESENT).contains("run"));
        assertTrue(dictionary.getAllElements(GrammaticalElement.VERB_PRESENT_THIRD_PERSON).contains("runs"));
        assertTrue(dictionary.getAllElements(GrammaticalElement.VERB_PAST).contains("ran"));
        assertTrue(dictionary.getAllElements(GrammaticalElement.VERB_FUTURE).contains("will run"));
    }

    // Ensures that adjectives already present in the dictionary are not duplicated when updating.
    @Test
    public void testUpdateDictionaryDuplicateEntry() throws Exception {
        injectEmptyData(dictionary); // reset

        // Manually add "happy" to ADJECTIVE list
        dictionary.getAllElements(GrammaticalElement.ADJECTIVE).add("happy");

        SentenceStructure sentence = new SentenceStructure(
                new String[0],
                new String[0],
                new String[]{"happy"}, // duplicate
                new ObjectMapper().readTree("{ \"tokens\": [] }")
        );

        dictionary.updateDictionary(sentence);

        List<String> adj = dictionary.getAllElements(GrammaticalElement.ADJECTIVE);
        assertEquals(1, adj.stream().filter("happy"::equals).count());
    }

    // Tests that getRandom returns a valid element when the list for that grammatical element is not empty.
    @Test
    public void testGetRandom() {
        dictionary.getAllElements(GrammaticalElement.NOUN).add("elephant");
        dictionary.getAllElements(GrammaticalElement.NOUN).add("giraffe");

        String random = dictionary.getRandom(GrammaticalElement.NOUN);

        assertNotNull(random);
        assertTrue(List.of("elephant", "giraffe").contains(random));
    }

    // Verifies that getRandom returns a fallback string if no data is available for the requested grammatical element.
    @Test
    public void testGetRandomEmptyInput() {
        String result = dictionary.getRandom(GrammaticalElement.SENTENCE_STRUCTURE);
        assertTrue(result.contains("no data for"));
    }

    // Confirms that getAllElements returns the correct list of elements and allows modifications.
    @Test
    public void testGetAllElements() {
        List<String> nouns = dictionary.getAllElements(GrammaticalElement.NOUN);
        assertNotNull(nouns);
        assertTrue(nouns.isEmpty());

        nouns.add("zebra");

        assertEquals(List.of("zebra"), dictionary.getAllElements(GrammaticalElement.NOUN));
    }
}
