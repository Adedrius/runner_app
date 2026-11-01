package runner_app;

public class InputCancelledException extends RuntimeException {

    public InputCancelledException() {
        super("Input cancelled by user.");
    }
}
