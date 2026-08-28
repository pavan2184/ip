package pavanmaxxer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests task persistence and malformed storage handling.
 */
public class StorageTest {
    @TempDir
    Path testDirectory;

    @Test
    void saveAndLoad_multipleTaskTypes_preservesState() throws Exception {
        Path dataFile = testDirectory.resolve("data/pavanmaxxer.txt");
        Storage storage = new Storage(dataFile);
        TaskList original = new TaskList();
        original.add(new Todo("read book"));
        original.add(new Deadline(
                "return book", LocalDate.parse("2026-08-31")));
        original.add(new Event("meeting", "2pm", "4pm"));
        original.mark(0);

        storage.save(original);
        TaskList loaded = new TaskList(storage.load());

        assertEquals(original.asList().stream().map(Task::toString).toList(),
                loaded.asList().stream().map(Task::toString).toList());
    }

    @Test
    void load_missingFile_createsFileAndReturnsEmptyList() throws Exception {
        Path dataFile = testDirectory.resolve("data/pavanmaxxer.txt");
        Storage storage = new Storage(dataFile);

        assertTrue(storage.load().isEmpty());
        assertTrue(Files.exists(dataFile));
    }

    @Test
    void load_malformedLine_throwsApplicationException() throws Exception {
        Path dataFile = testDirectory.resolve("pavanmaxxer.txt");
        Files.writeString(dataFile, "T | 0 | \n");

        assertThrows(PavanmaxxerException.class,
                () -> new Storage(dataFile).load());
    }
}
