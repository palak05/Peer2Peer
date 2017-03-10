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
		Socket client = null;
		try{
			client = new Socket("192.168.0.28",7732);
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
			//do{
				str = "ADD<sp>RFC<sp>123<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>1234<cr><lf>Title:<sp>A Proferred Official ICP<cr><lf>ADD<sp>RFC<sp>321<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>1234<cr><lf>Title:<sp>Domain Names and Company Name Retrieval<cr><lf><cr><lf>";
				toServer.writeUTF(str);
				System.out.println(fromServer.readUTF());
				
				str = "LOOKUP<sp>RFC<sp>123<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>1234<cr><lf>Title:<sp>Requirements for IPsec Remote Access Scenarios<cr><lf><cr><lf>";
				toServer.writeUTF(str);
				System.out.println(fromServer.readUTF());
				str = "LIST<sp>ALL<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>1234<cr><lf>";
				toServer.writeUTF(str);
				System.out.println(fromServer.readUTF());
				toServer.writeUTF("end");
				str = "LOOKUP<sp>RFC<sp>123<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>1234<cr><lf>Title:<sp>Requirements for IPsec Remote Access Scenarios<cr><lf><cr><lf>";
				toServer.writeUTF(str);
				System.out.println(fromServer.readUTF());
				//str = fromServer.readUTF();
				//System.out.println(str +" mil gya");
			//}while(str!="end");
			
		}
		catch(IOException e){
			System.out.println(e);
		}
	}

}
