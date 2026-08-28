package pavanmaxxer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests command and argument parsing.
 */
public class ParserTest {
    @Test
    void parseTask_validDeadline_returnsFormattedDeadline() throws Exception {
        Task task = Parser.parseTask(
                "deadline return book /by 2026-08-31", Command.DEADLINE);

        assertEquals("[D][ ] return book (by: Aug 31 2026)", task.toString());
    }

    @Test
    void parseTask_impossibleDate_throwsException() {
        assertThrows(PavanmaxxerException.class,
                () -> Parser.parseTask(
                        "deadline return book /by 2026-02-30",
                        Command.DEADLINE));
    }

    @Test
    void parseTaskIndex_firstTask_returnsZero() throws Exception {
        assertEquals(0, Parser.parseTaskIndex("mark 1", "mark", 2));
    }

    @Test
    void parseTaskIndex_outOfRange_throwsException() {
        assertThrows(PavanmaxxerException.class,
                () -> Parser.parseTaskIndex("mark 3", "mark", 2));
    }

    @Test
    void parseTask_emptyTodo_throwsException() {
        assertThrows(PavanmaxxerException.class,
                () -> Parser.parseTask("todo", Command.TODO));
    }

    @Test
    void parseTask_eventWithoutTo_throwsException() {
        assertThrows(PavanmaxxerException.class,
                () -> Parser.parseTask(
                        "event meeting /from 2pm", Command.EVENT));
    }

    @Test
    void parseCommand_unknownText_returnsUnknown() {
        assertEquals(Command.UNKNOWN, Parser.parseCommand("dance now"));
    }

    @Test
    void parseTaskIndex_nonInteger_throwsException() {
        assertThrows(PavanmaxxerException.class,
                () -> Parser.parseTaskIndex("delete first", "delete", 2));
    }
}
