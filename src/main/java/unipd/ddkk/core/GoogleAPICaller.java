package unipd.ddkk.core;

import java.util.Map;

public class GoogleAPICaller implements APICaller {

    public GoogleAPICaller() {
        try {
            Map<String, String> config = EnvLoader.loadEnv(".env");
            System.out.println("Key: " + config.get("API_KEY"));
            System.out.println("Access token: " + config.get("API_ACCESS_TOKEN"));
        } catch (EnvFileNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    @Override
    public SentenceStructure getStructure(String sentence) {
        return new SentenceStructure(
                new String[] { "John", "Mary" },
                new String[] { "walks", "runs" },
                new String[] { "fast", "slow" });
    }

    @Override
    public boolean isValid(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
