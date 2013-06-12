package cp2.voxel.network.packet;

public class PacketPlayer extends Packet {
	
	private static final long serialVersionUID = 5761880942399181301L;
	
	public int id;
	public int x;
	public int y;
	public int type;
	public int frame;
	public int scale;
	public String name;
	public int talking;
	public String message;
	
	public PacketPlayer(int id, int x, int y, int type, int frame, int scale, String name, int talking, String message) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
		this.frame = frame;
		this.scale = scale;
		this.name = name;
		this.talking = talking;
		this.message = message;
	}

}