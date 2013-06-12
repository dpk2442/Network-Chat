package cp2.voxel.network.packet;

public class PacketDisconnect extends Packet {
	
	private static final long serialVersionUID = 4050438771864211690L;
	
	public int id;
	
	public PacketDisconnect(int id) {
		this.id = id;
	}

}
