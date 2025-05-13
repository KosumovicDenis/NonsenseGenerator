package unipd.ddkk.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratedSentence {
    public final String content;
    public final Date date;

    private static final String separator = " # ";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public GeneratedSentence(String content) {
        this.content = content;
        this.date = new Date();
    }

    public GeneratedSentence(String content, Date date) {
        this.content = content;
        this.date = date;
    }

    public static GeneratedSentence fromString(String line) {
        // Expected format: 2025-05-13 14:30:00 | Actual content here
        String[] parts = line.split(separator, 2);
        try {
            Date parsedDate = formatter.parse(parts[0].strip());
            return new GeneratedSentence(parts[1], parsedDate);
        } catch (ParseException | ArrayIndexOutOfBoundsException e) {
            // Fallback: treat the line as plain content if format fails
            return new GeneratedSentence(line);
        }
    }

    @Override
    public String toString() {
        return formatter.format(date) + separator + content;
    }
}
