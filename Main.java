//program 7 Chat - CMSC 3320/001 - Group 7
//
//Caleb Ruby - rub4133@pennwest.edu
//Adir Turgeman - tur28711@pennwest.edu
//Caleb Rachocki - rac3146@pennwest.edu
//Ryan Miller - mil0780@pennwest.edu



//imports:
//import java.awt.*;
//import java.awt.event.*;
//import java.net.Socket;
//import java.net.ServerSocket;
//import java.net.InetSocketAddress;
//import java.net.SocketTimeoutException;
//import java.io.*;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.io.BufferedReader;

public class Main {
	
	
public static void main(String[] args) 
{
    Chat chat = new Chat(0);	//instantiate an object of type Chat
    
    if(args.length > 0)		//if parameters were passed 
    {
	    try 
	    {
			int to = Integer.parseInt(args[0]);
			chat.setTimeOut(to);
		} 
	    catch (NumberFormatException e) 
	    {
	    	System.out.println("Default TimeOut in use...");
		}
    }
    else
    {
    	System.out.println("Default TimeOut in use...");
    }
    
    	
    
}}
    
