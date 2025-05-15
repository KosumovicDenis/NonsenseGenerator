package unipd.ddkk.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SentenceGenerator implements Generator {
    private final Dictionary dictionary;
    private final Random random = new Random();

    public SentenceGenerator() {
        this.dictionary = new Dictionary();
    }

    @Override
    public String generatePhrase(SentenceStructure input) {
        List<String> names = input.names != null ? new ArrayList<>(Arrays.asList(input.names)) : new ArrayList<>();
        List<String> verbs = input.verbs != null ? new ArrayList<>(Arrays.asList(input.verbs)) : new ArrayList<>();
        List<String> adjectives = input.adjectives != null ? new ArrayList<>(Arrays.asList(input.adjectives)) : new ArrayList<>();

        // Flag to track if we already used one input element
        boolean[] usedInputElement = new boolean[] {false};

        String template = dictionary.getRandom(GrammaticalElement.SENTENCE_STRUCTURE);
        return fillTemplate(template, names, verbs, adjectives, usedInputElement);
    }

    private String fillTemplate(String template,
                                List<String> names,
                                List<String> verbs,
                                List<String> adjectives,
                                boolean[] usedInputElement) {
        Pattern pattern = Pattern.compile("\\[(noun|adjective|verb|sentence)]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(template);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String placeholder = matcher.group(1).toLowerCase();
            String replacement;

            switch (placeholder) {
                case "noun":
                    replacement = takeOrRandom(names, GrammaticalElement.NOUN, usedInputElement);
                    break;
                case "adjective":
                    replacement = takeOrRandom(adjectives, GrammaticalElement.ADJECTIVE, usedInputElement);
                    break;
                case "verb":
                    replacement = takeOrRandom(verbs, GrammaticalElement.VERB_PRESENT_THIRD_PERSON, usedInputElement);
                    break;
                case "sentence":
                    replacement = fillTemplate(dictionary.getRandom(GrammaticalElement.SENTENCE_STRUCTURE),
                                               names, verbs, adjectives, usedInputElement);
                    break;
                default:
                    replacement = "[unknown]";
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Picks an element either from the user list or dictionary:
     * - If no input element has been used yet, takes from input list (if available) and sets usedInputElement = true
     * - Otherwise, picks randomly between input list (if not empty) and dictionary
     */
    private String takeOrRandom(List<String> userList, GrammaticalElement fallbackElement, boolean[] usedInputElement) {
        if (!usedInputElement[0]) {
            if (userList != null && !userList.isEmpty()) {
                usedInputElement[0] = true;
                return userList.remove(random.nextInt(userList.size()));
            }
            return dictionary.getRandom(fallbackElement);
        } else {
            // 20% chance to pick from input list (if not empty), 80% from dictionary
            boolean pickFromInput = userList != null && !userList.isEmpty() && random.nextDouble() < 0.2;
            if (pickFromInput) {
                return userList.remove(random.nextInt(userList.size()));
            } else {
                return dictionary.getRandom(fallbackElement);
            }
        }
    }
}

