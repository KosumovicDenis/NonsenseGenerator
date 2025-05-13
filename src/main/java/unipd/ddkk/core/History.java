package unipd.ddkk.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class History {
   private ArrayList<GeneratedSentence> history;
   private String fileName;

   public History(String filePath) {
      this.fileName = filePath;
      history = loadHistoryFromFile(filePath);
   }

   private static ArrayList<GeneratedSentence> loadHistoryFromFile(String filePath) {
      ArrayList<GeneratedSentence> hist = new ArrayList<>();

      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
         String line;
         while ((line = br.readLine()) != null) {
            hist.add(GeneratedSentence.fromString(line));
         }
      } catch (java.io.FileNotFoundException e) {
         // File not found: it's okay if the file doesn't exist yet
         System.out.println("History file not found. Starting with empty history.");
      } catch (IOException e) {
         e.printStackTrace();
      }
      return hist;
   }

   private void saveHistoryToFile(String filePath) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
         for (GeneratedSentence str : history) {
            writer.write(str.toString());
            writer.write(System.lineSeparator());
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public ArrayList<GeneratedSentence> get() {
      return history;
   }

   public void push(GeneratedSentence lineToPush) {
      history.add(lineToPush);
      saveHistoryToFile(fileName);
   }

   public void push(ArrayList<GeneratedSentence> sentences) {
      history.addAll(sentences);
      saveHistoryToFile(fileName);
   }
}