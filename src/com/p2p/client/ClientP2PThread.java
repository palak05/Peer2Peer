package com.p2p.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientP2PThread implements Runnable {

	Socket socket = null;
	String folderName = null;

	public ClientP2PThread(Socket socket, String folderName) {
		this.socket = socket;
		this.folderName = folderName;
	}

	@Override
	public void run() {
		int portNo = 0;
		String hostName = "";
		String os = "";
		try {
			InputStream in = socket.getInputStream();
			DataInputStream fromClient = new DataInputStream(in);
			OutputStream out = socket.getOutputStream();
			DataOutputStream toClient = new DataOutputStream(out);
			String request = fromClient.readUTF();
			System.out.println(request);
			String response = "";
			String[] s = request.split("<cr><lf>");

			int statusCode = 0;
			String statusPhrase = "";
			String data = "";
			boolean fileFound = false;
			String[] type = s[0].split("<sp>");
			if (type[0].equalsIgnoreCase("GET")) {
				System.out.println("Inside if");
				String version = type[3];
				int rfcNumber = Integer.parseInt(type[2]);

				response += version + "<sp>";

				if (!version.equals("P2P-CI/1.0")) {
					statusCode = 505;
					statusPhrase = "P2P-CI Version Not Supported";
				}
				if (statusCode != 505) {
					File folder = new File(this.folderName);
					File[] listOfFiles = folder.listFiles();
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].getName().equals("rfc" + rfcNumber + ".txt")) {
							fileFound = true;
							
							FileReader f = new FileReader(listOfFiles[i].getPath());
							BufferedReader buff = new BufferedReader(f);
							String line = "";
							while ((line = buff.readLine()) != null) {
								data += line + "\n";
							}

						}

					}
					if(fileFound){
						statusCode = 200;
						statusPhrase = "OK";
						}else{
						statusCode = 404;
						statusPhrase = "Not Found";
					}
					
				}

				response += statusCode + "<sp>" + statusPhrase + "<cr><lf><cr><lf>Date:<sp>" + rfcNumber + "<cr><lf>OS:"
						+ os + "<cr><lf><cr><lf>Last-Modified:<sp>" + hostName + "<cr><lf><cr><lf>Content-Length:<sp>"
						+ portNo + "<cr><lf><cr><lf>Content-Type:<sp>text/plain<cr><lf><cr><lf>" + data;
			}

			toClient.writeUTF(response);
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
