package twitchplays;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import cn.com.jautoitx.Keyboard;
import cn.com.jautoitx.Win;

public class MyAutoItController 
{
	public final static String COMMANDS_FILENAME = "commands.txt";
	public final static String PRESS   = " down}";
	public final static String RELEASE = " up}";
	
	static char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	public static Map<String, String> keys;
	public static Map<String, String> commands;
	
	static
	{
		keys = new HashMap<String, String>();
		keys.put("UP", "{UP");
		keys.put("DOWN", "{DOWN");
		keys.put("LEFT", "{LEFT");
		keys.put("RIGHT", "{RIGHT");
		for (char c : alphabet)
		{
			keys.put(String.valueOf(c), "{" + String.valueOf(c));
		}
		keys.put("RSHIFT", "{RSHIFT");
		keys.put("LSHIFT", "{LSHIFT");
		keys.put("ENTER", "{ENTER");
		keys.put("SPACE", "{SPACE");

		commands = new HashMap<String, String>();
		File config = new File(COMMANDS_FILENAME);
		if(config.exists() && !config.isDirectory()) 
		{
			load();
		}
		else
		{
			commands.put("!start", "L");
			commands.put("!select", "RSHIFT");
			commands.put("!up", "W");
			commands.put("!left", "A");
			commands.put("!down", "S");
			commands.put("!right", "D");
			commands.put("!a", "X");
			commands.put("!b", "Z");
			commands.put("!l", "Q");
			commands.put("!r", "E");
		}
	}
	
	public static String getKeySendCommandFromKeyName(String keyName)
	{
		return keys.get(keyName);
	}
	
	public static String getControlNameFromChatCommand(String chatCommand)
	{
		return commands.get(chatCommand);
	}
	
	public static String getKeyNameFromChatCommand(String chatCommand)
	{
		return commands.get(chatCommand);
	}
	
	public static String getKeySendCommandFromChatCommand(String chatCommand)
	{
		return keys.get(commands.get(chatCommand));
	}
	
	public static boolean addCommandPair(String command, String keyName)
	{
		if (keys.containsKey(keyName) && !commands.containsKey(command))
		{
			commands.put(command, keyName);
			return true;
		}
		return false;
	}
	
	public static boolean removeCommandPair(String command)
	{
		if (commands.containsKey(command))
		{
			commands.remove(command);
			return true;
		}
		return false;
	}
	
	public static void save()
	{
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(COMMANDS_FILENAME), "utf-8"))) 
		{
	        Set<String> keys = MyAutoItController.commands.keySet();
	    	for (String key : keys)
	    	{
	    		writer.write(key + "=" + MyAutoItController.getKeyNameFromChatCommand(key) + "\n");
	    	}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
	}
	
	public static void load()
	{
		try 
		{
			commands.clear();
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(COMMANDS_FILENAME));
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
				String[] parts = line.split("=");
				addCommandPair(parts[0], parts[1]);
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void activate(String windowTitle)
	{
		Win.activate(windowTitle);
	}
	
	public static void sendKey(String keyCode)
	{
		Keyboard.send(keyCode + PRESS);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		Keyboard.send(keyCode + RELEASE);
	}
}
