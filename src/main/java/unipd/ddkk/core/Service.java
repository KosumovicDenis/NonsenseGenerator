package unipd.ddkk.core;

import java.util.ArrayList;

public class Service {
    private final Generator generator;
    private final History hist;
    private final APICaller apiCaller;

    public Service() {
        this.generator = new SentenceGenerator();
        this.apiCaller = new GoogleAPICaller();

        hist = new History("history.txt");
    }

    public String getSyntacticTree(int index, String input) {
        // Placeholder for syntactic tree logic
        return "Tree(" + input + ")";
    }

    public ArrayList<GeneratedSentence> generatePhrases(String input, int count) {
        SentenceStructure inputSentenceStructure = apiCaller.getStructure(input);
        ArrayList<GeneratedSentence> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String content = "";
            int attempts = 0;
            int maxAttempts = 10;
            do {
                content = generator.generatePhrase(inputSentenceStructure);
                attempts++;
            } while (!apiCaller.isValid(content) && attempts < maxAttempts);
            result.add(new GeneratedSentence(content));
        }
        hist.push(result);
        return result;
    }

    public ArrayList<GeneratedSentence> getHistory() {
        return hist.get();
    }

}
