package unipd.ddkk.core;

public class SentenceStructure {
    public final String[] names;
    public final String[] verbs;
    public final String[] adjectives;

    public SentenceStructure(String[] names, String[] verbs, String[] adjectives) {
        this.names = names;
        this.verbs = verbs;
        this.adjectives = adjectives;
    }
}
