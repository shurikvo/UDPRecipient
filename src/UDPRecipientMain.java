import java.io.*;

class UDPRecipientMain{
    public static void main(String[] args){
        UDPRecipient rcp;

        if(args.length < 2) {
            System.err.println("Call me: UDPRecipientMain <port> <path>");
            System.exit(0);
        }

        try {
            rcp = new UDPRecipient(args[0], args[1]);
            rcp.t.join();
        }
        catch (InterruptedException e) {
            System.out.println("Main thread exception: "+e);
        }
        System.out.println("Main thread finish");
    }
}

