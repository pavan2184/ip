/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected String typeIcon;
    protected String timingDetails;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this(description, "", "");
    }

    /**
     * Creates an incomplete task with its display type and optional timing details.
     *
     * @param description description of the task
     * @param typeIcon single-letter task type shown to the user
     * @param timingDetails formatted timing text, or an empty string
     */
    public Task(String description, String typeIcon, String timingDetails) {
        this.description = description;
        this.isDone = false;
        this.typeIcon = typeIcon;
        this.timingDetails = timingDetails;
    }

    /**
     * Returns the icon used to display the task's completion status.
     *
     * @return {@code X} when done, or a blank space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        String typePrefix = typeIcon.isEmpty() ? "" : "[" + typeIcon + "]";
        return typePrefix + "[" + getStatusIcon() + "] " + description + timingDetails;
    }
}
