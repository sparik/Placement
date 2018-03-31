package am.aua.placement.objective;

/**
 * Created by sparik on 3/31/18.
 */
public class ObjectiveCalculationException extends RuntimeException {
    public ObjectiveCalculationException(String message) {
        super(message);
    }

    public ObjectiveCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
