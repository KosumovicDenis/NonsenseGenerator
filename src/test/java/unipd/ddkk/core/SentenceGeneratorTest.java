package unipd.ddkk.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SentenceGeneratorTest {

    private SentenceGenerator generator;
    

    @BeforeEach
    public void setUp() {
        generator = new SentenceGenerator();
    }

    @Test
    public void testGeneratePhraseWithInputsAndTemplate() throws Exception {
        String[] nouns = new String[] { "Davide" };
        String[] verbs = new String[] { "works" };
        String[] adjectives = new String[] { "hard" };

        SentenceStructure sentence = new SentenceStructure(nouns, verbs, adjectives, null);
        String template = "[noun] [verb] [adjective]";

        String result = generator.generatePhrase(sentence, false, template);
        assertNotNull(result);
        assertFalse(result.contains("[noun]"), "Template placeholder [noun] was not replaced.");
        assertFalse(result.contains("[verb]"), "Template placeholder [verb] was not replaced.");
        assertFalse(result.contains("[adjective]"), "Template placeholder [adjective] was not replaced.");

        assertTrue(result.contains("Davide") || result.contains("works") || result.contains("hard"),
                "Input words should be used at least once in the generated phrase.");
    }

    @Test
    public void testGeneratePhraseWithRandomTemplate() {
        String[] nouns = new String[] { "Kosu" };
        String[] verbs = new String[] { "work" };
        String[] adjectives = new String[] { "hard" };

        SentenceStructure sentence = new SentenceStructure(nouns, verbs, adjectives, null);

        String result = generator.generatePhrase(sentence, false, "");
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Generated phrase should not be empty.");
    }

    @Test
    public void testGeneratePhraseUsesDictionaryIfNoInputs() {
        String[] nouns = null;
        String[] verbs = null;
        String[] adjectives = null;

        SentenceStructure sentence = new SentenceStructure(nouns, verbs, adjectives, null);

        String template = "[noun] [verb] [adjective]";
        String result = generator.generatePhrase(sentence, false, template);

        assertNotNull(result);
        assertFalse(result.contains("[noun]"));
        assertFalse(result.contains("[verb]"));
        assertFalse(result.contains("[adjective]"));
    }

    @Test
    public void testRecursiveSentencePlaceholder() {
        String[] nouns = new String[] { "Alice" };
        String[] verbs = new String[] { "runs" };
        String[] adjectives = new String[] { "fast" };

        SentenceStructure sentence = new SentenceStructure(nouns, verbs, adjectives, null);

    // Test a sentence that includes [sentence] recursively
    String template = "Intro [sentence]";
    String result = generator.generatePhrase(sentence, false, template);

    assertNotNull(result);
    assertTrue(result.startsWith("Intro"), "Recursive sentence should be inserted after 'Intro'");
    assertFalse(result.contains("[sentence]"), "Recursive [sentence] placeholder should be resolved");
    }

    @Test
    public void testGetAvailableTemplatesReturnsList() {
    List<String> templates = generator.getAvailableTemplates();
    assertNotNull(templates);
    assertFalse(templates.isEmpty(), "Templates should not be empty if dictionary has entries.");
    }

}
