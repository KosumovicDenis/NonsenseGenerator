package unipd.ddkk.core;

import java.util.ArrayList;

public class Controller {
    Service s;

    public Controller() {
        this.s = new Service();
    }

    public GenerationResult generate(String input, int count) {
            return s.generatePhrases(input, count);
    }

    public ArrayList<GeneratedSentence> getHistory() {
        return s.getHistory();
    }
}
