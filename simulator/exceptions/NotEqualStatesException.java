package simulator.exceptions;

import org.json.JSONObject;

public class NotEqualStatesException extends Exception{

	private static final long serialVersionUID = 1L;

	public NotEqualStatesException() { super(); }

	public NotEqualStatesException(String message) { super(message); }

	public NotEqualStatesException(String message, Throwable cause) { super(message, cause); }

	public NotEqualStatesException(Throwable cause) { super(cause); }

	public NotEqualStatesException(JSONObject state1, JSONObject state2, int step) {
		super("Detected difference between states:\n" + state1 +
				"\nand\n" + state2 + "\nat execution step " + step);
	}
}