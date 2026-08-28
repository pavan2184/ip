import java.util.List;

/**
 * Checks task-list mutations and case-insensitive description search.
 */
public class TaskListTest {
    public static void main(String[] args) {
        Todo first = new Todo("Read Book");
        TaskList tasks = new TaskList(List.of(first));

        tasks.add(new Todo("write report"));
        assert tasks.size() == 2;
        assert tasks.get(0) == first;

        assert tasks.mark(0) == first;
        assert first.isDone();
        assert tasks.unmark(0) == first;
        assert !first.isDone();

        assert tasks.find("book").equals(List.of(first));
        assert tasks.find("REPORT").equals(List.of(tasks.get(1)));

        Task deleted = tasks.delete(1);
        assert deleted.getDescription().equals("write report");
        assert tasks.size() == 1;
        assert tasks.asList().equals(List.of(first));

        System.out.println("PASS: task-list operations preserve task order and state");
    }
}
