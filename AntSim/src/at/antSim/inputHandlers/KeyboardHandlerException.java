package at.antSim.inputHandlers;

/**
 * Created on 05.05.2015.
 *
 * @author Clemens
 */
public class KeyboardHandlerException extends Throwable {
	public KeyboardHandlerException() {
	}

	public KeyboardHandlerException(String message) {
		super(message);
	}

	public KeyboardHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyboardHandlerException(Throwable cause) {
		super(cause);
	}

	public KeyboardHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
