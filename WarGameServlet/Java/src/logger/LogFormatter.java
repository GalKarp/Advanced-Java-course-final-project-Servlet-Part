package logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    static {
    }

    @Override
    public String format(LogRecord rec) {
	Date date = new Date();
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	StringBuffer buffer = new StringBuffer(1000);
	buffer.append(format.format(date));
	buffer.append(" ");
	buffer.append(rec.getMessage());
	buffer.append("\n\n");

	return buffer.toString();
    }

}
