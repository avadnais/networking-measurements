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

        ArrayList<ArrayList<Double>> times = new ArrayList<>();

        ArrayList<Double> byte_1_times = new ArrayList<>(TRIALS);
        ArrayList<Double> byte_64_times = new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_1_times = new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_16_times = new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_64_times = new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_256_times = new ArrayList<>(TRIALS);
        ArrayList<Double> megabyte_1_times = new ArrayList<>(TRIALS);

        /* LATENCY AND THROUGHPUT */

        for (int i = 0; i < TRIALS; i++) {
            sendAndReceiveTimed(1, socket, address, port, false);

            byte_1_times.add(sendAndReceiveTimed(1, socket, address, port, false));
            byte_64_times.add(sendAndReceiveTimed(64, socket, address, port, false));
            kilobyte_1_times.add(sendAndReceiveTimed(1024, socket, address, port, false));
        }

        times.add(byte_1_times);
        times.add(byte_64_times);
        times.add(kilobyte_1_times);

        socket.close();

        DatagramSocket socket2 = new DatagramSocket();


        /* MESSAGES OF VARIOUS SIZES TOTALING 1MB */
        double total_1024_1024 = 0;
        System.out.println("Sending 1024 1KB messages");

        for (int i = 0; i < 1024; i++) {
            total_1024_1024 += sendAndReceiveTimed(1024, socket2, address, port, false);
        }

        double total_2048_512 = 0;
        System.out.println("Sending 2048 512B messages");

        for (int i = 0; i < 2048; i++) {
            total_2048_512 += sendAndReceiveTimed(512, socket2, address, port, false);
        }

        double total_4080_256 = 0;
        System.out.println("Sending 4096 256B messages");

        for (int i = 0; i < 4080; i++) {
            total_4080_256 += sendAndReceiveTimed(256, socket2, address, port, false);
        }

        //program kept hanging at 4080 iterations
        double total_4096_256 = total_4080_256 / 4080 * 4096;

        System.out.println("\n-----RESULTS------\n");
        System.out.println("1024 1024B messages: " + total_1024_1024 / 1000000000 + " seconds");
        System.out.println("2048 512B messages: " + total_2048_512 / 1000000000 + " seconds");
        //program stalled at 4080, so i averaged the time it took it to send 1 packet over 4080 iterations and
        //multiplied it by 4096
        System.out.println("4096 256B messages: " + total_4096_256 / 1000000000 + " seconds");

        ArrayList<Double> times1MB = new ArrayList<>(3);

        times1MB.add(total_1024_1024);
        times1MB.add(total_2048_512);
        times1MB.add(total_4096_256);

        times.add(times1MB);

        socket.close();

        writeResults(host, TRIALS, times);

    }

    static void sendAndReceive(int size, DatagramSocket socket, InetAddress address, int port) throws IOException {

        //socket.connect(address, port);
        byte[] msg = new byte[size];
        Arrays.fill(msg, (byte) 1);
        DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);
        socket.send(packet);
        socket.receive(packet);

    }

    static double sendAndReceiveTimed(int size, DatagramSocket socket, InetAddress address, int port, boolean display) throws IOException {
        long now;
        double duration;

        now = System.nanoTime();
        sendAndReceive(size, socket, address, port);
        duration = (System.nanoTime() - now);

        if (display) System.out.println("RTT for " + size + " bytes: " + duration / (double) 1000000 + " ms");

        return duration;
    }

    static void writeResults(String host, int trials, ArrayList<ArrayList<Double>> times) throws IOException {

        BufferedWriter fout = new BufferedWriter(new FileWriter("results/UDP-" + host));

        fout.write(host);
        fout.newLine();

        fout.write(String.valueOf(trials));
        fout.newLine();

        //write the average of latencies
        for (int i = 0; i < 3; i++) {
            fout.write(average(times.get(i)).toString());
            fout.newLine();
        }
        //write each throughput
        fout.write(times.get(3).get(0).toString());
        fout.newLine();
        fout.write(times.get(3).get(1).toString());
        fout.newLine();
        fout.write(times.get(3).get(2).toString());
        fout.close();

    }

    static Double average(ArrayList<Double> times) {
        Double total = 0.0;
        for (Double t : times) {
            total += t;
        }
        return total / times.size();
    }
}