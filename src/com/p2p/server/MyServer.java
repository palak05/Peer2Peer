package com.p2p.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket MyService = null;
	    try {
	    	MyService = new ServerSocket(7732);
	        }
	        catch (IOException e) {
	           System.out.println(e);
	        }
	    Socket clientSocket = null;
	    try {
	    	clientSocket = MyService.accept();
	        }
	    catch (IOException e) {
	       System.out.println(e);
	    }
	    /*DataInputStream input = null;
	    String msg="";
	    try {
	       input = new DataInputStream(clientSocket.getInputStream());
	       msg = input.readUTF();
	       System.out.println(msg);
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }
	    PrintStream output = null;
	    try {
	       output = new PrintStream(clientSocket.getOutputStream());
	       output.print(msg+" hrhe");
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }*/
	    try{
			InputStream in = clientSocket.getInputStream();
			DataInputStream fromClient = new DataInputStream(in);
			String st = fromClient.readUTF();
			OutputStream out = clientSocket.getOutputStream();
			DataOutputStream toClient = new DataOutputStream(out);
			while(st != "end"){
				System.out.println(st);
				toClient.writeUTF(st+" ye le");
				st = fromClient.readUTF();
			}
		}
		catch(IOException e){
			System.out.println(e);
		}
	    try {
	       /* output.close();
	        input.close();*/
	        clientSocket.close();
	        MyService.close();
	     } 
	     catch (IOException e) {
	        System.out.println(e);
	     }
	}
}