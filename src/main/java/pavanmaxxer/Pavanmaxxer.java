package pavanmaxxer;

import java.nio.file.Path;

/**
 * Coordinates the Pavanmaxxer task manager's collaborators.
 */
public class Pavanmaxxer {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Pavanmaxxer using the supplied data-file location.
     *
     * @param filePath relative or absolute task data path
     */
    public Pavanmaxxer(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (PavanmaxxerException exception) {
            ui.showError(exception.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Reads and executes commands until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        boolean isRunning = true;
        while (isRunning && ui.hasNextCommand()) {
            String input = ui.readCommand();
            Command command = Parser.parseCommand(input);
            try {
                switch (command) {
                case BYE:
                    isRunning = false;
                    break;
                case LIST:
                    ui.showTasks(tasks.asList());
                    break;
                case MARK:
                    int markIndex = Parser.parseTaskIndex(input, "mark", tasks.size());
                    Task markedTask = tasks.mark(markIndex);
                    storage.save(tasks);
                    ui.showMarkedTask(markedTask, true);
                    break;
                case UNMARK:
                    int unmarkIndex = Parser.parseTaskIndex(input, "unmark", tasks.size());
                    Task unmarkedTask = tasks.unmark(unmarkIndex);
                    storage.save(tasks);
                    ui.showMarkedTask(unmarkedTask, false);
                    break;
                case DELETE:
                    int deleteIndex = Parser.parseTaskIndex(input, "delete", tasks.size());
                    Task deletedTask = tasks.delete(deleteIndex);
                    storage.save(tasks);
                    ui.showDeletedTask(deletedTask, tasks.size());
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = Parser.parseTask(input, command);
                    tasks.add(task);
                    storage.save(tasks);
                    ui.showAddedTask(task, tasks.size());
                    break;
                case FIND:
                    String keyword = Parser.parseFindKeyword(input);
                    ui.showMatchingTasks(tasks.find(keyword));
                    break;
                case UNKNOWN:
                    throw new PavanmaxxerException(
                            "I'm sorry, but I don't know what that means :-(");
                }
            } catch (PavanmaxxerException exception) {
                ui.showError(exception.getMessage());
            }
        }
        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new Pavanmaxxer(Path.of("data", "pavanmaxxer.txt")).run();
    }
}
