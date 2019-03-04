
/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TCPClient {

    static final int TRIALS = 1;

    public static void main(String args[]) throws IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter host: ");
        String host = sc.next();
        System.out.print("Enter port: ");
        int port = Integer.parseInt(sc.next());
        System.out.println(host + ":" + port);

        byte[] msg_1B = new byte[1];
        byte[] msg_64B = new byte[64];
        byte[] msg_256B = new byte[256];
        byte[] msg_512B = new byte[512];
        byte[] msg_1KB = new byte[1024];
        byte[] msg_16KB = new byte[1024 << 4];
        byte[] msg_64KB = new byte[1024 << 6];
        byte[] msg_256KB = new byte[1024 << 8];
        byte[] msg_1MB = new byte[1024 << 10];

        //byte[] msg_1_5MB = new byte[1 << 25];
        //Arrays.fill(msg_1MB, (byte) 1);


        ArrayList<ArrayList<Double>> times = new ArrayList<>(5);

        ArrayList<Double> byte_1_times= new ArrayList<>(TRIALS);
        ArrayList<Double> byte_64_times= new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_1_times= new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_16_times= new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_64_times= new ArrayList<>(TRIALS);
        ArrayList<Double> kilobyte_256_times= new ArrayList<>(TRIALS);
        ArrayList<Double> megabyte_1_times= new ArrayList<>(TRIALS);


        /* TESTING LATENCY AND THROUGPUT */

        for(int i = 0; i < TRIALS; i++) {


            //ack
            if(sendAndReceiveTimed(msg_1B, host, port, false) > 0) System.out.println("Ack successful");

            System.out.println("Sending and receiving 1 byte");
            byte_1_times.add(sendAndReceiveTimed(msg_1B, host, port, false));
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Sending and receiving 64 bytes");
            byte_64_times.add(sendAndReceiveTimed(msg_64B, host, port, false));
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Sending and receiving 1 kilobyte");
            kilobyte_1_times.add(sendAndReceiveTimed(msg_1KB, host, port, false));
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Sending and receiving 16 kilobytes");
            kilobyte_16_times.add(sendAndReceiveTimed(msg_16KB, host, port, false));
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Sending and receiving 64 kilobytes");
            kilobyte_64_times.add(sendAndReceiveTimed(msg_64KB, host, port, false));
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Sending and receiving 256 kilobytes");
            kilobyte_256_times.add(sendAndReceiveTimed(msg_256KB, host, port, false));
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Sending and receiving 1 megabyte");
            megabyte_1_times.add(sendAndReceiveTimed(msg_1MB, host, port, false));
            TimeUnit.SECONDS.sleep(1);
        }

        times.add(byte_1_times);
        times.add(byte_64_times);
        times.add(kilobyte_1_times);
        times.add(kilobyte_16_times);
        times.add(kilobyte_64_times);
        times.add(kilobyte_256_times);
        times.add(megabyte_1_times);


        System.out.println("\n------TCP RESULTS (" + TRIALS + " trials)-----\n");

        for(ArrayList<Double> size : times){
            double total = 0;
            double average;
            for(double time : size){
                total += time;
            }
            average = total / TRIALS;

            switch(times.indexOf(size)){
                case(0):
                    System.out.println("Average RTT for 1 byte: " + average / (double) 1000000 + " ms");
                    System.out.println("Throughput for 1 byte : " + throughputMbps(1, average) + " Mbps\n");
                    break;
                case(1):
                    System.out.println("Average RTT for 64 bytes: " + average / (double) 1000000 + " ms");
                    System.out.println("Throughput for 64 bytes : " + throughputMbps(64, average) + " Mbps\n");
                    break;
                case(2):
                    System.out.println("Average RTT for 1 kilobyte: " + average / (double) 1000000 + " ms");
                    System.out.println("Throughput for 1 kilobyte : " + throughputMbps(1024, average) + " Mbps\n");
                    break;
                case(3):
                    System.out.println("Average RTT for 16 kilobytes: " + average / (double) 1000000 + " ms");
                    System.out.println("Throughput for 16 kilobytes : " + throughputMbps(1024 << 4, average) + " Mbps\n");
                    break;
                case(4):
                    System.out.println("Average RTT for 64 kilobytes: " + average / (double) 1000000 + " ms");
                    System.out.println("Throughput for 64 kilobytes : " + throughputMbps(1024 << 6, average) + " Mbps\n");
                    break;
                case(5):
                    System.out.println("Average RTT for 256 kilobytes: " + average / (double) 1000000 + " ms");
                    System.out.println("Throughput for 256 kilobytes : " + throughputMbps(1024 << 8, average) + " Mbps\n");
                    break;
                case(6):
                    System.out.println("Average RTT for 1 megabyte: " + average / (double) 1000000 + " ms");
                    System.out.println("Throughput for 1 megabyte : " + throughputMbps(1024 << 10, average) + " Mbps");
                    break;
            }

        }


        /*
        *
        * TESTING TIME TO SEND VARIOUS MESSAGE SIZES TOTALLING 1MB
        *
        * Measure the interaction between message size and number of messages using TCP and UDP
        * by sending 1MByte of data (with a 1-byte acknowledgment in the reverse direction) using
        * different numbers of messages: 1024 1024Byte messages, vs 2048 512Byte messages, vs 4096 X 256Byte messages.
        *
        * */

        TimeUnit.SECONDS.sleep(9);

        System.out.println("TESTING TIME TO SEND VARIOUS MESSAGE SIZES TOTALLING 1MB");

        double total_1024_1024 = 0;

        for(int i = 0; i < 1024; i++) {
            sendAndReceiveTimed(msg_1B, host, port, true);
            total_1024_1024 += sendAndReceiveTimed(msg_1B, host, port, true);
        }

        double total_2048_512 = 0;

        for(int i = 0; i < 2048; i++){
            total_2048_512 += sendAndReceiveTimed(msg_512B, host, port, true);
        }

        double total_4096_256 = 0;

        for(int i = 0; i < 4096; i ++){
            total_4096_256 += sendAndReceiveTimed(msg_256B, host, port, true);
        }

        System.out.println("\n-----RESULTS------\n");
        System.out.println("1024 1024B messages: " + total_1024_1024 * 1000000000 + " seconds");
        System.out.println("2048 512B messages: " + total_2048_512 * 1000000000 + " seconds");
        System.out.println("4096 256B messages: " + total_4096_256 * 1000000000 + " seconds");


    }

    static void sendAndReceive(byte[] msg, Socket socket) throws IOException {

        byte[] rcv = new byte[msg.length];

        //write
        long now = System.nanoTime();
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

        //System.out.println("Sending " + msg.length);
        dout.write(msg, 0, msg.length);

        //read
        DataInputStream in = new DataInputStream(socket.getInputStream());
        in.readFully(rcv);

        dout.close();
        in.close();
    }

    static double sendAndReceiveTimed(byte[] msg, String host, int port, boolean display) throws IOException {

        long now;
        double duration;

        Socket socket = new Socket(host, port);
        socket.setSoTimeout(10000);
        //System.out.println("Successfully connected to server");

        now = System.nanoTime();
        sendAndReceive(msg, socket);
        duration = System.nanoTime() - now;

        socket.close();

        if (display) {
            System.out.println(duration / 1000000 + " ms");
        }


        return duration;
    }


    static double throughputMbps(int size, double time) {
        return size * 8 / (time / 1000);
    }
}