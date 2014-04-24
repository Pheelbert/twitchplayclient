package twitchplays;
import java.util.Set;

import org.jibble.pircbot.*;

public class MyIRCBot extends PircBot 
{
    private TwitchPlays output = null;
    /*
	MyIRCBot bot = new MyIRCBot("ToMyTerribleChannel", "irc.twitch.tv", 6667, "oauth:o2jwv6tvi51fh696n5hb5nzzhmo2efg", "#philbertsroom");
	bot.setOutput(this);
	*/
    
    public boolean connected = false;
	
    public MyIRCBot(String username, String host, int port, String password, String channel) throws Exception
    {
        this.setName(username);
        connect(host, port, password);
        joinChannel(channel);
    }
    
    public void setOutput(TwitchPlays output)
    {
    	this.output = output;
    }
    
    @Override
    public void onConnect()
    {
    	connected = true;
    }
   
    @Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) 
    {
    	String loweredMessage = message.toLowerCase();
    	String command = "";
    	String outputCommand = "";
    	
    	Set<String> keys = MyAutoItController.commands.keySet();
    	for (String key : keys)
    	{
    		if (loweredMessage.contains(key))
    		{
    			command = MyAutoItController.getKeySendCommandFromChatCommand(key);
    			outputCommand = key.replace('!', '\0');
    		}
    	}
    	
    	if (!command.isEmpty())
    	{
    		if (output.paused)
        	{
        		if (this.output != null) this.output.Log("Bot is paused");
        		return;
        	}
    		MyAutoItController.sendKey(command);
    		if (this.output != null) this.output.Log(sender + " " + outputCommand);
    	}
	}
}

/*
if (message.equalsIgnoreCase("time")) 
{
	String time = new java.util.Date().toString();
	sendMessage(channel, sender + ": The time is now " + time);
}
*/
