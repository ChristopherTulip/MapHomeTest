package com.tulip.maphometest.Utils;

import android.util.Log;

public class Logger 
{
	private static boolean debug = Constant.DEBUG;

	public static void e (String tag, String msg)
	{
		if (debug) Log.e(tag, msg);
	}
	
	public static void i (String tag, String msg)
	{
		if (debug) Log.i(tag, msg);
	}
	
	public static void d (String tag, String msg)
	{
		if (debug) Log.d(tag, msg);
	}
	
	public static void v (String tag, String msg)
	{
		if (debug) Log.v(tag, msg);
	}
}
