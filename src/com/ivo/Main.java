package com.ivo;

import java.util.Scanner;

public class Main {
    private static final String SERVER_ADDRESS = "localhost"; // enter server ip here
    private static final int COMMUNICATIONS_PORT = 5555;      // enter communications port here

    private static void init(){
        System.out.println("Web camera Streaming Application");
        System.out.println("--------------------------------");
        System.out.println("Options:");
        System.out.println("1. Press 1 to transmit.");
        System.out.println("2. Press 2 to receive.");

        Scanner in = new Scanner(System.in);
        int a = in.nextInt();

        Receiver r = new Receiver();
        Transmitter t = new Transmitter();
        try {
            if (a == 1) {
                // transmit video from local machine to given address and given port
                t.transmit(SERVER_ADDRESS, COMMUNICATIONS_PORT);
            } else {
                // listen for video transmission on given port
                r.receive(COMMUNICATIONS_PORT);
            }
        }catch(Exception ex){
            System.out.println("Something broke with: " + ex.toString());
            System.out.println();
            ex.printStackTrace(System.out);
        }
    }

    public static void main(String[] args) {
        init();
    }
}
