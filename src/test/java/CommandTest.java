/**
 * Checks how raw user input is classified into supported commands.
 */
public class CommandTest {
    public static void main(String[] args) {
        assertCommand(Command.BYE, Command.from("bye"));
        assertCommand(Command.LIST, Command.from("list"));
        assertCommand(Command.MARK, Command.from("mark 1"));
        assertCommand(Command.UNMARK, Command.from("unmark 1"));
        assertCommand(Command.DELETE, Command.from("delete 1"));
        assertCommand(Command.TODO, Command.from("todo read book"));
        assertCommand(Command.DEADLINE, Command.from("deadline submit /by Friday"));
        assertCommand(Command.EVENT, Command.from("event class /from 1 /to 2"));
        assertCommand(Command.UNKNOWN, Command.from("list extra"));
        assertCommand(Command.UNKNOWN, Command.from("byebye"));
        assertCommand(Command.UNKNOWN, Command.from(""));

        System.out.println("PASS: raw input is classified using Command enum values");
    }

    private static void assertCommand(Command expected, Command actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }
}
