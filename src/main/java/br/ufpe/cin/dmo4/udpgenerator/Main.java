/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpe.cin.dmo4.udpgenerator;

import br.ufpe.cin.dmo4.udpgenerator.client.ClientMain;
import br.ufpe.cin.dmo4.udpgenerator.server.ServerMain;

/**
 *
 * @author danilo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
               
        args = "-client -config config.json -port 7999 -threads 100 -address localhost".split(" ");

        if(args.length <= 1){
            System.exit(1);
        }                        
        
        if("-server".equals(args[0])){
            ServerMain.main(new String[]{args[1], args[2]});
        }else{
            String[] newargs = new String[args.length - 1];
            
            for(int i = 0; i < newargs.length; i++){
                newargs[i] = args[i+1];
            }
            
            ClientMain.main(newargs);
        }
    }
    
}
