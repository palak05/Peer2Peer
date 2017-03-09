package com.p2p.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread {

	protected Socket socket = null;

	public ClientThread(Socket socket) {
		super();
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream in = socket.getInputStream();
			DataInputStream fromClient = new DataInputStream(in);
			String request = fromClient.readUTF();
			OutputStream out = socket.getOutputStream();
			DataOutputStream toClient = new DataOutputStream(out);
			String response = "";
			/*
			 * while(st != "end"){ System.out.println(st);
			 * toClient.writeUTF(st+" ye le"); st = fromClient.readUTF(); }
			 */

			// String str =
			// "ADD<sp>RFC<sp>123<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>1234<cr><lf>Title:<sp>A
			// Proferred Official
			// ICP<cr><lf>ADD<sp>RFC<sp>321<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>567<cr><lf>Title:<sp>Domain
			// Names and Company Name Retrieval<cr><lf><cr><lf>";
			// String str =
			// "LOOKUP<sp>RFC<sp>3457<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>1234<cr><lf>Title:<sp>Requirements
			// for IPsec Remote Access Scenarios<cr><lf><cr><lf>";
			// String str =
			// "LIST<sp>ALL<sp>P2P-CI/1.0<cr><lf>Host:<sp>thishost.csc.ncsu.edu<cr><lf>Port:<sp>5678<cr><lf>";
			String[] s = request.split("<cr><lf>");
			for (int m = 0; m < s.length; m = m + 4) {
				String[] type = s[m].split("<sp>");
				if (type[0].equals("ADD")) {
					String version = type[3];
					int rfcNumber = Integer.parseInt(type[2]);
					int portNo = 0;
					String hostName = "";
					String title = "";
					for (int i = m + 1; i < m + 4; i++) {
						String[] sArray = s[i].split("<sp>");
						if (sArray[0].equals("Host:")) {
							hostName = sArray[1];
						}
						if (sArray[0].equals("Port:")) {
							portNo = Integer.parseInt(sArray[1]);
						}
						if (sArray[0].equals("Title:")) {
							title = sArray[1];
						}
					}
					// check if port number and hostname exist in client
					// linkedlist (insert just one time, dont check everytime)
					// Add in linkedlist of RFC to add with rfc, host and title
					/*System.out.println("Hostname: " + Host + " Port: " + Port + " RFC: " + rfcNumber + " Title: "
							+ Title + " version: " + version);
					String status = "code<sp>phrase";
					String ret = version + "<sp>" + status + "<cr><lf>RFC<sp>" + rfcNumber + "<sp>" + Title + "<sp>"
							+ Host + "<sp>" + Port;
					System.out.println(ret);*/
					
					addClient(portNo, hostName);
					addRFC(rfcNumber, hostName, title);
				}
				
				if (type[0].equals("LOOKUP")) {
					String version = type[3];
					int rfcNumber = Integer.parseInt(type[2]);
					int Port = 0;
					String Host = "";
					String Title = "";
					for (int i = m + 1; i < m + 4; i++) {
						String[] sArray = s[i].split("<sp>");
						if (sArray[0].equals("Host:")) {
							Host = sArray[1];
						}
						if (sArray[0].equals("Port:")) {
							Port = Integer.parseInt(sArray[1]);
						}
						if (sArray[0].equals("Title:")) {
							Title = sArray[1];
						}
					}
					// check if rfcnumber, title exist with any hostname in RFC
					// linkedlist
					// return list to the given host and port
					System.out.println("Hostname: " + Host + " Port: " + Port + " RFC: " + rfcNumber + " Title: "
							+ Title + " version: " + version);
				}
				if (type[0].equals("LIST")) {
					String version = type[2];
					int Port = 0;
					String Host = "";
					String Title = "";
					for (int i = m + 1; i < m + 3; i++) {
						String[] sArray = s[i].split("<sp>");
						if (sArray[0].equals("Host:")) {
							Host = sArray[1];
						}
						if (sArray[0].equals("Port:")) {
							Port = Integer.parseInt(sArray[1]);
						}
					}
					// return whole RFC LinkedList content to this host and port
					System.out.println("Hostname: " + Host + " Port: " + Port + " version: " + version);
				}
			}

		} catch (IOException e) {
			System.out.println(e);
		}
		// When client closes connection
		/*
		 * try { socket.close(); } catch (IOException e) {
		 * System.out.println(e); }
		 */
	}

	public boolean addClient(int portNo, String hostName) {
		return MyServer.clientList.add(new ClientNode(portNo, hostName));

		/*for (ClientNode clientNode : MyServer.clientList) {
			System.out.println(clientNode.portNo + " " + clientNode.hostName);
		}
*/
	}

	public boolean addRFC(int rfcNo, String hostName, String title) {
		return MyServer.rfcList.add(new RFCNode(rfcNo, hostName, title));

		/*for (RFCNode rfcNode : MyServer.rfcList) {
			System.out.println(rfcNode.rfcNo + " " + rfcNode.hostName + " " + rfcNode.title);
		}
*/
	}
}
