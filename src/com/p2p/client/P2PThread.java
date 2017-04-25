package com.p2p.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PThread implements Runnable{
	
	ServerSocket serverSocket;
	String folderName;
	
	public P2PThread(ServerSocket serverSocket, String folderName) {
		super();
		this.serverSocket = serverSocket;
		this.folderName = folderName;
	}

	@Override
	public void run() {
		Socket clientSocket = null;
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				Thread clientP2PThread = new Thread(new ClientP2PThread(clientSocket, folderName));
				clientP2PThread.start();
				
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

}
