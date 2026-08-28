import java.util.ArrayList;
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
     * @param tasks initial tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public Task mark(int index) {
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    public Task unmark(int index) {
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    public List<Task> find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ENGLISH);
        return tasks.stream()
                .filter(task -> task.getDescription()
                        .toLowerCase(Locale.ENGLISH)
                        .contains(normalizedKeyword))
                .toList();
    }

    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
