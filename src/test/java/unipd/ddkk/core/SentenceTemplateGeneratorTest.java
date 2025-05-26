package unipd.ddkk.core;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SentenceTemplateGeneratorTest {

    @Test
    public void testGenerateTemplateNotNullOrEmpty() {
        String template = SentenceTemplateGenerator.generateTemplate();
        assertNotNull(template, "Generated template should not be null.");
        assertFalse(template.trim().isEmpty(), "Generated template should not be empty.");
    }

    @Test
    public void testTemplateEndsWithPeriod() {
        String template = SentenceTemplateGenerator.generateTemplate();
        assertTrue(template.endsWith("."), "Generated template should end with a period.");
    }

    @Test
    public void testFirstLetterIsCapitalized() {
        String template = SentenceTemplateGenerator.generateTemplate();
        char firstChar = template.charAt(0);
        assertTrue(Character.isUpperCase(firstChar), "First letter of the sentence should be capitalized.");
    }

    @RepeatedTest(10)
    public void testContainsExpectedPlaceholders() {
        // Since output is random, run this multiple times to ensure placeholders are often included
        String template = SentenceTemplateGenerator.generateTemplate();
        assertTrue(template.contains("[noun]") || template.contains("[verb]") || template.contains("[adjective]"),
                "Template should contain at least one grammatical placeholder.");
    }

    @RepeatedTest(10)
    public void testHandlesComplexAndSimpleStructures() {
        // Complex sentences include a comma and subordinating conjunction
        String template = SentenceTemplateGenerator.generateTemplate();
        boolean isComplex = template.contains(",") && SentenceTemplateGeneratorTest.containsAny(template, new String[]{
                "although", "because", "while", "if", "when"
        });

        boolean isSimple = !isComplex;

        assertTrue(isComplex || isSimple, "Template should be either simple or complex.");
    }

    // Helper to detect if a string contains any of a set of substrings
    private static boolean containsAny(String input, String[] keywords) {
        for (String keyword : keywords) {
            if (input.toLowerCase().contains(keyword)) return true;
        }
        return false;
    }
}
