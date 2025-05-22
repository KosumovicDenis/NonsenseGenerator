package unipd.ddkk.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Service {
    private final Generator generator;
    private final History hist;
    private final APICaller apiCaller;

    public Service() {
        this.generator = new SentenceGenerator();
        this.apiCaller = new GoogleAPICaller();

        hist = new History("history.txt");
    }

    public List<String> getAvailableTemplates() {
        return generator.getAvailableTemplates();
    }

    public String getSyntacticTree(String input) {
        return "Tree(" + input + ")";
    }

    public static boolean phraseIsValid(ArrayList<PhraseClassificationAttribute> categories) {
        if (categories == null) {
            return false;
        }

        for (PhraseClassificationAttribute attribute : categories) {
            if (attribute.getConfidence() > 0.5) {
                return false;
            }
        }
        return true;
    }

    public GenerationResult generatePhrases(String input, boolean addToDict, int count, String selectedTemplate) {
        SentenceStructure treeStructure = apiCaller.getStructure(input);

        ArrayList<GeneratedSentence> result = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        List<Future<GeneratedSentence>> futures = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            futures.add(executor.submit(() -> {
                String content;
                int attempts = 0, maxAttempts = 10;
                ArrayList<PhraseClassificationAttribute> categories;
                do {
                    content = generator.generatePhrase(treeStructure, addToDict, selectedTemplate);
                    categories = apiCaller.getModerationCategories(content);
                    attempts++;
                } while (!phraseIsValid(categories) && attempts < maxAttempts);
                return new GeneratedSentence(content, categories);
            }));
        }

        for (Future<GeneratedSentence> f : futures) {
            try {
                result.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                result.add(new GeneratedSentence("", new ArrayList<>()));
            }
        }
        executor.shutdown();
        hist.push(result);

        // Restituisci insieme frasi e struttura
        return new GenerationResult(result, treeStructure);
    }

    public ArrayList<GeneratedSentence> getHistory() {
        return hist.get();
    }

}
