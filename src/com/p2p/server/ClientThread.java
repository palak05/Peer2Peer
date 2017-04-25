package com.p2p.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientThread extends Thread {

	protected Socket socket = null;

	public ClientThread(Socket socket) {
		super();
		this.socket = socket;
	}

	public void run() {
		int portNo = 0;
		String hostName = "";
		String title = "";
		try {
			InputStream in = socket.getInputStream();
			DataInputStream fromClient = new DataInputStream(in);
			OutputStream out = socket.getOutputStream();
			DataOutputStream toClient = new DataOutputStream(out);
			String request = fromClient.readUTF();
			while (!request.equals("end")) {
				String response = "";
				int flag = 0;
				String[] s = request.split("<cr><lf>");
				for (int m = 0; m < s.length; m = m + 4) {
					int statusCode = 0;
					String statusPhrase = "";
					String[] type = s[m].split("<sp>");
					if (type[0].equals("ADD")) {
						String version = type[3];
						int rfcNumber = Integer.parseInt(type[2]);

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
							if (getClients(hostName) != null) {
								flag = 1;
							}
							if (flag == 0 && addClient(portNo, hostName)) {
								flag = 1;
							}

							if (flag == 1 && addRFC(rfcNumber, portNo, hostName, title)) {
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
						String rfcTitle = "";
						for (int i = m + 1; i < m + 4; i++) {
							String[] sArray = s[i].split("<sp>");
							if (sArray[0].equals("Host:")) {
								hostName = sArray[1];
							}
							if (sArray[0].equals("Port:")) {
								portNo = Integer.parseInt(sArray[1]);
							}
							if (sArray[0].equals("Title:")) {
								rfcTitle = sArray[1];
							}
						}
						response += version + "<sp>";
						if (!version.equals("P2P-CI/1.0")) {
							statusCode = 505;
							statusPhrase = "P2P-CI Version Not Supported";
						}
						List<ClientNode> clients = null;
						if (statusCode != 505) {
							clients = findRFC(rfcNumber);
							if (clients.isEmpty()) {
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
								response += "RFC<sp>" + rfcNumber + "<sp>" + rfcTitle + "<sp>" + clientNode.hostName
										+ "<sp>" + clientNode.portNo + "<cr><lf>";
							}
						}
						response += "<cr><lf>";
					}
					if (type[0].equals("LIST")) {
						String version = type[2];
						for (int i = m + 1; i < m + 3; i++) {
							String[] sArray = s[i].split("<sp>");
							if (sArray[0].equals("Host:")) {
								hostName = sArray[1];
							}
							if (sArray[0].equals("Port:")) {
								portNo = Integer.parseInt(sArray[1]);
							}
						}
						response += version + "<sp>";
						if (!version.equals("P2P-CI/1.0")) {
							statusCode = 505;
							statusPhrase = "P2P-CI Version Not Supported";
						}
						if (statusCode != 505) {
							String result = getAllRFCs();
							if (result.length() == 16) {
								statusCode = 404;
								statusPhrase = "Not Found";
							} else {
								statusCode = 200;
								statusPhrase = "OK";
							}
							response += statusCode + "<sp>" + statusPhrase + result;
						}
					}
				}

				for (ClientNode clientNode : MyServer.clientList) {
					System.out.println(clientNode.portNo + " " + clientNode.hostName);
				}
				System.out.println();
				toClient.writeUTF(response);
				request = fromClient.readUTF();
			}

		} catch (IOException e) {
			System.out.println(e);
			Iterator<ClientNode> iterator = MyServer.clientList.iterator();
			while(iterator.hasNext()) {
				ClientNode clientNode = iterator.next();
				if (clientNode.portNo == portNo) {
					iterator.remove();
				}
			}
			
			Iterator<RFCNode> iterator2 = MyServer.rfcList.iterator();
			ClientNode clientNode = new ClientNode(portNo, hostName);
			while(iterator.hasNext()) {
				RFCNode rfcNode = iterator2.next();
				if (rfcNode.hostName.equals(clientNode)) {
					iterator2.remove();
				}
			}
		}
	}

	public boolean addClient(int portNo, String hostName) {
		return MyServer.clientList.add(new ClientNode(portNo, hostName));
	}

	public boolean addRFC(int rfcNo, int portNo, String hostName, String title) {
		return MyServer.rfcList.add(new RFCNode(rfcNo, new ClientNode(portNo, hostName), title));

	}

	public List<ClientNode> findRFC(int rfcNo) {
		String hostName = "";
		System.out.println("Searching: " + rfcNo);
		List<ClientNode> clients = new ArrayList<>();
		for (RFCNode rfcNode : MyServer.rfcList) {
			System.out.println("Checking with: " + rfcNode.rfcNo);
			if (rfcNode.rfcNo == rfcNo) {
				clients.add(rfcNode.hostName);
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

	private String getAllRFCs() {
		String response = "<cr><lf>";
		for (RFCNode rfcNode : MyServer.rfcList) {
			if (rfcNode.hostName == null) {
				MyServer.rfcList.remove(rfcNode);
				continue;
			}
			response += "RFC<sp>" + rfcNode.rfcNo + "<sp>" + rfcNode.title + "<sp>" + rfcNode.hostName.hostName + "<sp>"
					+ rfcNode.hostName.portNo + "<cr><lf>";
		}
		response += "<cr><lf>";
		return response;

	}
}
