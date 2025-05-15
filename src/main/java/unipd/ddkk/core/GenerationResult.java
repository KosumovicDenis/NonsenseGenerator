package unipd.ddkk.core;

import java.util.ArrayList;

public class GenerationResult {
    public final ArrayList<GeneratedSentence> sentences;
    public final SentenceStructure syntaxTree;

    public GenerationResult(ArrayList<GeneratedSentence> sentences, SentenceStructure syntaxTree) {
        this.sentences    = sentences;
        this.syntaxTree   = syntaxTree;
    }
}

