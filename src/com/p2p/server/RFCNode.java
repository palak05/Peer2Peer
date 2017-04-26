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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + rfcNo;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RFCNode other = (RFCNode) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (rfcNo != other.rfcNo)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RFCNode [rfcNo=" + rfcNo + ", client_port=" + hostName.portNo + ", hostName=" + hostName.hostName + ", title=" + title + "]";
	}
}
