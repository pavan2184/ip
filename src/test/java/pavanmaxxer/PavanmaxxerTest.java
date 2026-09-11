package pavanmaxxer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PavanmaxxerTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_addDuplicate_warnsButAddsAndPersistsTask() {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        Pavanmaxxer pavanmaxxer = new Pavanmaxxer(dataFile);
        pavanmaxxer.getResponse("todo read book");

        assertEquals("This task already exists in your list, but I've added it again:\n"
                        + "  [T][ ] READ BOOK\n"
                        + "Now you have 2 tasks in the list.",
                pavanmaxxer.getResponse("todo READ BOOK"));

        Pavanmaxxer reloadedPavanmaxxer = new Pavanmaxxer(dataFile);
        assertEquals("1.[T][ ] read book\n2.[T][ ] READ BOOK",
                reloadedPavanmaxxer.getResponse("list"));
    }

    @Test
    void getResponse_addThenList_returnsGuiReadyMessages() {
        Pavanmaxxer pavanmaxxer = new Pavanmaxxer(
                temporaryDirectory.resolve("tasks.txt"));

        assertEquals("Got it. I've added this task:\n"
                        + "  [T][ ] read book\n"
                        + "Now you have 1 tasks in the list.",
                pavanmaxxer.getResponse("todo read book"));
        assertEquals("1.[T][ ] read book", pavanmaxxer.getResponse("list"));
    }

    @Test
    void getResponse_unknownCommand_returnsErrorMessage() {
        Pavanmaxxer pavanmaxxer = new Pavanmaxxer(
                temporaryDirectory.resolve("tasks.txt"));

        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(",
                pavanmaxxer.getResponse("hello"));
    }
}
