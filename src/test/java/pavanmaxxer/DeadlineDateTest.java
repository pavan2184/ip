package pavanmaxxer;

import java.time.LocalDate;

/**
 * Checks that deadline dates are parsed, validated, and displayed consistently.
 */
public class DeadlineDateTest {
    public static void main(String[] args) throws Exception {
        Deadline deadline = new Deadline(
                "return book", LocalDate.parse("2026-08-31"));
        assert deadline.toString()
                .equals("[D][ ] return book (by: Aug 31 2026)");

        Task parsed = Parser.parseTask(
                "deadline return book /by 2026-08-31", Command.DEADLINE);
        assert parsed.toString()
                .equals("[D][ ] return book (by: Aug 31 2026)");

        boolean didThrow = false;
        try {
            Parser.parseTask(
                    "deadline return book /by 2026-02-30", Command.DEADLINE);
        } catch (PavanmaxxerException exception) {
            didThrow = true;
        }
        assert didThrow;

        System.out.println("PASS: deadline dates are parsed and displayed consistently");
    }
}
