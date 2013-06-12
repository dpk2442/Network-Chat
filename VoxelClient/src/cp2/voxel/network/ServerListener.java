package cp2.voxel.network;

import cp2.voxel.network.packet.Packet;

public interface ServerListener {
	
	public void packetReceived(Packet packet);

}
