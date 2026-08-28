import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Checks that tasks can be saved and reconstructed across application runs.
 */
public class PersistenceTest {
    public static void main(String[] args) throws Exception {
        savesAndLoadsAllTaskTypes();
        loadsFromMissingFile();
        rejectsCorruptData();
        System.out.println("PASS: tasks persist across application runs");
    }

    private static void savesAndLoadsAllTaskTypes() throws Exception {
        Path testRoot = Files.createTempDirectory("pavanmaxxer-level7-");
        Path dataFile = testRoot.resolve("data").resolve("pavanmaxxer.txt");

        ArrayList<Task> tasks = new ArrayList<>();
        Todo todo = new Todo("read book");
        todo.markAsDone();
        tasks.add(todo);
        tasks.add(new Deadline("return book", LocalDate.parse("2026-08-31")));
        tasks.add(new Event("project meeting", "2pm", "4pm"));

        Storage storage = new Storage(dataFile);
        storage.save(new TaskList(tasks));
        ArrayList<Task> loaded = storage.load();

        assert Files.exists(dataFile);
        assert loaded.size() == 3;
        assert loaded.get(0).toString().equals("[T][X] read book");
        assert loaded.get(1).toString()
                .equals("[D][ ] return book (by: Aug 31 2026)");
        assert loaded.get(2).toString()
                .equals("[E][ ] project meeting (from: 2pm to: 4pm)");
    }

    private static void loadsFromMissingFile() throws Exception {
        Path testRoot = Files.createTempDirectory("pavanmaxxer-level7-empty-");
        Path dataFile = testRoot.resolve("data").resolve("pavanmaxxer.txt");

        ArrayList<Task> loaded = new Storage(dataFile).load();

        assert loaded.isEmpty();
        assert Files.exists(dataFile);
    }

    private static void rejectsCorruptData() throws Exception {
        Path testRoot = Files.createTempDirectory("pavanmaxxer-level7-corrupt-");
        Path dataFile = testRoot.resolve("pavanmaxxer.txt");
        Files.writeString(dataFile, "T | 0 | \n");

        boolean didThrow = false;
        try {
            new Storage(dataFile).load();
        } catch (PavanmaxxerException exception) {
            didThrow = true;
        }

        assert didThrow;
    }
}
