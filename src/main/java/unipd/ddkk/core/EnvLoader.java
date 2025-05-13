package unipd.ddkk.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
   public static Map<String, String> loadEnv(String path) throws EnvFileNotFoundException {
      Map<String, String> env = new HashMap<>();

      try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
         String line;
         while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty() || line.startsWith("#"))
               continue;
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
               env.put(parts[0].trim(), parts[1].trim());
            }
         }
      } catch (java.io.FileNotFoundException e) {
         // File not found: gracefully notify
         throw new EnvFileNotFoundException(".env file not found! Create one starting from the file \".env.example\"");
      } catch (IOException e) {
         e.printStackTrace();
      }

      if (env.isEmpty()) {
         throw new EnvFileNotFoundException(".env file is empty or malformed!");
      }

      return env;
   }
}
