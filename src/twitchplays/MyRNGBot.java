package twitchplays;

import java.util.Random;

@SuppressWarnings("unused")
public class MyRNGBot 
{
	private TwitchPlays output = null;
	/*
    MyRNGBot bot = new MyRNGBot();
    bot.setOutput(this);
    bot.start();
    */
	
	public void start()
	{
		//Random rng = new Random();
		while (true)
		{
			String command = "";
			String outputCommand = "";
			//int randomNumber = rng.nextInt(14);
			/*switch (randomNumber)
			{
			case 0:
				command = MyAutoItController.START;
	    		outputCommand = "start";
				command = MyAutoItController.LEFT;
	    		outputCommand = "left";
				break;
			case 1:
				command = MyAutoItController.SELECT;
	    		outputCommand = "select";
				command = MyAutoItController.UP;
	    		outputCommand = "up";
				break;
			case 2:
				command = MyAutoItController.UP;
	    		outputCommand = "up";
				break;
			case 3:
				command = MyAutoItController.LEFT;
	    		outputCommand = "left";
				break;
			case 4:
				command = MyAutoItController.DOWN;
	    		outputCommand = "down";
				break;
			case 5:
				command = MyAutoItController.RIGHT;
	    		outputCommand = "right";
				break;
			case 6:
				command = MyAutoItController.A;
	    		outputCommand = "a";
				break;
			case 7:
				command = MyAutoItController.B;
	    		outputCommand = "b";
				break;
			case 8:
				command = MyAutoItController.L;
	    		outputCommand = "L trigger";
				command = MyAutoItController.DOWN;
	    		outputCommand = "down";
				break;
			case 9:
				command = MyAutoItController.R;
	    		outputCommand = "R trigger";
				break;
			case 10:
				command = MyAutoItController.CUP;
	    		outputCommand = "C up";
				command = MyAutoItController.RIGHT;
	    		outputCommand = "right";
				break;
			case 11:
				command = MyAutoItController.CLEFT;
	    		outputCommand = "C left";
				break;
			case 12:
				command = MyAutoItController.CDOWN;
	    		outputCommand = "C down";
				break;
			case 13:
				command = MyAutoItController.CRIGHT;
	    		outputCommand = "C right";
				break;
			}*/
			
			if (!command.isEmpty())
			{
				MyAutoItController.sendKey(command);
				if (this.output != null) this.output.Log("RNG pressed " + outputCommand);
			}
		}
	}
}
