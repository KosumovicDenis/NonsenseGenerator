package unipd.ddkk.core;

import java.util.ArrayList;

public class Service {
    private final Generator generator;
    private final History hist;

    public Service() {
        this.generator = new SentenceGenerator();

        hist = new History("history.txt");
    }

    public String getSyntacticTree(int index, String input) {
        // Placeholder for syntactic tree logic
        return "Tree(" + input + ")";
    }

    public ArrayList<GeneratedSentence> generatePhrases(String input, int count) {
        ArrayList<GeneratedSentence> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String content = generator.generatePhrase("InputString"); // Placeholder input
            result.add(new GeneratedSentence(content));
        }
        hist.push(result);
        return result;
    }

    public ArrayList<GeneratedSentence> getHistory() {
        return hist.get();
    }

}
