import java.io.Serializable;

class UDPHeader implements Serializable{
	public int number;
	public int length;
	public String name, description;
}

class UDPMessage extends UDPHeader {
	private byte[] data;

	UDPMessage(int n, byte[] bData, int l) {
		number = n;
		length = l;
		setData(bData);
		name = "";
		description = "";
	}

	UDPMessage(int n, byte[] bData, int l, String sName, String sDescr) {
		number = n;
		length = l;
		setData(bData);
		name = sName;
		description = sDescr;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}