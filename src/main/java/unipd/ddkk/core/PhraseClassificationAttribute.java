package unipd.ddkk.core;

public class PhraseClassificationAttribute {
   private String name;
   private float confidence;
   private static final String nameConfidenceSeparator = "=";

   public PhraseClassificationAttribute(String name, float confidence) {
      this.name = name;
      this.confidence = confidence;
   }

   public String getName() {
      return name;
   }

   public float getConfidence() {
      return confidence;
   }

   public static PhraseClassificationAttribute fromString(String str) throws IllegalArgumentException {
      if (str == null || !str.contains(nameConfidenceSeparator)) {
         throw new IllegalArgumentException("Invalid input string: " + str);
      }
      String[] parts = str.split(nameConfidenceSeparator, 2);
      String name = parts[0].trim();
      float confidence;
      try {
         confidence = Float.parseFloat(parts[1].trim());
      } catch (NumberFormatException e) {
         throw new IllegalArgumentException("Invalid confidence value: " + parts[1], e);
      }
      return new PhraseClassificationAttribute(name, confidence);
   }

   @Override
   public String toString() {
      return getName() + nameConfidenceSeparator + getConfidence();
   }
}
