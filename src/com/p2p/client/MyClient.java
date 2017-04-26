package com.p2p.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MyClient implements Runnable {

	int portNumber;
	String folderName;
	String serverIP;
	String ipAddress;

	public MyClient(int portNumber, String folderName, String serverIP) {
		super();
		this.portNumber = portNumber;
		this.folderName = folderName;
		this.serverIP = serverIP;
	}

	public void run() {

		Socket client = null;

		try {
			client = new Socket(serverIP, 7732);
			ipAddress = client.getLocalAddress().getHostAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			System.out.println("Welcome to P2P-CI system!!");
			System.out.println("What would you like to do?");
			Scanner scanner = new Scanner(System.in);
			int a = 0;
			OutputStream out = client.getOutputStream();
			DataOutputStream toServer = new DataOutputStream(out);
			String str = null;
			InputStream in = client.getInputStream();
			DataInputStream fromServer = new DataInputStream(in);

			// adding all local files to server
			File folder = new File(folderName);
			File[] listOfFiles = folder.listFiles();
			for (int i = 1; i < listOfFiles.length; i++) {
				Integer rfcNo = Integer
						.parseInt(listOfFiles[i].getName().substring(3, (int) (listOfFiles[i].getName().length() - 4)));
				String str2 = "ADD<sp>RFC<sp>" + rfcNo + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
						+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + listOfFiles[i].getName()
						+ "<cr><lf>";
				toServer.writeUTF(str2);
				toServer.flush();
				fromServer.readUTF();
			}

			while (a <= 5) {
				System.out.println("1. Add RFC");
				System.out.println("2. Lookup RFC");
				System.out.println("3. Get list of RFCs");
				System.out.println("4. Choose a client to download RFC from:");
				System.out.println("5. Quit");
				System.out.println("Pick an option:");
				a = scanner.nextInt();
				switch (a) {
				case 1:
					System.out.println("Provide RFC number which you want to add:");
					int rfcNumber = scanner.nextInt();
					str = "ADD<sp>RFC<sp>" + rfcNumber + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
							+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + "rfc" + rfcNumber + ".txt"
							+ "<cr><lf>";
					toServer.writeUTF(str);
					System.out.println(fromServer.readUTF());
					break;

				case 2:
					System.out.println("Provide the RFC number which you want to lookup:");
					int rfcNo = scanner.nextInt();
					str = "LOOKUP<sp>RFC<sp>" + rfcNo + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
							+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + "rfc" + rfcNo + ".txt"
							+ "<cr><lf><cr><lf>";
					toServer.writeUTF(str);
					System.out.println(fromServer.readUTF());
					break;

				case 3:
					System.out.println("The list of RFCs is as follows:");
					str = "LIST<sp>ALL<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress + "<cr><lf>Port:<sp>" + portNumber
							+ "<cr><lf>";
					toServer.writeUTF(str);
					System.out.println(fromServer.readUTF());
					break;

				case 4:
					System.out.println("Provide RFC number which you want:");
					int rfcNum = scanner.nextInt();
					System.out.println("Provide hostname from which you want to download RFC:");
					String hostName = scanner.next();
					System.out.println("Provide port number of the client:");
					int portNumber = scanner.nextInt();
					str = "GET<sp>RFC<sp>" + rfcNum + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + hostName + "<sp>Port:<sp>"
							+ portNumber + "<cr><lf>OS:<sp>Mac<sp>OS<sp>10.4.1<cr><lf>";
					Socket s2 = new Socket(hostName, portNumber);
					DataOutputStream outputStream2 = new DataOutputStream(s2.getOutputStream());
					InputStream inputStream2 = s2.getInputStream();
					DataInputStream inputStreamReader2 = new DataInputStream(inputStream2);
					outputStream2.writeUTF(str);
					String output = inputStreamReader2.readUTF();
					List<String> response = Arrays.asList(output.split("<cr><lf>"));
					String data = response.get(response.size() - 1);
					try {
						PrintWriter writer = new PrintWriter(folderName + "rfc" + rfcNum + ".txt", "UTF-8");
						writer.println(data);
						writer.close();
						str = "ADD<sp>RFC<sp>" + rfcNum + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
								+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + "rfc" + rfcNum + ".txt"
								+ "<cr><lf>";
						toServer.writeUTF(str);
						System.out.println(fromServer.readUTF());
					} catch (IOException e) {
						e.printStackTrace();
					}
					s2.close();
					break;

				case 5:
					client.close();
					Thread.currentThread().stop();
					break;
					
				default:
					System.out.println("Kindly make a valid choice!");
					a = scanner.nextInt();
					break;
				}
			}
			//client.close();
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
