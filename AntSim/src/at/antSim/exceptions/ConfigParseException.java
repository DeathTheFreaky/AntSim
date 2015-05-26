package at.antSim.exceptions;

/**ConfigParsingException is thrown when parsing of the Config file fails.
 * 
 * @author Flo
 *
 */
public class ConfigParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5542731797107720162L;

	/**Creates a new {@link ConfigParseException}.
	 * 
	 * @param message - the exception message
	 */
	public ConfigParseException(String message) {
		super(message);
	}
}
