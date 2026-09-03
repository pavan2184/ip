package pavanmaxxer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task-list collection operations.
 */
public class TaskListTest {
    @Test
    void delete_middleTask_closesGap() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("A"));
        tasks.add(new Todo("B"));
        tasks.add(new Todo("C"));

        Task deleted = tasks.delete(1);

        assertEquals("B", deleted.getDescription());
        assertEquals(List.of("A", "C"), tasks.asList().stream()
                .map(Task::getDescription)
                .toList());
    }

    @Test
    void markAndUnmark_validIndex_updatesTaskState() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertTrue(tasks.mark(0).isDone());
        assertFalse(tasks.unmark(0).isDone());
    }

    @Test
    void asList_returnedList_cannotMutateTaskList() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(UnsupportedOperationException.class, () ->
                tasks.asList().add(new Todo("write book")));
        assertEquals(1, tasks.size());
    }

    @Test
    void find_mixedCaseKeyword_returnsMatchesInOriginalOrder() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("Read Book"));
        tasks.add(new Todo("buy milk"));
        tasks.add(new Deadline(
                "return book", LocalDate.parse("2026-08-31")));

        assertEquals(List.of("Read Book", "return book"),
                tasks.find("BOOK").stream()
                        .map(Task::getDescription)
                        .toList());
    }
}
