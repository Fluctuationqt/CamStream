package com.ivo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Receiver {
    private ImageIcon icon;
    private JLabel imgLabel;
    private JFrame window;
    private Socket socket;
    private ServerSocket serverSocket;

    private void initUI(int port) throws IOException {
        System.out.println("Receiving on port " + port);

        // setup image panel and UI
        //icon = new ImageIcon(ImageIO.read(new File("images/waiting.jpg")));
        icon = new ImageIcon();
        imgLabel = new JLabel(icon);
        window = new JFrame("Stream Receiver ");
        window.add(imgLabel);
        window.setSize(640, 480);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private void DrawImage(BufferedImage image){
        icon = new ImageIcon(image);
        imgLabel.setIcon(icon);
        imgLabel.repaint();
        window.repaint();
        System.out.println("Received " + image.getHeight() +
                "x" + image.getWidth()
                + ": " + System.currentTimeMillis());
    }

    public byte[] readMessage(DataInputStream din) throws IOException {
        int msgLen = din.readInt();
        byte[] msg = new byte[msgLen];
        din.readFully(msg);
        return msg;
    }

    @SuppressWarnings({"InfiniteLoopStatement", "ResultOfMethodCallIgnored"})
    void receive(int port) throws IOException {
        initUI(port);

        // Setup server socket and accept first connection
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        DataInputStream in = new DataInputStream(socket.getInputStream());

        while(true) {
            // read image from
            byte[] imageAr = readMessage(in); // TODO: ARRAY SIZE NEGATIVE or OUT OF MEMORY EXCEPTION THROWS HERE
            ByteArrayInputStream bain = new ByteArrayInputStream(imageAr);
            BufferedImage image = ImageIO.read(bain);

            // Show image
            DrawImage(image);
            System.out.println("Received: " + System.currentTimeMillis());

            /* TODO: close the two sockets when done
               System.out.println("Closing: " + System.currentTimeMillis());
               socket.close();
               serverSocket.close()
            */
        }
    }
}