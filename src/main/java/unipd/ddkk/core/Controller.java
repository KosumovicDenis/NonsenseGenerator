package unipd.ddkk.core;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    Service s;

    public Controller() {
        this.s = new Service();
    }

    public GenerationResult generate(String input, boolean addToDict, int count, String selectedTemplate) {
        return s.generatePhrases(input, addToDict, count, selectedTemplate, true);
    }

    public List<String> getAvailableTemplates() {
        return s.getAvailableTemplates();
    }

    public ArrayList<GeneratedSentence> getHistory() {
        return s.getHistory();
    }
}
