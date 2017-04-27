package com.p2p.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Client_P2P {
	
	public static final String FOLDER_LOCATION = "C:/Users/admin/Documents/IP/clientfolders/";

	public static void main(String[] args) throws UnknownHostException {
		
		Random random = new Random();
		int port = random.nextInt(40000) + 10001;
		ServerSocket serverSocket = null;
		System.out.println("Assign a folder number to this client: ");
		Scanner scanner = new Scanner(System.in);
		Integer foldernumber = scanner.nextInt();
		String folderName = FOLDER_LOCATION + foldernumber.toString() + "/";	
		System.out.println("What is server's IP address?");
		String serverIP = scanner.next();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		Thread thread1 = new Thread(new MyClient(port, folderName, serverIP));
		thread1.start();
		Thread thread = new Thread(new P2PThread(serverSocket, folderName));
		thread.run();
	}

}
