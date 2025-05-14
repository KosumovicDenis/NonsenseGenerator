package unipd.ddkk.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

public class SentenceGenerator implements Generator {
    private final Dictionary dictionary;
    private final Random random = new Random();

    public SentenceGenerator() {
        this.dictionary = new Dictionary();
    }

    @Override
    public String generatePhrase(SentenceStructure input) {
        String template = dictionary.getRandom(GrammaticalElement.SENTENCE_STRUCTURE);
        return fillTemplate(template);
    }

    private String fillTemplate(String template) {
        Pattern pattern = Pattern.compile("\\[(noun|adjective|verb|sentence)]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(template);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String placeholder = matcher.group(1).toLowerCase();
            String replacement;

            switch (placeholder) {
                case "noun":
                replacement = dictionary.getRandom(GrammaticalElement.NOUN);
                break;
                case "adjective":
                replacement = dictionary.getRandom(GrammaticalElement.ADJECTIVE);
                break;
                case "verb":
                GrammaticalElement[] tenses = {
                    GrammaticalElement.VERB_PRESENT,
                    GrammaticalElement.VERB_PRESENT_THIRD_PERSON
                };
                replacement = dictionary.getRandom(tenses[random.nextInt(tenses.length)]);
                break;
                case "sentence":
                replacement = fillTemplate(dictionary.getRandom(GrammaticalElement.SENTENCE_STRUCTURE));
                break;
                default:
                replacement = "[unknown]";
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }

}
