package com.ivo;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

class Transmitter {
    private Webcam webcam;

    // initializes UI and gets device's default web camera
    private void initUI(String address, int port){
        System.out.println("Starting transmission to " + address + ":" + port);

        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        JFrame window = new JFrame("Client");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    void transmit(String address, int port) throws IOException, InterruptedException {
        initUI(address, port);

        // Setup Socket and get it's output stream
        InetAddress ipAddress = InetAddress.getByName(address);
        Socket socket = new Socket(ipAddress, port);
        OutputStream outputStream = socket.getOutputStream();
        BufferedImage image;

        while(true)
        {
            // Get web cam image
            image = webcam.getImage();

            // Convert image and it's size to byte arrays
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image,"jpg", baos);
            byte[] imageSizeBytes = ByteBuffer.allocate(4).putInt(baos.size()).array();
            byte[] imageBytes = baos.toByteArray();

            // Send image's byte array size(used for reading) and it's data bytes
            outputStream.write(imageSizeBytes);
            outputStream.write(imageBytes);

            // clear the buffers
            // outputStream.flush(); should i?
            baos.flush();
            System.out.println("Flushed: " + System.currentTimeMillis());
            Thread.sleep(100);

            /* TODO: close socket when done
               System.out.println("Closing: " + System.currentTimeMillis());
               socket.close();
            */
        }
    }
}
