package com.p2p.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
			int flag = 0;

			/*
			 * while(st != "end"){ System.out.println(st);
			 * toClient.writeUTF(st+" ye le"); st = fromClient.readUTF(); }
			 */

			String[] s = request.split("<cr><lf>");
			for (int m = 0; m < s.length; m = m + 4) {
				int statusCode = 0;
				String statusPhrase = "";
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

					response += version + "<sp>";
					if (!version.equals("P2P-CI/1.0")) {
						statusCode = 505;
						statusPhrase = "P2P-CI Version Not Supported";
					}
					if (statusCode != 505) {
						if (flag == 0 && addClient(portNo, hostName)) {
							flag = 1;
						}

						if (flag == 1 && addRFC(rfcNumber, hostName, title)) {
							statusCode = 200;
							statusPhrase = "OK";
						} else if (flag == 0) {
							statusCode = 400;
							statusPhrase = "Bad Request";
						}
					}
					response += statusCode + "<sp>" + statusPhrase + "<cr><lf><cr><lf>RFC<sp>" + rfcNumber + "<sp>"
							+ title + "<sp>" + hostName + "<sp>" + portNo + "<cr><lf><cr><lf>";
				}

				if (type[0].equals("LOOKUP")) {
					String version = type[3];
					int rfcNumber = Integer.parseInt(type[2]);
					int portNumber = 0;
					String Host = "";
					String title = "";
					for (int i = m + 1; i < m + 4; i++) {
						String[] sArray = s[i].split("<sp>");
						if (sArray[0].equals("Host:")) {
							Host = sArray[1];
						}
						if (sArray[0].equals("Port:")) {
							portNumber = Integer.parseInt(sArray[1]);
						}
						if (sArray[0].equals("Title:")) {
							title = sArray[1];
						}
					}
					response += version + "<sp>";
					if (!version.equals("P2P-CI/1.0")) {
						statusCode = 505;
						statusPhrase = "P2P-CI Version Not Supported";
					}
					List<ClientNode> clients = null;
					if (statusCode != 505) {
						clients = findRFC(portNumber);
						if (clients == null) {
							statusCode = 404;
							statusPhrase = "Not Found";
						} else {
							statusCode = 200;
							statusPhrase = "OK";
						}

					}
					response += statusCode + "<sp>" + statusPhrase + "<cr><lf><cr><lf>";
					if (statusCode == 200) {
						for (ClientNode clientNode : clients) {
							response += "RFC<sp>" + rfcNumber + "<sp>" + title + "<sp>" + clientNode.hostName + "<sp>"
									+ clientNode.portNo + "<cr><lf>";
						}
					}
					response += "<cr><lf>";
					// check if rfcnumber, title exist with any hostname in RFC
					// linkedlist
					// return list to the given host and port
					// System.out.println("Hostname: " + Host + " Port: " + Port
					// + " RFC: " + rfcNumber + " Title: "
					// + Title + " version: " + version);
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

		/*
		 * for (ClientNode clientNode : MyServer.clientList) {
		 * System.out.println(clientNode.portNo + " " + clientNode.hostName); }
		 */
	}

	public boolean addRFC(int rfcNo, String hostName, String title) {
		return MyServer.rfcList.add(new RFCNode(rfcNo, hostName, title));

		/*
		 * for (RFCNode rfcNode : MyServer.rfcList) {
		 * System.out.println(rfcNode.rfcNo + " " + rfcNode.hostName + " " +
		 * rfcNode.title); }
		 */
	}

	public List<ClientNode> findRFC(int rfcNo) {
		String hostName = "";
		List<ClientNode> clients = new ArrayList<>();
		for (RFCNode rfcNode : MyServer.rfcList) {
			if (rfcNode.rfcNo == rfcNo) {
				hostName = rfcNode.hostName;
				ClientNode clientNode = getClients(hostName);
				if (clientNode == null) {
					MyServer.rfcList.remove(rfcNode);
				} else {
					clients.add(clientNode);
				}
			}
		}

		return clients;
	}

	private ClientNode getClients(String hostName) {

		for (ClientNode clientNode : MyServer.clientList) {
			if (clientNode.hostName.equals(hostName)) {
				return clientNode;
			}
		}
		return null;
	}
}
