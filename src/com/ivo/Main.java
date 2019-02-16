package com.ivo;

import java.util.Scanner;

public class Main {

    private static void startupMessage(){
        System.out.println("Web camera Streaming Application");
        System.out.println("--------------------------------");
        System.out.println("Options:");
        System.out.println("1. Press 1 to transmit.");
        System.out.println("2. Press 2 to receive.");
    }

    private static String getAddress(String message, Scanner in){
        System.out.println(message);
        String address = in.nextLine();
        return (address.length() == 0) ? "127.0.0.1" : address;
    }

    private static int getPort(String message, Scanner in){
        System.out.println(message);
        String portLine = in.nextLine();
        return (portLine.length() == 0) ? 5555 : Integer.parseInt(portLine);
    }

    private static void init(){
        startupMessage();

        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        in.nextLine();

        Receiver r = new Receiver();
        Transmitter t = new Transmitter();
        try {
            if (a == 1) {
                // transmit video from local machine to given address and given port
                String m1 = "Receiver IP address (default 127.0.0.1): ";
                String m2 = "Receiver Port(default 5555): ";
                t.transmit(getAddress(m1, in), getPort(m2, in));
            } else {
                // listen for video transmission on given port
                String m = "Listen on port(default 5555): ";
                r.receive(getPort(m, in));
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
