package unipd.ddkk.core;

import java.util.List;

public interface Generator {
    String generatePhrase(SentenceStructure sentenceStructure, boolean addToDict, String requestedTemplate);
    List<String> getAvailableTemplates();
}
