package cp2.voxel.network.packet;

public class PacketConnect extends Packet {
	
	private static final long serialVersionUID = -2563210802528646750L;
	
	public int id;
	
	public PacketConnect(int id) {
		this.id = id;
	}

}
