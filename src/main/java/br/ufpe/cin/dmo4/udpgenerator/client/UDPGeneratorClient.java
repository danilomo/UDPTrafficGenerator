package br.ufpe.cin.dmo4.udpgenerator.client;

import br.ufpe.cin.dmo4.udpgenerator.util.Constants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  The UDP traffic generator client. 
 * 
 * It sends a request to the server containing information about the sending
 * rate, the packet size distribution, and for how much time it'll listen to 
 * packages. This information is marshaled in a JSON string.
 * 
 * @author Danilo Oliveira
 */
public class UDPGeneratorClient extends Thread {

    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private byte[] buf;
    private String parameters;
    private DatagramPacket receiveP;
    private transient boolean threadActive;

    /**
     * 
     * @param port The server port to send the initial package
     * @param serverAddress The server address
     * @param parameters The sending rate, package size distribution and duration 
     * , passed as a JSON object.
     */
    public UDPGeneratorClient(int port, String serverAddress, String parameters) {
        try {
            this.port = port;
            socket = new DatagramSocket();
            address = InetAddress.getByName(serverAddress);
            this.parameters = parameters;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The run method from java.lang.Thread.
     */
    @Override
    public void run() {
        
        if (! sendInitialPacket() ) {
            return;
        }
        
        threadActive = true;

        while (threadActive) {
            try {
                receiveP = new DatagramPacket(buf, buf.length);                
                socket.receive(receiveP);

//                for debugging purposes
                System.out.println("Received: " + packetToBase64().trim());

            } catch (IOException ex) {
                Logger.getLogger(UDPGeneratorClient.class.getName()).log(Level.SEVERE, null, ex);

                threadActive = false;
            } finally{
               
            }
        } 
        
        close();

    }

    private boolean sendInitialPacket() {
        
        try {
            DatagramPacket initP = getInitialPacket();
            
            initBuffer();
            
            socket.send(initP);
        } catch (IOException ex) {
            Logger.getLogger(UDPGeneratorClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    private void initBuffer() {
        buf = new byte[Constants.MEGABYTE];
    }

    private DatagramPacket getInitialPacket() {
        byte[] buff = parameters.getBytes();
        return new DatagramPacket(buff, buff.length, address, port);
        
        
    }

    public void close() {
        socket.close();
    }

    private String packetToBase64() {
        byte[] data = new byte[receiveP.getLength() - receiveP.getOffset()];
        System.arraycopy(buf, receiveP.getOffset(), data, 0, receiveP.getLength());
        
        byte[] encoded = Base64.getEncoder().encode(data);        
        return new String(encoded);        
    }

}
