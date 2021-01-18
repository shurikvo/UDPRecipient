import java.net.*; 
import java.io.*;

class UDPRecipient implements Runnable{
	private int port;
	private String path, name, fileName; 
	private boolean inRecMode, haveInfo;

	public Thread t;
	public byte[][] dataArc = new byte[500][];
	
	public UDPRecipient(String sPort, String sPath) {
		this.port = Integer.parseInt(sPort);
		this.path = sPath;
		this.name = "Port_"+sPort;		
		this.inRecMode = false;
		this.haveInfo = false;
		this.t = new Thread(this, this.name);
		System.out.println("New thread: " + this.t);
		this.fileName = "Result.dat";
		this.t.start();
	}
	
	private int compose() {
		String file = this.path+"\\"+this.fileName;
		FileOutputStream output;
		try {
			output = new FileOutputStream(file, false);
			for(int i = 0; i < 500; ++i) {
				if(dataArc[i] != null) {
					output.write(dataArc[i]);
					System.out.println("Add: "+(i+1)+": "+dataArc[i].length); 
				}
			}
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("compose file: "+e); 
			return -1; 
		} catch (IOException ex) {
			System.err.println("compose IO: "+ex); 
			return -1; 
		}
		return 0;
	}
	
	private int receive(){
		int number = 0, length = 0;
		UDPMessage udm;
		
		try{
			DatagramSocket ds = new DatagramSocket(this.port); 
			ds.setSoTimeout(10000);
			System.out.println("Waiting for: "+this.port);
			while (true){
				try {
					DatagramPacket pack = new DatagramPacket(new byte[1024], 1024); 
					ds.receive(pack);
					this.inRecMode = true;
					this.haveInfo = true;
					
					ByteArrayInputStream bis = new ByteArrayInputStream(pack.getData());
					ObjectInput in = null;
					try {
						in = new ObjectInputStream(bis);
						udm = (UDPMessage)in.readObject(); 
					} finally { try { if (in != null) { in.close(); } } catch (IOException ex) {} }
					
					this.fileName = udm.description;
					number = udm.number;
					length = udm.length;
					dataArc[number-1] = new byte[length];
					System.arraycopy(udm.getData(), 0, dataArc[number-1], 0, length);
					System.out.println("Received: "+pack.getLength()+": "+number+": "+length); 
				}catch(SocketTimeoutException et){
					this.inRecMode = false;
					if(this.haveInfo) {
						compose();
						break;
					}
				}
			} 
		}catch(Exception e){
			System.err.println(this.name + ".receive exception: "+e);
			return -1;
		} 
		return 0;
	} 
	
	public void run() {
		try {
			receive();
		}
		catch (Exception e) {
			System.out.println(this.name + ".run exception: "+e);
		}
		System.out.println(this.name + " finish");
	}	
}
