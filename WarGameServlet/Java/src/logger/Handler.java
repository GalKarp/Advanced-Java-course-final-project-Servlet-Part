package logger;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.logging.FileHandler;

public class Handler extends FileHandler {

	public Handler(String name) throws IOException, SecurityException,
			Exception {
		super("loggerFiles/" + name + ".txt", false);
		setFormatter(new LogFormatter());

	}

	public Handler(String name, Object obj) throws IOException,
			SecurityException, Exception {
		super("loggerFiles/" + name + ".txt", false);
		setFilter(new ObjectFilter(obj));
		setFormatter(new LogFormatter());

	}

	/**
	 * Enemy Launcher & Iron Dome File Handler Constructor
	 * 
	 * @param name
	 * @param id
	 * @param obj
	 * @throws IOException
	 * @throws SecurityException
	 */
	public Handler(String name, String id, Object obj) throws IOException,
			SecurityException, Exception {
		super("loggerFiles/" + name + "_" + id + ".txt", false);
		setFilter(new ObjectFilter(obj));
		setFormatter(new LogFormatter());

	}

	/**
	 * Launcher Destroyer File Handler Constructor
	 * 
	 * @param name
	 * @param id
	 * @param obj
	 * @throws IOException
	 * @throws SecurityException
	 */
	public Handler(String name, int id, Object obj) throws IOException,
			SecurityException, AccessDeniedException {
		super("loggerFiles/" + name + "_" + id + ".txt", false);
		setFilter(new ObjectFilter(obj));
		setFormatter(new LogFormatter());

	}

}
