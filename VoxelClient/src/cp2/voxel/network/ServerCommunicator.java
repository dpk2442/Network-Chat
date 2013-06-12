package cp2.voxel.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cp2.voxel.network.packet.Packet;

public class ServerCommunicator extends Thread {
	
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private Socket socket = null;
	
	private ServerListener listener;
	
	public ServerCommunicator(String host, int port, ServerListener listener) {
		this.listener = listener;
		try {
			// open a socket connection
			socket = new Socket(host, port);
			// open I/O streams for objects
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		this.start();
	}
	
	public void sendMessage(Object obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			Object fromServer = null;
			try {
				fromServer = ois.readObject();
			} catch (Exception e) {}
			if (fromServer instanceof Packet) {
				listener.packetReceived((Packet)fromServer);
			} else if (fromServer == null) {
				listener.packetReceived(null);
			}
		}
	}

}
