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
	public static void info(String msg, Object object)
	{
		log(Level.INFO,msg,object);
	}
	
	public static void debug(String msg, Object object)
	{
		log(Level.FINE,msg,object);
	}
	
	public static void warn(String msg, Object object)
	{
		log(Level.WARNING,msg,object);
	}
	
	public static void error(String msg, Object object)
	{
		log(Level.SEVERE,msg,object);
	}
}