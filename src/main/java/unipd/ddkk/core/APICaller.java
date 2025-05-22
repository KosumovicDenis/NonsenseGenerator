package unipd.ddkk.core;

import java.util.ArrayList;

public interface APICaller {
    SentenceStructure getStructure(String sentence);
    ArrayList<PhraseClassificationAttribute> getModerationCategories(String sentence);
}
