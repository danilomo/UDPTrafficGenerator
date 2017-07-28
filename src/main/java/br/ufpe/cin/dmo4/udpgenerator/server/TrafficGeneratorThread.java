package br.ufpe.cin.dmo4.udpgenerator.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Danilo Oliveira
 */
public class TrafficGeneratorThread extends Thread {

    private final DatagramSocket serverSocket;
    private final InetAddress address;
    private final int port;
    private byte[] payload;
    private final Random random;
    private final TrafficGeneratorParameters config;
    private transient boolean isActive;

    public TrafficGeneratorThread(DatagramSocket serverSocket,
            InetAddress address, int port,
            TrafficGeneratorParameters config) {
        this.serverSocket = serverSocket;
        this.address = address;
        this.port = port;
        this.random = new Random();
        this.config = config;
    }

    @Override
    public void run() {

        createTimer();

        isActive = true;

        while (isActive) {
            try {
                generateRandomPayload();

                sendPacket();

                think();
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(TrafficGeneratorThread.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        
        System.out.println("MEU FI MORREU!");
    }

    private void generateRandomPayload() {

        int size = (int) config.getRandomPackageSize();

        if (size < 1) {
            size = 1;
        }

        payload = new byte[size];

        random.nextBytes(payload);
    }

    private void sendPacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(payload, payload.length, address, port);
        serverSocket.send(packet);
    }

    private void think() throws InterruptedException {
        long thinkingTime = (long) (config.getRandomThinkingTime() * 1000);

        Thread.sleep(thinkingTime);
    }

    private void createTimer() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TrafficGeneratorThread.this.isActive = false;
            }
        }, config.getDuration());
    }

}
