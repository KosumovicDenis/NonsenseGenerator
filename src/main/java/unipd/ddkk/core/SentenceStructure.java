package unipd.ddkk.core;

import com.fasterxml.jackson.databind.JsonNode;

public class SentenceStructure {
    public final String[] names;
    public final String[] verbs;
    public final String[] adjectives;
    public final JsonNode structure;

    public SentenceStructure(String[] names, String[] verbs, String[] adjectives, JsonNode node) {
        this.names = names;
        this.verbs = verbs;
        this.adjectives = adjectives;
        this.structure = node;
    }
}
