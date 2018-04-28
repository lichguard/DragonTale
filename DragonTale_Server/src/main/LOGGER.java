package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;


public class LOGGER {

	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
	private static Date date = new Date();
	public static void log(Level level,String msg, Object object)
	{
		date.setTime(System.currentTimeMillis());
		System.out.println(dateFormat.format(date.getTime()) + " [" + level.getName()  +"] " +object.getClass().getName() + ": " + msg);

	}
}