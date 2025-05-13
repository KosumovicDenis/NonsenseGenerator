package unipd.ddkk.core;

public class SentenceGenerator implements Generator {

   private final Dictionary dictionary;
   private final APICaller apiCaller;

   private int remove_me;

   public SentenceGenerator() {
      this.dictionary = new Dictionary();
      this.apiCaller = new GoogleAPICaller();

      remove_me = 0;
   }

   @Override
   public String generatePhrase(String input) {
      return "Generated phrase " + remove_me++;
   }
}
