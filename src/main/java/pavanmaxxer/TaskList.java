package pavanmaxxer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Owns the ordered collection of tasks and its collection-level operations.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing a defensive copy of the supplied tasks.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task at the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the specified index.
     *
     * @param index Zero-based task index.
     * @return Deleted task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks and returns the task at the specified index as completed.
     *
     * @param index Zero-based task index.
     * @return Updated task.
     */
    public Task mark(int index) {
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks and returns the task at the specified index as incomplete.
     *
     * @param index Zero-based task index.
     * @return Updated task.
     */
    public Task unmark(int index) {
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns tasks whose descriptions contain every keyword, ignoring case.
     *
     * @param keywords Keywords to match against task descriptions.
     * @return Matching tasks in their original order.
     */
    public List<Task> find(String... keywords) {
        return tasks.stream()
                .filter(task -> containsEveryKeyword(task, keywords))
                .toList();
    }

    private static boolean containsEveryKeyword(Task task, String... keywords) {
        String normalizedDescription = task.getDescription()
                .toLowerCase(Locale.ENGLISH);
        return Arrays.stream(keywords)
                .map(keyword -> keyword.toLowerCase(Locale.ENGLISH))
                .allMatch(normalizedDescription::contains);
    }

    /**
     * Returns an unmodifiable snapshot of the stored tasks.
     *
     * @return Unmodifiable task-list snapshot.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
