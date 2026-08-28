package pavanmaxxer;

/**
 * Represents the finite set of commands understood by Pavanmaxxer.
 */
public enum Command {
    BYE("bye", false),
    LIST("list", false),
    MARK("mark", true),
    UNMARK("unmark", true),
    DELETE("delete", true),
    TODO("todo", true),
    DEADLINE("deadline", true),
    EVENT("event", true),
    UNKNOWN("", false);

    private final String keyword;
    private final boolean hasArguments;

    Command(String keyword, boolean hasArguments) {
        this.keyword = keyword;
        this.hasArguments = hasArguments;
    }

    /**
     * Classifies raw user input without accepting a partial command name.
     *
     * @param input raw line entered by the user
     * @return the matching command, or {@link #UNKNOWN} when none matches
     */
    public static Command from(String input) {
        for (Command command : values()) {
            if (command == UNKNOWN) {
                continue;
            }
            if (input.equals(command.keyword)
                    || command.hasArguments && input.startsWith(command.keyword + " ")) {
                return command;
            }
        }
        return UNKNOWN;
    }
}
