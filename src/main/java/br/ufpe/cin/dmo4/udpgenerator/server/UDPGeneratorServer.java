package br.ufpe.cin.dmo4.udpgenerator.server;

import br.ufpe.cin.dmo4.udpgenerator.util.Constants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The server for the UDP traffic generator. It accepts clients requests,
 * parses the requests (specifying package sending rate and package size
 * distribution), and starts a thread for generating traffic to the client.
 * 
 * @author Danilo Oliveira
 */
public class UDPGeneratorServer{

    protected DatagramSocket socket = null;
    protected boolean running;
    protected byte[] buf = new byte[Constants.OPENING_CONNECTION_MESSAGE_SIZE];

    /**
     * 
     * @param port The UDP port for listening incoming requests
     * @throws IOException 
     */
    public UDPGeneratorServer(int port) throws IOException {
        socket = new DatagramSocket(port);
    }

    /**
     * Starts the server in the same thread where this method is called.
     * 
     */
    public void startServer() {
        running = true;
        while (running) {

            try {
                // Receives a packet from a client
                DatagramPacket packet = new DatagramPacket(buf, buf.length);                
                socket.receive(packet);
                
                //decodes the packet payload (a JSON string) and generates
                //configuration object
                TrafficGeneratorParameters config = TrafficGeneratorParameters.parseJSON(new String(buf).trim());                

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                
                //starts a new thread for sending UDP datagrams to a client
                //according to the configuration specified on the received datagram
                TrafficGeneratorThread conn = new TrafficGeneratorThread(socket, address, port, config);
                conn.start();
            } catch (IOException ex) {
                Logger.getLogger(UDPGeneratorServer.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        socket.close();
    }
   

}
