package unipd.ddkk.core;

import java.util.ArrayList;

public class Controller {
    Service s;

    public Controller(Service ser) {
        s = ser;
    }

    public GenerationResult generate(String input, int count) {
            return s.generatePhrases(input, count);
    }

    public ArrayList<GeneratedSentence> getHistory() {
        return s.getHistory();
    }
}
