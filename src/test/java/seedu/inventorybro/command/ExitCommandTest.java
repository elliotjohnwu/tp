package seedu.inventorybro.command;

import org.junit.jupiter.api.Test;

import seedu.inventorybro.ExitException;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ExitCommandTest {
    private final Ui ui = new Ui();

    /**
     * Verifies that executing the exit command throws {@link ExitException}
     * instead of terminating the JVM, keeping the test runner alive.
     */
    @Test
    void execute_throwsExitException() {
        assertThrows(ExitException.class, () -> new ExitCommand().execute(new ItemList(), ui));
    }
}
