package unipd.ddkk.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class GeneratedSentence {
    final String content;
    final Date date;
    ArrayList<PhraseClassificationAttribute> categories;

    private static final String dateContentSeparator = " # ";
    private static final String contentClassificationsSeparator = " @ ";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public GeneratedSentence(String content, ArrayList<PhraseClassificationAttribute> classification) {
        this.content = content;
        this.categories = classification;
        this.date = new Date();
    }

    public GeneratedSentence(String content, ArrayList<PhraseClassificationAttribute> classification, Date date) {
        this.content = content;
        this.date = date;
        this.categories = classification;
    }

    public static GeneratedSentence fromString(String line) {
        try {
            // Split into date and remainder (content + classifications)
            String[] parts = line.split(dateContentSeparator, 2);
            if (parts.length < 2) {
                // Fallback if missing separator
                return new GeneratedSentence(line, new ArrayList<>());
            }

            Date parsedDate = formatter.parse(parts[0].strip());

            // parts[1] contains content + optionally classifications after '@'
            String contentPart = parts[1];
            String content;
            ArrayList<PhraseClassificationAttribute> classifications = new ArrayList<>();

            int atIndex = contentPart.indexOf(contentClassificationsSeparator);
            if (atIndex == -1) {
                // No classifications, only content
                content = contentPart;
            } else {
                content = contentPart.substring(0, atIndex).strip();
                String classificationsPart = contentPart.substring(atIndex + contentClassificationsSeparator.length());
                String[] classificationStrings = classificationsPart.split(" ~ ");

                for (String classStr : classificationStrings) {
                    if (!classStr.isBlank()) {
                        try {
                            classifications.add(PhraseClassificationAttribute.fromString(classStr));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error parsing PhraseClassificationAttribute, skipping... (found \"" + classStr + "\")");
                        }
                    }
                }
            }

            return new GeneratedSentence(content, classifications, parsedDate);

        } catch (ParseException e) {
            // If date parse fails, treat entire line as content with empty categories
            return new GeneratedSentence(line, new ArrayList<>());
        }
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<PhraseClassificationAttribute> getClassification() {
        return categories;
    }

    @Override
    public String toString() {
        String classificationsLine = categories.stream()
                .map(PhraseClassificationAttribute::toString)
                .collect(Collectors.joining(" ~ "));

        return formatter.format(date) + dateContentSeparator + content + contentClassificationsSeparator + classificationsLine;
    }

}
