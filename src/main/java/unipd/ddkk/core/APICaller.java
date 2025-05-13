package unipd.ddkk.core;

public interface APICaller {
    SentenceStructure getStructure(String sentence);

    boolean isValid(String input);
}
