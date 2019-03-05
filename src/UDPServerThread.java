import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;

    public UDPServerThread() throws IOException {
        this("UDPServerThread");
    }

    public UDPServerThread(String name) throws IOException {
        super(name);
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter port: ");

        socket = new DatagramSocket(Integer.parseInt(sc.next()));
        System.out.println("Socket created");

    }

    public void run() {

        Scanner sc = new Scanner(System.in);



        try {

            /* THROUGHPUT AND LATENCY */


            //ack
            receiveAndSend(1, socket);

            receiveAndSend(1, socket);
            receiveAndSend(64, socket);
            receiveAndSend(1024, socket);

            /* MESSAGES OF VARIOUS SIZED TOTALING 1MB*/

            for(int i = 0; i < 1024; i++){
                receiveAndSend(1024, socket);
            }

            for(int i = 0; i < 2048; i++){
                receiveAndSend(512, socket);
            }

            for(int i = 0; i < 4096; i++){
                receiveAndSend(256, socket);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }


    static void receiveAndSend(int size, DatagramSocket socket) throws IOException {
        byte[] msg = new byte[size];
        DatagramPacket packet = new DatagramPacket(msg, size);
        socket.receive(packet);
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(msg, size, address, port);
        socket.send(packet);


    }
}