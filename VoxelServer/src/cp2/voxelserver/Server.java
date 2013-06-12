package cp2.voxelserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import cp2.voxel.network.packet.Packet;
import cp2.voxel.network.packet.PacketConnect;
import cp2.voxel.network.packet.PacketDisconnect;

public class Server extends Thread {

	private Terminal terminal;
	private ServerSocket voxelServer;
	private volatile ArrayList<Client> clients;
	
	private Server me = this;
	
	private int newestid;

	public static void main(String argv[]) throws Exception {
		new Server();
	}

	public Server() throws Exception {
		newestid = 0;
		clients = new ArrayList<Client>();
		terminal = new Terminal("MyWorld Server");
		int port = 3000;
		try {
			voxelServer = new ServerSocket(port);
			terminal.println("MyWorld Server listening on port " + voxelServer.getLocalPort() + ".");
			terminal.println("Type \"list\" to list clients.");
			terminal.println("Type \"exit\" or \"quit\" to stop the server.");
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							Socket client = voxelServer.accept();
							terminal.println("Accepted a connection from: " + client.getInetAddress());
							synchronized (clients) {
								clients.add(new Client(client, me, terminal, newestid));
							}
							newestid++;
						} catch (IOException e) {}
					}
				}
			}).start();
			this.start();
		} catch (BindException e) {
			terminal.println("Could not bind to port. Either another server is running, or you don't have the permissions to use that port.");
			terminal.println("Press enter to exit.");
			terminal.readLine();
			System.exit(1);
		}
	} 

	public void run() {
		while (true) {
			String userInput = terminal.readLine();
			if (userInput.equals("exit") || userInput.equals("stop")) {
				terminal.println("Server is exiting...");
				System.exit(0);
			} else if (userInput.equals("list")) {
				StringBuilder str = new StringBuilder();
				synchronized (clients) {
					for (Client client : clients)
						str.append(client.getInetAddress() + ", ");
				}
				String clientList = str.toString();
				if (clientList.length() > 3)
					terminal.println("Connected clients: " + clientList.substring(0, clientList.length() - 2));
			}
		}
	}
	
	public void sendToAllClients(Packet packet) {
		synchronized (clients) {
			for (Client client : clients) {
				client.sendPacket(packet);
			}
		}
	}
	
	public void removeClient(Client client) {
		synchronized (clients) {
			clients.remove(client);
		}
	}
}

class Client extends Thread {
	private Socket client = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private Server server = null;
	private Terminal terminal;
	
	private int id;

	public Client(Socket clientSocket, Server server, Terminal terminal, int id) {
		client = clientSocket;
		this.server = server;
		this.terminal = terminal;
		this.id = id;
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
		} catch(Exception e1) {
			try {
				client.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		this.sendPacket(new PacketConnect(id));
		this.start();
	}
	
	public InetAddress getInetAddress() {
		return client.getInetAddress();
	}
	
	public void sendPacket(Packet packet) {
		try {
			oos.writeObject(packet);
			oos.flush();
		} catch (IOException e) {}
	}

	public void run() {
		while (true) {
			try {
				Object readObject = ois.readObject();
				if (readObject instanceof Packet) {
					server.sendToAllClients((Packet)readObject);
				} else if (readObject == null) {
					server.removeClient(this);
				}
			} catch(Exception e) {
				server.sendToAllClients(new PacketDisconnect(this.id));
				terminal.println("Client " + client.getInetAddress() + " disconnected.");
				server.removeClient(this);
				break;
			}
		}
	}
}