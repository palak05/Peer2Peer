package com.p2p.server;

public class ClientNode {
	
		int portNo;
		String hostName;
		
		public ClientNode(int portNo, String hostName) {
			super();
			this.portNo = portNo;
			this.hostName = hostName;
		}
		
		public int getPortNo() {
			return portNo;
		}

		public String getHostName() {
			return hostName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
			result = prime * result + portNo;
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
			ClientNode other = (ClientNode) obj;
			if (hostName == null) {
				if (other.hostName != null)
					return false;
			} else if (!hostName.equals(other.hostName))
				return false;
			if (portNo != other.portNo)
				return false;
			return true;
		}
}
