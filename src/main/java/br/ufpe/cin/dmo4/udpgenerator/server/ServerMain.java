/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpe.cin.dmo4.udpgenerator.server;

import java.io.IOException;

/**
 *
 * @author danilo
 */
public class ServerMain {

    public static void main(String[] args) {

        args = new String[]{"-port", "7999"};
        
        if (args.length <= 1 || !"-port".equals(args[0])) {
            System.exit(1);
        }

        try {           
            UDPGeneratorServer server = new UDPGeneratorServer(Integer.parseInt(args[1]));
            server.startServer();
        } catch (NumberFormatException ne) {
            System.err.println("Invalid port.");
        } catch (IOException iOException) {
            System.err.println("IO exception: " + iOException.getMessage());
        }
    }
}
