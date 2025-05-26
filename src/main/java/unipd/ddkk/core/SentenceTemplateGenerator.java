package unipd.ddkk.core;

import java.util.Random;

public class SentenceTemplateGenerator {

   private static final String[] subjects = {
         "the [adjective] [noun]", "a [adjective] [noun]", "every [noun]", "some [adjective] [noun]"
   };

   private static final String[] verbs = {
         "[verb]", "decided to [verb]", "tried to [verb]", "managed to [verb]"
   };

   private static final String[] objects = {
         "the [adjective] [noun]", "a [noun]", "its own [noun]", "another [adjective] [noun]"
   };

   private static final String[] prepositionalPhrases = {
         "before sunset", "in the dark", "with great care", "without hesitation", "during the storm"
   };

   private static final String[] subordinatingConjunctions = {
         "although", "because", "while", "if", "when"
   };

   private static final Random rand = new Random();

   public static String generateTemplate() {
      boolean useComplex = rand.nextBoolean(); // mix simple and complex sentences
      String sentence;

      if (useComplex) {
         String subConj = subordinatingConjunctions[rand.nextInt(subordinatingConjunctions.length)];
         sentence = subConj + " " + generateClause() + ", " + generateClause() + ".";
      } else {
         sentence = generateClause() + ".";
      }

      return capitalize(sentence);
   }

   private static String generateClause() {
      String subject = subjects[rand.nextInt(subjects.length)];
      String verb = verbs[rand.nextInt(verbs.length)];
      String object = objects[rand.nextInt(objects.length)];

      String base = subject + " " + verb + " " + object;

      if (rand.nextBoolean()) {
         base += " " + prepositionalPhrases[rand.nextInt(prepositionalPhrases.length)];
      }

      return base;
   }

   private static String capitalize(String sentence) {
      return sentence.substring(0, 1).toUpperCase() + sentence.substring(1);
   }

   public static void main(String[] args) {
      String template = generateTemplate();
      System.out.println(template);
   }
}
