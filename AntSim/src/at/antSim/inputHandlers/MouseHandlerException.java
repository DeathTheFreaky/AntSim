package at.antSim.inputHandlers;

/**
 * Created on 05.05.2015.
 *
 * @author Clemens
 */
public class MouseHandlerException extends Exception {
	public MouseHandlerException() {
	}

	public MouseHandlerException(String message) {
		super(message);
	}

	public MouseHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public MouseHandlerException(Throwable cause) {
		super(cause);
	}

	public MouseHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
