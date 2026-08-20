import java.util.Scanner;

/**
 * Runs the Pavanmaxxer text-based task manager.
 */
public class Pavanmaxxer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("Hello! I'm Pavanmaxxer.");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            try {
                if (input.equals("list")) {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(input, "mark", taskCount);
                    tasks[taskIndex].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskIndex]);
                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(input, "unmark", taskCount);
                    tasks[taskIndex].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskIndex]);
                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    int taskIndex = parseTaskIndex(input, "delete", taskCount);
                    Task removedTask = tasks[taskIndex];
                    for (int i = taskIndex; i < taskCount - 1; i++) {
                        tasks[i] = tasks[i + 1];
                    }
                    taskCount--;
                    tasks[taskCount] = null;
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else if (isTaskCommand(input)) {
                    Task task = parseTask(input);
                    if (taskCount >= tasks.length) {
                        throw new PavanmaxxerException("The task list is full.");
                    }
                    tasks[taskCount] = task;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks[taskCount]);
                    taskCount++;
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else {
                    throw new PavanmaxxerException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (PavanmaxxerException exception) {
                System.out.println("OOPS!!! " + exception.getMessage());
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Returns whether the input begins a supported task-creation command.
     */
    private static boolean isTaskCommand(String input) {
        return input.equals("todo") || input.startsWith("todo ")
                || input.equals("deadline") || input.startsWith("deadline ")
                || input.equals("event") || input.startsWith("event ");
    }

    /**
     * Parses a validly named task command and validates all required fields.
     */
    private static Task parseTask(String input) throws PavanmaxxerException {
        if (input.equals("todo") || input.startsWith("todo ")) {
            String description = input.length() == 4 ? "" : input.substring(5).trim();
            if (description.isEmpty()) {
                throw new PavanmaxxerException("The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }

        if (input.equals("deadline") || input.startsWith("deadline ")) {
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
     * Extracts and validates a one-based task number, returning its array index.
     */
    private static int parseTaskIndex(String input, String command, int taskCount)
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

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new PavanmaxxerException("That task number does not exist.");
        }
        return taskNumber - 1;
    }
}
