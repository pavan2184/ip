/**
 * Represents a task occurring between a start and end time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description description of the task
     * @param from start-time text supplied by the user
     * @param to end-time text supplied by the user
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
