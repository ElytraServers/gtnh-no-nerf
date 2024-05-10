package cn.taskeren.gtnn.client.rawinput;

public class RawInputException extends RuntimeException {
	public RawInputException(String message) {
		super(message);
	}

	public RawInputException(String message, Throwable cause) {
		super(message, cause);
	}
}
