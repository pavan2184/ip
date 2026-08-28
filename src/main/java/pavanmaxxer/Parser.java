package pavanmaxxer;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Converts raw user input into commands and validated command arguments.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Identifies the command represented by the input.
     *
     * @param input raw user input
     * @return matching command, or {@link Command#UNKNOWN}
     */
    public static Command parseCommand(String input) {
        return Command.from(input);
    }

    /**
     * Creates a task from a task-creation command.
     *
     * @param input raw user input
     * @param command recognized task command
     * @return parsed task
     * @throws PavanmaxxerException if required arguments are invalid or missing
     */
    public static Task parseTask(String input, Command command)
            throws PavanmaxxerException {
        if (command == Command.TODO) {
            String description = input.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new PavanmaxxerException(
                        "The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }

        if (command == Command.DEADLINE) {
            String arguments = input.substring("deadline".length()).trim();
            int byIndex = arguments.indexOf("/by");
            if (arguments.isEmpty() || byIndex == 0) {
                throw new PavanmaxxerException(
                        "The description of a deadline cannot be empty.");
            }
            if (byIndex < 0) {
                throw new PavanmaxxerException("A deadline needs a /by date.");
            }
            String description = arguments.substring(0, byIndex).trim();
            String byText = arguments.substring(byIndex + 3).trim();
            if (description.isEmpty()) {
                throw new PavanmaxxerException(
                        "The description of a deadline cannot be empty.");
            }
            if (byText.isEmpty()) {
                throw new PavanmaxxerException("A deadline needs a /by date.");
            }
            try {
                return new Deadline(description, LocalDate.parse(byText));
            } catch (DateTimeParseException exception) {
                throw new PavanmaxxerException(
                        "Use yyyy-MM-dd for deadline dates.");
            }
        }

        if (command == Command.EVENT) {
            String arguments = input.substring("event".length()).trim();
            int fromIndex = arguments.indexOf("/from");
            if (arguments.isEmpty() || fromIndex == 0) {
                throw new PavanmaxxerException(
                        "The description of an event cannot be empty.");
            }
            if (fromIndex < 0) {
                throw new PavanmaxxerException("An event needs a /from time.");
            }
            String description = arguments.substring(0, fromIndex).trim();
            String fromAndTo = arguments.substring(fromIndex + 5).trim();
            int toIndex = fromAndTo.indexOf("/to");
            if (description.isEmpty()) {
                throw new PavanmaxxerException(
                        "The description of an event cannot be empty.");
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

        throw new PavanmaxxerException("That command does not create a task.");
    }

    /**
     * Converts a one-based task number into a valid list index.
     *
     * @param input raw user input
     * @param commandWord command whose argument is being parsed
     * @param taskListSize current number of tasks
     * @return zero-based task index
     * @throws PavanmaxxerException if the index is missing or invalid
     */
    public static int parseTaskIndex(String input, String commandWord,
            int taskListSize) throws PavanmaxxerException {
        String taskNumberText = input.substring(commandWord.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new PavanmaxxerException(
                    "Please provide a task number to " + commandWord + ".");
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

    /**
     * Extracts the required keyword from a find command.
     *
     * @param input raw user input
     * @return non-empty search keyword
     * @throws PavanmaxxerException if the keyword is missing
     */
    public static String parseFindKeyword(String input)
            throws PavanmaxxerException {
        String keyword = input.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new PavanmaxxerException("Please provide a keyword to find.");
        }
        return keyword;
    }
}
