# Peer2Peer

Team:
Palak Agrawal (pagrawa2)
Prerit Bhandari (pbhanda2)

We have implemented a peer-to-peer (P2P) system with centralized index (CI). This system will allow users to download RFC's from their peers rather than from centralized server. The server would just let users know about the active connections and whether they have that particular RFC or not.

There is a centralized server which is running on port 7734 and maintaining two lists, first is client list with all the active clients (hostname and port number) and second is RFC list with all the available RFC's with all active clients (RFC number, RFC title, hostname). Different clients run on different threads while they are connected to the server.

Clients are allotted port numbers randomly. They maintain two different threads, one for making requests to server and other to listen to requests from other peers. On startup, client should be allotted a folder which acts as its repository for RFC's. It will then ask for server's IP address. Provide the machine's IP address on which server is running. After this, client will let server know about all the RFC's available with it in its folder. 

It will then get several options, user can choose to either ADD, LOOKUP or get a LIST of all available RFC's with the server. It can also request another active peer for RFC and download it from there. After downloading an RFC, it will inform server about this addition and it will be entered in the list maintained by the server.

How to run our code?

* Create an Eclipse project with same name as our project and change the location to the place where you have extracted our project files.
* Change location of client folders and provide a valid location in Client_P2P.java (variable FOLDER_LOCATION).
* To start the server, run MyServer.java
* To start a client, run Client_P2P.java
...Provide a folder number for this client.
...Provide server's IP address.
...Choose a menu option or quit :)

