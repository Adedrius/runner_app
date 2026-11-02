package runner_app;

/**
 * Custom exception class to handle cancelled inputs from user.
 */
public class InputCancelledException extends RuntimeException {

    public InputCancelledException() {
        super("Input cancelled by user.");
    }
}
