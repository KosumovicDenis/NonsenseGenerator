package unipd.ddkk.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class EnvLoaderTest {

    @Test
    public void testFileNotFoundThrowsException() {
        String invalidPath = "nonexistent.env";
        assertThrows(
            EnvFileNotFoundException.class,
            () -> EnvLoader.loadEnv(invalidPath)
        );
    }

    @Test
    public void testEmptyFileThrowsException() throws IOException {
        Path tempFile = Files.createTempFile("empty", ".env");

        assertThrows(
            EnvFileNotFoundException.class,
            () -> EnvLoader.loadEnv(tempFile.toString())
        );
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testMalformedFileThrowsException() throws IOException {
        Path tempFile = Files.createTempFile("wrong", ".env");
        Files.writeString(tempFile, "something wrong\n\nalso wrong");

        assertThrows(
            EnvFileNotFoundException.class,
            () -> EnvLoader.loadEnv(tempFile.toString())
        );
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testValidEnvFileParsesCorrectly() throws IOException, EnvFileNotFoundException {
        Path tempFile = Files.createTempFile("valid", ".env");
        Files.writeString(tempFile, "KEY=value\nACCESS_TOKEN=123");

        Map<String, String> env = EnvLoader.loadEnv(tempFile.toString());
        assertEquals(2, env.size());
        assertEquals("value", env.get("KEY"));
        assertEquals("123", env.get("ACCESS_TOKEN"));

        Files.deleteIfExists(tempFile);
    }
}
