package seedu.inventorybro;

/**
 * Thrown by {@link seedu.inventorybro.command.ExitCommand} to signal that the application
 * should terminate. Caught by the main loop in {@link InventoryBro#run()}, which then calls
 * {@code System.exit(0)}. Using an exception instead of calling {@code System.exit} directly
 * inside the command keeps the JVM alive during unit tests.
 */
public class ExitException extends RuntimeException {
    public ExitException() {
        super("exit");
    }
}
