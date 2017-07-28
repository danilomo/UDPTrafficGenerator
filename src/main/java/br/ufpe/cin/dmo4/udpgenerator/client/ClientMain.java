package br.ufpe.cin.dmo4.udpgenerator.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * The client main class.
 *
 * @author Danilo Oliveira
 */
public class ClientMain {

    @Option(name = "-threads", usage = "number of threads")
    private int nThreads = 1;

    @Option(name = "-config", usage = "JSON config file")
    private String configFile = "config.json";

    @Option(name = "-port", usage = "server port")
    private int port = 7999;

    @Option(name = "-address", usage = "server address")
    private String serverAddress = "<<address>>";

    /**
     *
     * @param args The command line arguments for the client.
     * @ClientMain class.
     */
    public static void main(String[] args) {

        try {

            ClientMain rc = initProgram("-config config.json -port 7999 -threads 5 -address localhost".split(" "));
//            ClientMain rc = initProgram(args);
            rc.startThreads();

        } catch (Exception ex) {
            System.exit(1);
        }
    }

    private static ClientMain initProgram(String[] args) {
        ClientMain cm = new ClientMain();
        CmdLineParser parser = new CmdLineParser(cm);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println("Usage mode: \n"
                    + "java -jar udpgenerator.jar <args>");
            parser.printUsage(System.err);
            System.exit(1);
        }

        if (cm.invalid()) {
            System.out.println("Usage mode: \n"
                    + "java -jar udpgenerator.jar <args>");
            parser.printUsage(System.err);
            System.exit(1);
        }

        return cm;
    }

    private void startThreads() {

        String config = null;

        try {
            config = readConfigFile(configFile, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);

            System.exit(1);
        }

        startTimer(config);

        for (int i = 0; i < nThreads; i++) {
            UDPGeneratorClient client = new UDPGeneratorClient(port, serverAddress, config);
            client.start();
        }

    }

    private static String readConfigFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private boolean invalid() {
        return nThreads <= 0;
    }

    private void startTimer(String config) {
        try {
            long stopAt = (long) new JSONObject(config).getDouble("duration") * 1000;

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, stopAt);
        } catch (JSONException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Experiment duration not specified");
            System.exit(1);
        }
    }
}
