package pavanmaxxer;

/**
 * Checks how raw user input is classified into supported commands.
 */
public class CommandTest {
    public static void main(String[] args) throws Exception {
        assertCommand(Command.BYE, Parser.parseCommand("bye"));
        assertCommand(Command.LIST, Parser.parseCommand("list"));
        assertCommand(Command.MARK, Parser.parseCommand("mark 1"));
        assertCommand(Command.UNMARK, Parser.parseCommand("unmark 1"));
        assertCommand(Command.DELETE, Parser.parseCommand("delete 1"));
        assertCommand(Command.TODO, Parser.parseCommand("todo read book"));
        assertCommand(Command.DEADLINE,
                Parser.parseCommand("deadline submit /by 2026-08-31"));
        assertCommand(Command.EVENT,
                Parser.parseCommand("event class /from 1 /to 2"));
        assertCommand(Command.UNKNOWN, Parser.parseCommand("list extra"));
        assertCommand(Command.UNKNOWN, Parser.parseCommand("byebye"));
        assertCommand(Command.UNKNOWN, Parser.parseCommand(""));

        assert Parser.parseTask("todo read book", Command.TODO).toString()
                .equals("[T][ ] read book");
        assert Parser.parseTask(
                "deadline return book /by 2026-08-31", Command.DEADLINE)
                .toString().equals("[D][ ] return book (by: Aug 31 2026)");
        assert Parser.parseTask(
                "event class /from 1pm /to 2pm", Command.EVENT)
                .toString().equals("[E][ ] class (from: 1pm to: 2pm)");

        assert Parser.parseTaskIndex("mark 1", "mark", 2) == 0;
        assert Parser.parseTaskIndex("delete 2", "delete", 2) == 1;
        assertThrows(() -> Parser.parseTask(
                "deadline return book /by 2026-02-30", Command.DEADLINE));
        assertThrows(() -> Parser.parseTask("todo", Command.TODO));
        assertThrows(() -> Parser.parseTask("event class /from 1pm", Command.EVENT));
        assertThrows(() -> Parser.parseTaskIndex("mark one", "mark", 2));
        assertThrows(() -> Parser.parseTaskIndex("mark 3", "mark", 2));

        System.out.println("PASS: commands and arguments are parsed and validated");
    }

    private static void assertCommand(Command expected, Command actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void assertThrows(ThrowingAction action) throws Exception {
        boolean didThrow = false;
        try {
            action.run();
        } catch (PavanmaxxerException exception) {
            didThrow = true;
        }
        assert didThrow;
    }

    @FunctionalInterface
    private interface ThrowingAction {
        void run() throws Exception;
    }
}
