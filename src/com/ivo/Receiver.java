package com.ivo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

class Receiver {
    private ImageIcon icon;
    private JLabel imgLabel = new JLabel();

    private void initUI(int port){
        System.out.println("Receiving on port " + port);

        // setup image panel and UI
        JLabel imgLabel = new JLabel();
        imgLabel.setSize(700,500);
        icon = new ImageIcon();
        JFrame window = new JFrame("Server");
        window.add(imgLabel);
        window.setSize(500, 500);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private void DrawImage(BufferedImage image){
        icon.setImage(image);
        imgLabel.setIcon(icon);
        imgLabel.repaint();

        System.out.println("Received " + image.getHeight() +
                "x" + image.getWidth()
                + ": " + System.currentTimeMillis());
    }

    @SuppressWarnings({"InfiniteLoopStatement", "ResultOfMethodCallIgnored"})
    void receive(int port) throws IOException {
        initUI(port);

        // Setup server socket and accept first connection
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();

        while(true) {
            // read image from
            System.out.println("Reading: " + System.currentTimeMillis());
            byte[] sizeAr = new byte[4];
            inputStream.read(sizeAr); // TODO: MUST I USE BUFFERED READER INSTEAD?
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
            System.out.println("size is:" + size);

            byte[] imageAr = new byte[size]; // TODO: ARRAY SIZE NEGATIVE or OUT OF MEMORY EXCEPTION THROWS HERE
            inputStream.read(imageAr);

            ByteArrayInputStream bain = new ByteArrayInputStream(imageAr);
            BufferedImage image = ImageIO.read(bain);

            // Show image
            DrawImage(image);

            /* TODO: close the two sockets when done
               System.out.println("Closing: " + System.currentTimeMillis());
               socket.close();
               serverSocket.close()
            */
        }
    }
}
