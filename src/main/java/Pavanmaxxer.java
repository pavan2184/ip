import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Pavanmaxxer text-based task manager.
 */
public class Pavanmaxxer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println("Hello! I'm Pavanmaxxer.");
        System.out.println("What can I do for you?");

        mainLoop:
        while (true) {
            String input = scanner.nextLine();
            Command command = Command.from(input);

            try {
                switch (command) {
                case BYE:
                    break mainLoop;
                case LIST:
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                    break;
                case MARK:
                    int taskIndex = parseTaskIndex(input, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = parseTaskIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = parseTaskIndex(input, "delete", tasks.size());
                    Task removedTask = tasks.remove(deleteIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = parseTask(input, command);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    break;
                case UNKNOWN:
                    throw new PavanmaxxerException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (PavanmaxxerException exception) {
                System.out.println("OOPS!!! " + exception.getMessage());
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Parses a validly named task command and validates all required fields.
     */
    private static Task parseTask(String input, Command command) throws PavanmaxxerException {
        if (command == Command.TODO) {
            String description = input.length() == 4 ? "" : input.substring(5).trim();
            if (description.isEmpty()) {
                throw new PavanmaxxerException("The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }

        if (command == Command.DEADLINE) {
            String arguments = input.length() == 8 ? "" : input.substring(9).trim();
            int byIndex = arguments.indexOf("/by");
            if (arguments.isEmpty() || byIndex == 0) {
                throw new PavanmaxxerException("The description of a deadline cannot be empty.");
            }
            if (byIndex < 0) {
                throw new PavanmaxxerException("A deadline needs a /by time.");
            }
            String description = arguments.substring(0, byIndex).trim();
            String by = arguments.substring(byIndex + 3).trim();
            if (description.isEmpty()) {
                throw new PavanmaxxerException("The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new PavanmaxxerException("A deadline needs a /by time.");
            }
            return new Deadline(description, by);
        }

        String arguments = input.length() == 5 ? "" : input.substring(6).trim();
        int fromIndex = arguments.indexOf("/from");
        if (arguments.isEmpty() || fromIndex == 0) {
            throw new PavanmaxxerException("The description of an event cannot be empty.");
        }
        if (fromIndex < 0) {
            throw new PavanmaxxerException("An event needs a /from time.");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String fromAndTo = arguments.substring(fromIndex + 5).trim();
        int toIndex = fromAndTo.indexOf("/to");
        if (description.isEmpty()) {
            throw new PavanmaxxerException("The description of an event cannot be empty.");
        }
        if (toIndex < 0) {
            throw new PavanmaxxerException("An event needs a /to time.");
        }
        String from = fromAndTo.substring(0, toIndex).trim();
        String to = fromAndTo.substring(toIndex + 3).trim();
        if (from.isEmpty()) {
            throw new PavanmaxxerException("An event needs a /from time.");
        }
        if (to.isEmpty()) {
            throw new PavanmaxxerException("An event needs a /to time.");
        }
        return new Event(description, from, to);
    }

    /**
     * Extracts and validates a one-based task number, returning its list index.
     */
    private static int parseTaskIndex(String input, String command, int taskListSize)
            throws PavanmaxxerException {
        String taskNumberText = input.substring(command.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new PavanmaxxerException("Please provide a task number to " + command + ".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new PavanmaxxerException("The task number must be an integer.");
        }

        if (taskNumber < 1 || taskNumber > taskListSize) {
            throw new PavanmaxxerException("That task number does not exist.");
        }
        return taskNumber - 1;
    }
}
