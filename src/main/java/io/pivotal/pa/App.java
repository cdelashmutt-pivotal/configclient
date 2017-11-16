package io.pivotal.pa;

import io.pivotal.springcloud.ssl.CloudFoundryCertificateTruster;

import java.net.URI;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //Adds the CloudFoundry cert to the trust store to avoid issues with self signed certs.
        //Not needed if your CF install uses a cert that is already trusted by Java.
        CloudFoundryCertificateTruster.trustCertificates();

        try {
            String accessTokenURI = args[0];
            String clientId = args[1];
            String clientSecret = args[2];
            String serverURI = args[3];
            ConfigServer configServer = new ConfigServer(serverURI, accessTokenURI, clientId, clientSecret);
            System.out.println(configServer.getConfig(args[4]));
        }
        catch(Exception e) {
            System.err.println(e);
        }
        System.exit(0);
    }
}
