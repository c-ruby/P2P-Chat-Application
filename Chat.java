//program 7 Chat - CMSC 3320/001 - Group 7
//
//Caleb Ruby - rub4133@pennwest.edu
//Adir Turgeman - tur28711@pennwest.edu
//Caleb Rachocki - rac3146@pennwest.edu
//Ryan Miller - mil0780@pennwest.edu



//imports:
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.GarbageCollectorMXBean;
import java.io.BufferedReader;
@SuppressWarnings({ "unused" })

public class Chat implements Runnable, ActionListener, WindowListener
{
//init
	//buffers
	BufferedReader br;
	PrintWriter pw;
	//constants
	protected final static boolean auto_flush = true;
	private static final int DEFAULT_PORT  = 44004;
	private static final int  DEFAULT_TIMEOUT = 2000;
	//buttons	
	Button ChangePortButton = new Button("Change Port");
	Button SendButton = new Button(" Send ");
	Button ServerButton = new Button ("start server");
	Button ClientButton = new Button (" connect ");
	Button DisconnectButton = new Button ("Disconnect");
	Button ChangeHostButton = new Button ("Change host");
	//labels	
	Label PortLabel = new Label("Port");
	Label HostLabel = new Label("Host");
	//textfields	
	TextField ChatText = new TextField(70);
	TextField PortText = new TextField(10);
	TextField HostText = new TextField(10);
	//frame	
	Frame dispFrame;
	//the thread
	Thread TheThread;
	//text areas	
	TextArea DialogScreen = new TextArea("Dialog\n",10,80);
	TextArea MessageScreen = new TextArea("Messages\n",3,80);
	//sockets	
	Socket client;
	Socket server;
	
	ServerSocket listen_socket;
	//data	
	String host ="";
	int port = DEFAULT_PORT;
	int service = 0;
	boolean more = true;
	int timeOut = DEFAULT_TIMEOUT;

//constructor
	public Chat(int socketWait)
	{
		makeFrame();	//method which instantiates the window 
		
		//initialize 
		service = 0;	//runstate
		more = true;	//more flag
		
		
		
	}
	public void makeFrame()
	{
		//instantiates the frame
				dispFrame = new Frame();				
				
			//set gridbaglayout
				GridBagLayout GBL = new GridBagLayout();
				GridBagConstraints GBC = new GridBagConstraints();
				dispFrame.setLayout(GBL);
				
		       
		        	Insets insets = new Insets(0, 0, 0, 0);
		        	GBC.insets = insets;
		    //set message textArea  
		        GBC.anchor = GridBagConstraints.NORTH;
		        GBC.fill = GridBagConstraints.BOTH; // Resize both horizontally and vertically
		        GBC.gridx = 0; // Start from the leftmost column
		        GBC.gridy = 0; // Start from the top row
		        GBC.gridwidth = GridBagConstraints.REMAINDER; // Take up the remaining columns
		        
		        GBC.gridheight = 3; // Take up one row
		        GBC.weightx = 1.0; // Fill horizontally
		        GBC.weighty = 6; // Fill vertically proportionally
		        dispFrame.add(MessageScreen, GBC); // Add the dialog screen component
		        
		    //add buttons and textfields 
		        //chat text 
		        GBC.gridy = 3; // Set the grid row
		        GBC.anchor = GridBagConstraints.CENTER; // Center the component horizontally
		        GBC.fill = GridBagConstraints.HORIZONTAL; // Allow the component to expand horizontally
		        GBC.gridwidth = 1;
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Fill horizontally
		        GBC.weighty = 1; // Don't allocate vertical space
		        dispFrame.add(ChatText, GBC);
		        ChatText.setEnabled(false);
		        ChatText.addActionListener(this);
		        //send 
		        GBC.gridy = 3; // Set the grid row
		        GBC.gridx = 1;
		        GBC.gridwidth = 1;
		        GBC.anchor = GridBagConstraints.WEST; // Center the component horizontally
		        GBC.fill = GridBagConstraints.NONE; // Allow the component to expand horizontally
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = .2; // Fill horizontally
		        GBC.weighty = 1; // Don't allocate vertical space
		        dispFrame.add(SendButton, GBC);
		        SendButton.addActionListener(this);
		      
		     // Host label
		        GBC.gridy = 4; // Set the grid row
		        GBC.gridx = 0; // Start from the leftmost column
		        GBC.anchor = GridBagConstraints.EAST; // Align to the left
		        GBC.fill = GridBagConstraints.NONE; // Do not resize
		        GBC.gridwidth = 1; // Take up one column
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Do not allocate horizontal space
		        GBC.weighty = 1; // Do not allocate vertical space
		        dispFrame.add(HostLabel, GBC);
		        // Host text field 
		        GBC.gridx = 1; // Place next to the label
		        GBC.anchor = GridBagConstraints.WEST; // Align to the left
		        GBC.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
		        GBC.gridwidth = 3; // Take up remaining columns
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Fill horizontally
		        HostText.addActionListener(this);
		        dispFrame.add(HostText, GBC);
		        //change host
		        GBC.gridy = 4; // Set the grid row
		        GBC.gridx = 4; // Start from the leftmost column
		        GBC.anchor = GridBagConstraints.EAST; // Align to the left
		        GBC.fill = GridBagConstraints.NONE; // Do not resize
		        GBC.gridwidth = 1; // Take up one column
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Do not allocate horizontal space
		        GBC.weighty = 1; // Do not allocate vertical space
		        dispFrame.add(ChangeHostButton, GBC);
		       // ChangeHostButton.setEnabled(false);
		        ChangeHostButton.addActionListener(this);
		        //start server button 
		      //change host
		        GBC.gridy = 4; // Set the grid row
		        GBC.gridx = 5; // Start from the leftmost column
		        GBC.anchor = GridBagConstraints.EAST; // Align to the left
		        GBC.fill = GridBagConstraints.NONE; // Do not resize
		        GBC.gridwidth = 1; // Take up one column
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Do not allocate horizontal space
		        GBC.weighty = 1; // Do not allocate vertical space
		        dispFrame.add(ServerButton, GBC);
		        //start
		        ServerButton.addActionListener(this);
		        
		     // port label
		        GBC.gridy = 5; // Set the grid row
		        GBC.gridx = 0; // Start from the leftmost column
		        GBC.anchor = GridBagConstraints.EAST; // Align to the left
		        GBC.fill = GridBagConstraints.NONE; // Do not resize
		        GBC.gridwidth = 1; // Take up one column
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Do not allocate horizontal space
		        GBC.weighty = 1; // Do not allocate vertical space
		        dispFrame.add(PortLabel, GBC);
		        // port text field 
		        GBC.gridx = 1; // Place next to the label
		        GBC.anchor = GridBagConstraints.WEST; // Align to the left
		        GBC.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
		        GBC.gridwidth = 3; // Take up remaining columns
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Fill horizontally
		        PortText.addActionListener(this);
		        dispFrame.add(PortText, GBC);
		        //change port
		        GBC.gridy = 5; // Set the grid row
		        GBC.gridx = 4; // Start from the leftmost column
		        GBC.anchor = GridBagConstraints.EAST; // Align to the left
		        GBC.fill = GridBagConstraints.NONE; // Do not resize
		        GBC.gridwidth = 1; // Take up one column
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Do not allocate horizontal space
		        GBC.weighty = 1; // Do not allocate vertical space
		        dispFrame.add(ChangePortButton, GBC);
		        ChangePortButton.addActionListener(this);
		        //client connnect button
		        GBC.gridy = 5; // Set the grid row
		        GBC.gridx = 5; // Start from the leftmost column
		        GBC.anchor = GridBagConstraints.EAST; // Align to the left
		        GBC.fill = GridBagConstraints.NONE; // Do not resize
		        GBC.gridwidth = 1; // Take up one column
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Do not allocate horizontal space
		        GBC.weighty = 1; // Do not allocate vertical space
		        dispFrame.add(ClientButton, GBC);
		        ClientButton.setEnabled(false);
		        ClientButton.addActionListener(this);
		        //disconnnect button
		        GBC.gridy = 6; // Set the grid row
		        GBC.gridx = 5; // Start from the leftmost column
		        GBC.anchor = GridBagConstraints.EAST; // Align to the left
		        GBC.fill = GridBagConstraints.NONE; // Do not resize
		        GBC.gridwidth = 1; // Take up one column
		        GBC.gridheight = 1; // Take up one row
		        GBC.weightx = 1; // Do not allocate horizontal space
		        GBC.weighty = 1; // Do not allocate vertical space
		        dispFrame.add(DisconnectButton, GBC);
		        DisconnectButton.addActionListener(this);
		        
		    
		    //set dialog textArea
		        GBC.anchor = GridBagConstraints.PAGE_END;
		        GBC.fill = GridBagConstraints.BOTH;
		        GBC.gridwidth = GridBagConstraints.REMAINDER; 
		        GBC.gridy = 7;
		        GBC.gridx = 0; // S
		        GBC.gridheight = 1;
		        GBC.weighty = .01; // Fill vertically proportionally
		        dispFrame.add(DialogScreen, GBC);
		    //frame wrapup
		       // dispFrame.setTitle("Chat Program");
		        dispFrame.addWindowListener(this);
		        dispFrame.setVisible(true);
		        dispFrame.setSize(800, 600);
		        dispFrame.setResizable(true);
		        MessageScreen.setEditable(false);
		        DialogScreen.setEditable(false);
	}

	
//member functions 
	//function to handle main keyboard input for timeout
	public void setTimeOut(int userDefTimeOut)
	{
		System.out.println("Parameter receieved in main: " + userDefTimeOut);
		System.out.println("Setting timeout to: " + userDefTimeOut + "milliseconds...");
		
		timeOut = userDefTimeOut;
	}
	//initializes the thread 
	public void start()
	{
		if(TheThread == null)
		{
			TheThread = new Thread(this);
			System.out.println("Made the thread");
			
		}
		TheThread.setPriority(Thread.MAX_PRIORITY);	//set thread priority 
		TheThread.start();
		
	}
	
	//close method 
	public void close()	//attempts to close the reader, writer, and all the sockets 
	{
		more = false;
		
		try //server socket  
		{
			if(server!=null)
			{
				if(pw!= null)
				{
					pw.print("");
					server.close();
					server = null;
				}
				if(br!=null)
				{
					br.close();
				}
				
			}
		} catch (IOException e) 
		{}
		try //server socket  
		{
			if(client!=null)
			{
				if(pw!= null)
				{
					pw.print("");
					client.close();
					client = null;
				}
				if(br!=null)
				{
					br.close();
				}
			}
		} catch (IOException e) 
		{}
		try //server socket  
		{
			if(listen_socket!=null)
			{
				if(pw!= null)
				{
					pw.print("");
					listen_socket.close();
					listen_socket = null;
				}
				if(br!=null)
				{
					br.close();
				}
			}
		} catch (IOException e) 
		{}
		
		//reset buttons 
		ServerButton.setEnabled(true);
		ClientButton.setEnabled(false);
		//reset host textfeild
		HostText.setText("");
		ChatText.setEnabled(false);
		
		
		service = 0;	//reset the state 
		
		
		TheThread = null;
		
		
	}
	public void dialogDisplay(String dialog)
	{
		switch(service)
		{
		case 1:
			DialogScreen.append("/Client/ ");
			break;
		case 2:
			DialogScreen.append("/Server/ ");
			break;
		}
		
		DialogScreen.append(dialog + "\n"	);
		
		ChatText.requestFocus();
	}

		
	
//thread methods 	
	//run method: heart of the program 
	public void run()
	{
		System.out.println("running");	//dialog letting you know the thread is running 
		String line = "";				//line buffer 
		String stateMessage = "";
		TheThread.setPriority(Thread.MAX_PRIORITY);
		switch(service)	//create ouput dialog for each program mode 
		{
		case 0:
			stateMessage = "Waiting";
			break;
		case 1:
			stateMessage = "Client";
			break;
		case 2:
			stateMessage = "Server";
			break;
		}
		
		DialogScreen.append("\nRun method started...\nCurrent state: " + stateMessage + "\n");
		
		
		
		while(more)
		{
			try 
			{
				line = br.readLine();
				if(br != null)
				{
					MessageScreen.append("in <- : "+line+"\n");
				}
			} 
			catch (IOException e) 
			{
				
			}	
			
			if(line == null)
			{
				more = false;
			}
		}
		close();
		DialogScreen.append("\nChat ended. Disconnecting...\n");
	}
	//stop method
	public void stop()
	{
		close();
		if(TheThread != null)
		{
			TheThread.setPriority(Thread.MIN_PRIORITY);	//set thread priority 	
			TheThread.interrupt();
		}
		//dispFrame.removeWindowListener(this);
		//meed to remove all the listeners 
		
		//dispFrame.dispose();
		
		
		System.exit(0);
		
	}
	
	
	
	
	
//window functions
	@Override
	public void windowActivated(WindowEvent arg0) {
		ChatText.requestFocus();
	    
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		ChatText.requestFocus();
	    
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
	    
	    this.stop();
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	    
		ChatText.requestFocus();
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	    
		ChatText.requestFocus();
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		ChatText.requestFocus();
	    
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
	    
		ChatText.requestFocus();
	
	}
	public void actionPerformed(ActionEvent e) 
	{
		
		if(e.getSource() == ChatText || e.getSource() == SendButton)	//when user presses enter or the send button 
		{
			String data = ChatText.getText();
			MessageScreen.append("out ->: " + data + "\n");
			try {
				pw.println(data);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				DialogScreen.append("Not currently connected!\n");
			}
			ChatText.setText("");
		}
		else if(e.getSource() == ServerButton)
		{
			DialogScreen.append("Initializing host socket...\n");
			
			try 
			{
				ServerButton.setEnabled(false);
				ClientButton.setEnabled(false);
				
				if(listen_socket != null)
				{
					listen_socket.close();
					listen_socket = null;
				}
				
				//DialogScreen.append("Initializing host socket...\n");
				listen_socket = new ServerSocket(port);
				listen_socket.setSoTimeout(timeOut*20);
				
				if(client!=null)
				{
					client.close();
					client = null;
				}
				
				try 
				{
					DialogScreen.append("/Server/ Host socket created, waiting for connection...\n");
					client = listen_socket.accept();
					
					
					dispFrame.setTitle("/Server/");
					
					DialogScreen.append("/Server/ Connection made with" + client.getInetAddress() + "\n");
					
					try 
					{
						br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						pw = new PrintWriter(client.getOutputStream(), auto_flush);
						
						service = 2;
						ChatText.setEnabled(true);
						more = true;
						start();
					} 
					catch (IOException e2) 
					{
						DialogScreen.append("/server/ Issue generating input and output buffers\n");
						close();
					}
				}
				catch (SocketTimeoutException e3) 
				{
					DialogScreen.append("/server/ Socket timed out.\n");
					close();
				} 
			}
			catch(IOException e4)
			{
				DialogScreen.append("/server/ Issue connecting to socket\n");
				close();
			}	
		}
		else if(e.getSource() == ClientButton)
		{
			DialogScreen.append("\n/Client/ Attempting connection to server...\n");
			
			try 
			{
				ServerButton.setEnabled(false);
				ClientButton.setEnabled(false);
				
				if(server!=null)
				{
					server.close();
					server = null;
				}
				
				server = new Socket();
				DialogScreen.append("/Client/ Socket Created...\n");
				server.setSoTimeout(timeOut);
				
				try 
				{
					DialogScreen.append("/Client/ Connecting...\n");
					server.connect(new InetSocketAddress(host, port));
					
					dispFrame.setTitle("Client");
					DialogScreen.append("/Client/ Connected to " + server.getInetAddress() + "\n");
					
					try 
					{
						br = new BufferedReader(new InputStreamReader(server.getInputStream()));
						pw = new PrintWriter(server.getOutputStream(), auto_flush);
						
						service = 1;
						
						ChatText.setEnabled(true);
						more = true;
						start();
						server.setSoTimeout(0);
						
					} 
					catch (IOException e2) 
					{
						DialogScreen.append("/Client/ Failed to generate in/out buffers\n");
					}
				} 
				catch (SocketTimeoutException e5) 
				{
					DialogScreen.append("/Client/ Socket timed out\n");
				}
				
			} 
			catch (IOException e2) 
			{
				DialogScreen.append("/Client/ Failed to connect to socket\n");
			}
			
		}
		else if(e.getSource() == DisconnectButton)
		{
			if(TheThread!=null)
			{
				DialogScreen.append("\nDisconnecting...\n");
			
			//send null (?)
			
			
			TheThread.interrupt();
			
			close();
			}
			else 
			{
				DialogScreen.append("Disconnect invalid: Not currently connected.");
			}
		}
		else if(e.getSource() == ChangeHostButton || e.getSource() == HostText)
		{
			if(HostText.getText() != null)
			{
				ClientButton.setEnabled(true);
				host = HostText.getText();
			}
			
		}
		else if(e.getSource() == PortText || e.getSource() == ChangePortButton)
		{
			String portString = PortText.getText();
			try {
				int portnum = Integer.parseInt(portString);
				port = portnum;
			} catch (NumberFormatException e1) {
				DialogScreen.append("Error setting port, using default port\n");
				port = DEFAULT_PORT;
			}
			
			if(HostText.getText() != null)
			{
				ClientButton.setEnabled(true);
				host = HostText.getText();
			}
		}
		
		ChatText.requestFocus();
	}

	
	
	
	
}
