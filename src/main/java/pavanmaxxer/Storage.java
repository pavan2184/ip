package pavanmaxxer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Loads and saves tasks using the application's text storage format.
 */
public final class Storage {
    private final Path filePath;

    /**
     * Creates storage that reads and writes the supplied file.
     *
     * @param filePath Relative or absolute task data path.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads saved tasks, creating an empty data file on first use.
     *
     * @return Loaded tasks in their saved order.
     * @throws PavanmaxxerException If the file cannot be read or is corrupt.
     */
    public ArrayList<Task> load() throws PavanmaxxerException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                return new ArrayList<>();
            }

            ArrayList<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(filePath)) {
                if (!line.isBlank()) {
                    tasks.add(decodeTask(line));
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new PavanmaxxerException("Unable to load saved tasks.");
        }
    }

    /**
     * Saves the current task list, replacing the previous stored snapshot.
     *
     * @param tasks Task list to save.
     * @throws PavanmaxxerException If the file cannot be written.
     */
    public void save(TaskList tasks) throws PavanmaxxerException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks.asList()) {
                lines.add(encodeTask(task));
            }
            Files.write(filePath, lines);
        } catch (IOException exception) {
            throw new PavanmaxxerException("Unable to save tasks.");
        }
    }

    private String encodeTask(Task task) {
        String state = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D | " + state + " | " + deadline.getDescription()
                    + " | " + deadline.getBy();
        }
        if (task instanceof Event event) {
            return "E | " + state + " | " + event.getDescription()
                    + " | " + event.getFrom() + " | " + event.getTo();
        }
        return "T | " + state + " | " + task.getDescription();
    }

    private Task decodeTask(String line) throws PavanmaxxerException {
        String[] fields = line.split(" \\| ", -1);
        if (fields.length < 3 || fields[2].isBlank()) {
            throw new PavanmaxxerException("Saved task data is corrupted.");
        }
        boolean isDone = parseState(fields[1]);
        Task task;
        try {
            switch (fields[0]) {
                case "T":
                    if (fields.length != 3) {
                        throw new PavanmaxxerException("Saved task data is corrupted.");
                    }
                    task = new Todo(fields[2]);
                    break;
                case "D":
                    if (fields.length != 4) {
                        throw new PavanmaxxerException("Saved task data is corrupted.");
                    }
                    task = new Deadline(fields[2], LocalDate.parse(fields[3]));
                    break;
                case "E":
                    if (fields.length != 5) {
                        throw new PavanmaxxerException("Saved task data is corrupted.");
                    }
                    task = new Event(fields[2], fields[3], fields[4]);
                    break;
                default:
                    throw new PavanmaxxerException("Saved task data is corrupted.");
            }
        } catch (DateTimeParseException exception) {
            throw new PavanmaxxerException("Saved task data is corrupted.");
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private boolean parseState(String state) throws PavanmaxxerException {
        if (state.equals("1")) {
            return true;
        }
        if (state.equals("0")) {
            return false;
        }
        throw new PavanmaxxerException("Saved task data is corrupted.");
    }
}
