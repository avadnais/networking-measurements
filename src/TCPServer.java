
/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TCPServer {

    static int TRIALS = 1;

    public static void main(String args[]) throws IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter port: ");

        int port = Integer.parseInt(sc.next());

        /* TESTING LATENCY AND THROUGHPUT */

        for(int i = 0; i < TRIALS; i++) {

            //ack
            receiveAndSend(1, port);

            receiveAndSend(1, port);
            receiveAndSend(64, port);
            receiveAndSend(1024, port);
            receiveAndSend(1024 << 4, port);
            receiveAndSend(1024 << 6, port);
            receiveAndSend(1024 << 8, port);
            receiveAndSend(1024 << 10, port);
        }

        /* TESTING TIME TO SEND VARIOUS MESSAGE SIZES TOTALLING 1MB */
        for(int i = 0; i < 1024; i ++){
            receiveAndSend(1, port);
            receiveAndSend(1024, port);
        }
        for(int i = 0; i < 2048; i ++){
            receiveAndSend(1, port);
            receiveAndSend(512, port);
        }
        for(int i = 0; i < 4096; i ++){
            receiveAndSend(1, port);
            receiveAndSend(256, port);
        }

    }

    static void receiveAndSend(int size, int port) throws IOException {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Socket created");
            //serverSocket.setSoTimeout(0);
            Socket clientSocket = serverSocket.accept();

            DataInputStream din = new DataInputStream(clientSocket.getInputStream());

            byte[] msg = new byte[size];
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());

            din.readFully(msg);
            dout.write(msg);

            System.out.println("Echoed " + msg.length + " bytes");

            serverSocket.close();
            clientSocket.close();
            din.close();
            dout.close();


        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
