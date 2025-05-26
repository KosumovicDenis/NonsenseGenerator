package unipd.ddkk.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class HistoryTest {

    @Test
    public void testHistoryNonExistentFile() {
        String nonExistentPath = "nonExistent.txt";

        History history = new History(nonExistentPath);
        assertNotNull(history.get(), "History list should not be null.");
        assertEquals(0, history.get().size(), "History should be empty for non-existent file.");
    }

}
