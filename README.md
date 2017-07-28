# UDPTrafficGenerator
A generator of UDP traffic to stress your network.

Traffic is generated from the server to the client. The server will listen to incoming UDP packages from clients. A package sent by the client contains the parameters for traffic generation: sending rate, package size distribution, and sending time, enconding as a JSON string. Each incoming package received by the server will start a new thread for package generation that will last according to the specified sending time. 

The client can start multiple threads according to the command line parameter. The parameters for traffic generation are read from a JSON file, as shown in the example bellow.

### Usage:

To start the server:

java -jar udpgenerator.jar -server <port number>

To start the client:

java -jar udpgenerator.jar -client -config <json configuration file> -port <server port> -threads <number of threads> -address <server address>
