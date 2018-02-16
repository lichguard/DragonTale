package main;

import java.util.logging.Level;

public class LOGGER {

	public static void log(Level level,String msg, Object object)
	{
		System.out.println(object.getClass().getName() + ": " + msg);
	}
}
