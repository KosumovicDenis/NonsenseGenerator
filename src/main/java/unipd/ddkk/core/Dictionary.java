package unipd.ddkk.core;

import java.util.Random;

public class Dictionary {
    private final Random random = new Random();

    public void updateDictionary(String input) {
        // TODO
    }

    public SentenceStructure getSentenceStructure() {
        return new SentenceStructure(
                new String[] { "Alice", "Bob", "Charlie" },
                new String[] { "runs", "jumps", "codes" },
                new String[] { "quick", "lazy", "happy" });
    }

    public String getRandom(GrammaticalElement e) {
        System.out.println("TODO get random");
        switch (e) {
            case GrammaticalElement.VERB_PRESENT:
                System.out.println("am eating");
                break;
            case GrammaticalElement.VERB_PAST:
                System.out.println("ate");
                break;
            case GrammaticalElement.VERB_FUTURE:
                System.out.println("will eat");
                break;
            case GrammaticalElement.SENTENCE_STRUCTURE:
                System.out.println("[noun] [verb] [adjective].");
                break;
            case GrammaticalElement.ADJECTIVE:
                System.out.println("big");
                break;
            case GrammaticalElement.NOUN:
                System.out.println("apple");
                break;
        }
        return "TODO";
    }
}
