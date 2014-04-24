package twitchplays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import cn.com.jautoitx.Win;

public class TwitchPlays extends JFrame
{
	private static final long serialVersionUID = 6960035658635289037L;
	public static final String CONFIG_FILENAME = "configurations.txt";
	private static final int WIDTH = 755;
	private static final int HEIGHT = 440;
	private JTextArea textarea;
	private JLabel time;
	private int days = 0;
	private int hours = 0;
	private int minutes = 0;
	private int seconds = 0;
	public boolean paused = false;
	private TwitchPlays self;
	private JTextField botUsername;
	private JTextField botPassword;
	private JTextField channel;
	private JTextField host;
	private JTextField port;
	private JButton startButton;
	private JTextField emulatorWindowName;
	private JTextField chatInput;
	private JComboBox<String> emulatedInput;
	private JButton addButton;
	private JButton removeButton;
	private JTable table;
	private JButton commandSaveButton;
	private JButton saveButton;

	public TwitchPlays()
	{
		self = this;
    	setTitle("TwitchPlays Client by Pheelbert");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setResizable(false);
        setLayout(null);
        setIconImage(new ImageIcon("picons.png").getImage());
        
        add(createTimePanel(0, 20, 400, 50));
        add(createChatPanel(0, 70, 200, 240));
        add(createCredentialsPanel(200, 70, 200, 240));
        add(createExecutePanel(0, 310, 400, 80));
        add(createConfigurationsPanel(400, 20, 350, 370));
        
        load();
        
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	private JPanel createTimePanel(int x, int y, int width, int height)
	{
		JPanel panel = new JPanel();
		panel.setBounds(x, y, width, height);
		panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        time = new JLabel();
        time.setText("0d 0h 0m 0s");
        panel.setBackground(Color.BLACK);
        time.setForeground(Color.WHITE);
        time.setFont(new Font("Courier", Font.BOLD, 16));
        panel.add(time);
        return panel;
	}
	
	private JPanel createChatPanel(int x, int y, int width, int height)
	{
	    JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.setBounds(x, y, width, height);
        panel.setBackground(Color.BLACK);
        textarea = new JTextArea(10, 20);
        textarea.setMargin(new Insets(10, 10, 0, 0));
        textarea.setEditable(false);
        textarea.setBackground(Color.BLACK);
        textarea.setForeground(Color.WHITE);
        textarea.setFont(new Font("Courier", Font.BOLD, 14));
        JScrollPane scroll = new JScrollPane(textarea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        DefaultCaret caret = (DefaultCaret)textarea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.add(scroll);
        return panel;
	}
	
	private JPanel createCredentialsPanel(int x, int y, int width, int height)
	{
	    JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.setBounds(x, y, width, height);
        panel.setBackground(Color.BLACK);
        
        JLabel lblBotUsername = new JLabel("Bot username: ");
        lblBotUsername.setToolTipText("Bot is just an irc(ex: twitch) username. You can make a new account for the bot if you wish.");
        lblBotUsername.setIcon(new ImageIcon("tooltipicon.gif"));
        lblBotUsername.setHorizontalTextPosition(SwingConstants.LEFT);
        botUsername = new JTextField("username", 12);
        
        JLabel lblBotPassword = new JLabel("Bot password: ");
        lblBotPassword.setToolTipText("Password to the irc(ex: twitch) account. For twitch you need to encrypt your password and write that in this field: www.twitchapps.com/tmi.");
        lblBotPassword.setIcon(new ImageIcon("tooltipicon.gif"));
        lblBotPassword.setHorizontalTextPosition(SwingConstants.LEFT);
        botPassword = new JTextField("oauth:encrypted_pass", 12);
        
        JLabel lblChannel = new JLabel("Channel:             ");
        lblChannel.setToolTipText("Name of the channel you wish you get input from (ex: #channelname). '#' before the name is important.");
        lblChannel.setIcon(new ImageIcon("tooltipicon.gif"));
        lblChannel.setHorizontalTextPosition(SwingConstants.LEFT);
        channel = new JTextField("#channel_name", 12);
        
        JLabel lblHost = new JLabel("Host:                    ");
        lblHost.setToolTipText("IRC host (ex for twitch: irc.twitch.tv).");
        lblHost.setIcon(new ImageIcon("tooltipicon.gif"));
        lblHost.setHorizontalTextPosition(SwingConstants.LEFT);
        host = new JTextField("irc.twitch.tv", 12);
        
        JLabel lblPort = new JLabel("Port:                     ");
        lblPort.setToolTipText("Port used to send and recieve data (ex for twitch: 6667).");
        lblPort.setIcon(new ImageIcon("tooltipicon.gif"));
        lblPort.setHorizontalTextPosition(SwingConstants.LEFT);
        port = new JTextField("6667", 12);
        
        panel.add(lblBotUsername);
        panel.add(botUsername);
        panel.add(lblBotPassword);
        panel.add(botPassword);
        panel.add(lblChannel);
        panel.add(channel);
        panel.add(lblHost);
        panel.add(host);
        panel.add(lblPort);
        panel.add(port);
        
        return panel;
	}
	
	private JPanel createExecutePanel(int x, int y, int width, int height)
	{
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.setBounds(x, y, width, height);
        panel.setBackground(Color.BLACK);
        startButton = new JButton("Start");
        JLabel lblEmulatorWindowName = new JLabel("Emulator window name:");
        emulatorWindowName = new JTextField("VisualBoyAdvance", 12);
        panel.add(lblEmulatorWindowName);
        panel.add(emulatorWindowName);
        panel.add(startButton);
    
        startButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
            	if (!emulatorWindowName.getText().trim().equals("") &&
            		!botUsername.getText().trim().equals("")        &&
            	    !botPassword.getText().trim().equals("")        &&
            	    !channel.getText().trim().equals("")            &&
            	    !host.getText().trim().equals("")               &&
            	    !port.getText().trim().equals(""))
                {
            		try
            		{
                		MyIRCBot bot = new MyIRCBot(botUsername.getText(), host.getText(), Integer.parseInt(port.getText()), botPassword.getText(), channel.getText());
                		bot.setOutput(self);
                		if (!bot.connected) throw new Exception("Could not connect to the channel.");
                		botUsername.setEnabled(false);
                		botPassword.setEnabled(false);
                		channel.setEnabled(false);
                		host.setEnabled(false);
                		port.setEnabled(false);
                		emulatorWindowName.setEnabled(false);
                		startButton.setText("Running");
                		startButton.setEnabled(false);
            		}
            		catch (Exception e)
            		{
            			JOptionPane.showMessageDialog(null, "Error message: " + e.getMessage());
            		}
            		finally
            		{
            			Timer timer = new Timer();
            			timer.scheduleAtFixedRate(new TimerTask()
            	        {
            	            public void run()
            	            {
            	            	if (!paused)
            	            	{
            	            		self.IncrementTime(1);
            	            	}
            	            }
            	        }, 1000, 1000);
            			
            			Timer timer2 = new Timer();
            			timer2.scheduleAtFixedRate(new TimerTask()
            	        {
            	            public void run()
            	            {
            	                if (Win.active(emulatorWindowName.getText()))
            	                {
            	                	paused = false;
            	                	startButton.setText("Running");
            	                	startButton.setBackground(Color.GREEN);
            	                }
            	                else
            	                {
            	                	paused = true;
            	                	startButton.setText("Paused");
            	                	startButton.setBackground(Color.YELLOW);
            	                }
            	            }
            	        }, 1000, 1000);
            		}
                }
            	else
            	{
            		JOptionPane.showMessageDialog(null, "Error message: Please fill in the information.");
            	}
            }
        });
        
        saveButton = new JButton("Save configurations");
        saveButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
            	try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CONFIG_FILENAME), "utf-8"))) 
        		{
            		writer.write(botUsername.getText() + "\n");
            		writer.write(botPassword.getText() + "\n");
            		writer.write(channel.getText() + "\n");
            		writer.write(host.getText() + "\n");
            		writer.write(port.getText() + "\n");
            		writer.write(emulatorWindowName.getText() + "\n");
            		JOptionPane.showMessageDialog(null, "Configurations were saved!");
        		} 
        		catch (IOException e)
        		{
        			e.printStackTrace();
        		} 
            }
        });
        panel.add(saveButton);
	        
        return panel;
	}
	
	private JPanel createConfigurationsPanel(int x, int y, int width, int height)
	{
		JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.setBounds(x, y, width, height);
        panel.setBackground(Color.BLACK);
        panel.add(new JLabel("Chat input:                "));
        chatInput = new JTextField("", 12);
        panel.add(chatInput);
        panel.add(new JLabel("Emulated input: "));
        emulatedInput = new JComboBox<String>();
        
        Set<String> keyNames = MyAutoItController.keys.keySet();
        for (String key : keyNames)
    	{
        	emulatedInput.addItem(key);
    	}
        
        panel.add(emulatedInput);
        addButton = new JButton("+");
        panel.add(addButton);
        removeButton = new JButton("-");
        panel.add(removeButton);
        
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Command");
        columnNames.add("Key");
        
        Vector<Vector<String>> rowData = new Vector<Vector<String>>();
        Set<String> keys = MyAutoItController.commands.keySet();
    	for (String key : keys)
    	{
    		Vector<String> row = new Vector<String>();
    		row.add(key);
    		row.add(MyAutoItController.getKeyNameFromChatCommand(key));
    		rowData.add(row);
    	}
    	
    	table = new JTable(rowData, columnNames);
    	table.getColumnModel().getColumn(0).setMaxWidth(120);
    	table.getColumnModel().getColumn(1).setMaxWidth(120);
    	for (int c = 0; c < table.getColumnCount(); c++)
    	{
    	    Class<?> col_class = table.getColumnClass(c);
    	    table.setDefaultEditor(col_class, null);
    	}
    	
        JScrollPane scroll = new JScrollPane(table);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(220, 200));
        panel.add(scroll);
    
        addButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
            	if (!chatInput.getText().trim().equals(""))
            	{
	            	Vector<String> row = new Vector<String>();
	            	if (MyAutoItController.addCommandPair(chatInput.getText(), emulatedInput.getSelectedItem().toString()))
	            	{
		            	row.add(chatInput.getText());
		            	row.add(emulatedInput.getSelectedItem().toString());
		            	((DefaultTableModel) table.getModel()).addRow(row);
	            	}
	            	else
	            	{
	            		JOptionPane.showMessageDialog(null, "Chat command already used.");
	            	}
            	}
            	else
            	{
            		JOptionPane.showMessageDialog(null, "Error message: Please fill in the information.");
            	}
            }
        });
    
        removeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
            	if (table.getSelectedRow() > -1)
            	{
	            	if (MyAutoItController.removeCommandPair(((DefaultTableModel) table.getModel()).getValueAt(table.getSelectedRow(), 0).toString()))
	            	{
	            		((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
	            	}
            	}
            	else
            	{
            		JOptionPane.showMessageDialog(null, "You must select a command to remove.");
            	}
            }
        });
        
        commandSaveButton = new JButton("Save commands");
        commandSaveButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
            	MyAutoItController.save();
            	JOptionPane.showMessageDialog(null, "Commands were saved!");
            }
        });
        panel.add(commandSaveButton);
        
        return panel;
	}
	
	public void Log(String text)
	{
		textarea.append(text + "\n");
	}
	
	public TimerTask IncrementTime(int secondsAdded)
	{
		this.seconds += secondsAdded;
		if (this.seconds > 59)
		{
			this.seconds = 0;
			this.minutes++;
			if (this.minutes > 59)
			{
				this.minutes = 0;
				this.hours++;
				if (this.hours > 23)
				{
					this.hours = 0;
					this.days++;
				}
			}
		}
		
		time.setText(this.days + "d " + this.hours + "h " + this.minutes + "m " + this.seconds + "s");
		return null;
	}
	
	public void load()
	{
		try 
		{
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILENAME));
			String line = null;
			int index = 0;
			while ((line = reader.readLine()) != null) 
			{
				switch (index)
				{
				case 0:
					botUsername.setText(line);
					break;
				case 1:
					botPassword.setText(line);
					break;
				case 2:
					channel.setText(line);
					break;
				case 3:
					host.setText(line);
					break;
				case 4:
					port.setText(line);
					break;
				case 5:
					emulatorWindowName.setText(line);
					break;
				}
				index++;
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception 
	{
		new TwitchPlays();
	}
}
