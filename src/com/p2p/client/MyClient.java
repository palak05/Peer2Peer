package com.p2p.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket client = null;
		try{
			client = new Socket("169.254.89.58",7732);
		}
		catch(IOException e){
			System.out.println(e);
		}
		try{
			OutputStream out = client.getOutputStream();
			DataOutputStream toServer = new DataOutputStream(out);
			Scanner scan = new Scanner(System.in);
			String str = null;
			InputStream in = client.getInputStream();
			DataInputStream fromServer = new DataInputStream(in);
			do{
				str = scan.next();
				toServer.writeUTF(str);
				str = fromServer.readUTF();
				System.out.println(str +" mil gya");
			}while(str!="end");
			
		}
		catch(IOException e){
			System.out.println(e);
		}
	}

}
