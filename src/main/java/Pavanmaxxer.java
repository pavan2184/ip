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

            if (input.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                tasks[taskCount] = new Todo(description);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount]);
                taskCount++;
                System.out.println("Now you have " + taskCount + " tasks in the list.");
            } else if (input.startsWith("deadline ")) {
                String[] deadlineParts = input.substring(9).split(" /by ", 2);
                String description = deadlineParts[0];
                String by = deadlineParts[1];
                tasks[taskCount] = new Deadline(description, by);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount]);
                taskCount++;
                System.out.println("Now you have " + taskCount + " tasks in the list.");
            } else if (input.startsWith("event ")) {
                String[] eventParts = input.substring(6).split(" /from ", 2);
                String description = eventParts[0];
                String[] timeParts = eventParts[1].split(" /to ", 2);
                String from = timeParts[0];
                String to = timeParts[1];
                tasks[taskCount] = new Event(description, from, to);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount]);
                taskCount++;
                System.out.println("Now you have " + taskCount + " tasks in the list.");
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("added: " + input);
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}
