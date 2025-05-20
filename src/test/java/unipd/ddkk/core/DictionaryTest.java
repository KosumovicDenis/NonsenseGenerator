package unipd.ddkk.core;

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
        injectTestData(dictionary);
    }

    @Test
    public void testGetRandom() {
        String result = dictionary.getRandom(GrammaticalElement.ADJECTIVE);
        assertTrue(Arrays.asList("quick", "lazy", "sleepy").contains(result));
    }

    @Test
    public void testGetRandomWithEmptyElement() {
        String result = dictionary.getRandom(GrammaticalElement.VERB_PRESENT_THIRD_PERSON);
        assertEquals("[no data for VERB_PRESENT_THIRD_PERSON]", result);
    }

    @Test
    public void testUpdateDictionary() {
        SentenceStructure newStructure = new SentenceStructure(
            new String[] { "fox", "dog" },
            new String[] { "run", "jump" },
            new String[] { "blue", "green" }
        );

        dictionary.updateDictionary(newStructure);

        String adj = dictionary.getRandom(GrammaticalElement.ADJECTIVE);
        assertTrue(Arrays.asList("blue", "green").contains(adj));

        String noun = dictionary.getRandom(GrammaticalElement.NOUN);
        assertTrue(Arrays.asList("fox", "dog").contains(noun));
    }

    // Use reflection to inject mock data into the private 'data' field
    private void injectTestData(Dictionary dict) throws Exception {
        Field dataField = Dictionary.class.getDeclaredField("data");
        dataField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<GrammaticalElement, List<String>> mockData = (Map<GrammaticalElement, List<String>>) dataField.get(dict);

        mockData.clear(); // Clear existing file-loaded data
        mockData.put(GrammaticalElement.ADJECTIVE, Arrays.asList("quick", "lazy", "sleepy"));
        mockData.put(GrammaticalElement.NOUN, Arrays.asList("cat", "dog", "mouse"));
        mockData.put(GrammaticalElement.SENTENCE_STRUCTURE, Arrays.asList("{adj} {noun} {verb}"));
        mockData.put(GrammaticalElement.VERB_PRESENT, Arrays.asList("runs", "jumps", "sleeps"));
        mockData.put(GrammaticalElement.VERB_PRESENT_THIRD_PERSON, Collections.emptyList());
    }
}
