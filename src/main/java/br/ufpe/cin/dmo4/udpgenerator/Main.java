package br.ufpe.cin.dmo4.udpgenerator;

import br.ufpe.cin.dmo4.udpgenerator.client.ClientMain;
import br.ufpe.cin.dmo4.udpgenerator.server.ServerMain;

/**
 * The main class of this project.
 *
 * It can call the server's or the client's main class, according to the first
 * command line argument.
 *
 * @author Danilo Oliveira
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        args = "-server -port 7999".split(" ");
        
        //Examples: 
        //java -jar udpgenerator.jar -server -port 7999
        //java -jar udpgenerator.jar -client -config config.json -port 7999 -threads 100 -address localhost
        if (args.length <= 1) {
            System.exit(1);
        }

        if (null == args[0]) {
            System.err.println("Invalid command line arguments.");
        } else switch (args[0]) {
            case "-server":
                ServerMain.main(new String[]{args[1], args[2]});
                break;
            case "-client":
                String[] newargs = new String[args.length - 1];
                for (int i = 0; i < newargs.length; i++) {
                    newargs[i] = args[i + 1];
                }   ClientMain.main(newargs);
                break;
            default:
                System.err.println("Invalid command line arguments.");
                break;
        }
    }

}
