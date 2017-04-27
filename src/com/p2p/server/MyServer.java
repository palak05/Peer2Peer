package com.p2p.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MyServer {

	public static volatile List<ClientNode> clientList = new LinkedList<>();
	public static volatile List<RFCNode> rfcList = new LinkedList<>();

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(7734);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Socket clientSocket = null;
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				ClientThread clientThread = new ClientThread(clientSocket);
				clientThread.start();

			} catch (IOException e) {
				System.out.println(e);
			}
		}

	}
}