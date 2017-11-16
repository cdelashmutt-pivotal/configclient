# configclient
A simple, non-Spring example of getting configuration data from a Spring Cloud Config server.

## Details
This project imports some Spring libraries to make it easier to do the OAuth2 exchange needed for Pivotal Spring Cloud Config Server, but that is not a requirement.

Spring Cloud Config server has a very simple, RESTful API that any client application can use to retrieve configuration info. 

### Building
This project uses Maven for managing dependencies and the build.  

#### Linux/MacOS 
```commandline
./mvnw package
```
#### Windows 
```
mvnw package
```

### Config Server 
#### Pivotal Spring Cloud Services in Pivotal CF
If you are using Pivotal Spring Cloud in Pivotal CF, you can create a service instance and a service-key to access the server.

After logging in and targeting the space you want to create your service instance in, execute the following:
##### Windows
```commandline
cf create-service p-config-server standard my-config-server -c '{ \"git\": { \"uri\":\"https://github.com/cdelashmutt-pivotal/config-repo\" } }'
```
##### Linux/MacOS
```commandline
cf create-service p-config-server standard my-config-server -c '{ "git": { "uri":"https://github.com/cdelashmutt-pivotal/config-repo" } }'
```

Next, create a service key and view the contents so you can get the info needed to connect.
```commandline
cf create-service-key my-config-server my-key
cf service-key my-config-server my-key
```

You will be presented with something like the following (with some real values):
```json
Getting key my-key for service instance my-config-server as cdelashmutt@pivotal.io...

{
 "access_token_uri": "https://some-real-host-name-for-uaa/oauth/token",
 "client_id": "p-config-server-something-something",
 "client_secret": "not-a-real-password",
 "uri": "https://some-real-config-server-host-name"
}
```

### Running
#### Note
> If you are using a Pivotal CF instance with an untrusted or self-signed certificate, then you will get an error like the following:
> `error="access_denied", error_description="Error requesting access token."`.
>
> If you see this error, set the CF_TARGET environment variable to the hostname of your Pivotal CF API endpoint.  You can get this by executing `cf target` and using the value listed for the `api endpoint` entry.

After building, you can run the test app using the values from above, and replacing `<app-name>` with the name of the application you want to get configuration for:
```commandline
java -jar target/config-client-1.0-SNAPSHOT-jar-with-dependencies.jar <access_token_uri> <client_id> <client_secret> <uri> <app_name>
```

You should recieve output similar to the following:
```properties
eureka.client.serviceUrl.defaultZone: http://127.0.0.1:8761/eureka/
eureka.instance.leaseRenewalIntervalInSeconds: 10
eureka.instance.metadataMap.instanceId: ${spring.application.name}:8080
greeting: Howdy %s!
management.security.enabled: false
security.basic.enabled: false
```