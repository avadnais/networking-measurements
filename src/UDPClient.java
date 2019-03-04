import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter host: ");
        String host = sc.next();
        System.out.print("Enter port: ");
        int port = Integer.parseInt(sc.next());


        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();

        InetAddress address = InetAddress.getByName(host);

        int TRIALS = 5;

        for(int i = 0; i < TRIALS; i++) {
            sendAndReceiveTimed(1, new DatagramSocket(), address, port,false);
            sendAndReceiveTimed(1, new DatagramSocket(), address, port, false);
            sendAndReceiveTimed(64, new DatagramSocket(), address, port, false);
            sendAndReceiveTimed(1024, new DatagramSocket(), address, port, false);
        }

        socket.close();
    }

    static void sendAndReceive(int size, DatagramSocket socket, InetAddress address, int port) throws IOException {

        //socket.connect(address, port);
        byte[] msg = new byte[size];
        Arrays.fill(msg, (byte) 1);
        DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);
        socket.send(packet);
        socket.receive(packet);
        socket.close();

    }

    static void sendAndReceiveTimed(int size, DatagramSocket socket, InetAddress address, int port, boolean display) throws IOException {
        long now;
        double duration;

        now = System.nanoTime();
        sendAndReceive(size, socket, address, port);
        duration = System.nanoTime() - now;

        if(display) System.out.println("RTT for " + size + " bytes: " + duration / (double)1000000 + " ms");
    }


}
