package unipd.ddkk.core;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Service Class Test
 */
@DisplayName("ServiceClass Test Suite")
public class ServiceTest {

    private Service service;

    @BeforeEach
    void setUp() {
        service = new Service();
    }

    @Test
    @DisplayName("Test default state of MyClass")
    void testDefaultState() {
        // Assert
        assertNotNull(service, "Instance should be initialized");
    }

    @Test
    @DisplayName("Test behavior of generatePhrases() on phrases count with valid input")
    void testGeneratePhrasesCount() {
        // Arrange
        String input = "";
        int count = 5;
        int expectedPhrasesNumber = 5;
        // Act
        GenerationResult result = service.generatePhrases(input, count);

        int phrasesNumber = result.sentences.size();

        // Assert
        assertEquals(expectedPhrasesNumber, phrasesNumber, "Expected number of phrases");
    }

    @Test
    @DisplayName("Test behavior of generatePhrases() on syntax with valid input")
    void testGeneratePhrasesSyntaxTree() {
        // Arrange
        String input = "I like Trains";
        int count = 5;
        // Act
        GenerationResult result = service.generatePhrases(input, count);


        boolean isValid = true;
        for (GeneratedSentence sentence : result.sentences) {
            if (!(contains(sentence, result.syntaxTree.names) || contains(sentence, result.syntaxTree.verbs) || contains(sentence, result.syntaxTree.adjectives))) {
                isValid = false;
                break;
            }
        }
        assertTrue(isValid, "All sentences should contain at least one word from the syntax tree");
    }

    public boolean contains(GeneratedSentence sentenceContent, String[] words) {
        for (String word : words) {
            if (sentenceContent.content.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


}

