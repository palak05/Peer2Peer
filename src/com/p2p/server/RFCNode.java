package com.p2p.server;

public class RFCNode {

	int rfcNo;
	ClientNode hostName;
	String title;

	public RFCNode(int rfcNo, ClientNode hostName, String title) {
		super();
		this.rfcNo = rfcNo;
		this.hostName = hostName;
		this.title = title;
	}
	
	public int getRfcNo() {
		return rfcNo;
	}

	public ClientNode getHostName() {
		return hostName;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "RFCNode [rfcNo=" + rfcNo + ", client_port=" + hostName.portNo + ", hostName=" + hostName.hostName + ", title=" + title + "]";
	}
}
