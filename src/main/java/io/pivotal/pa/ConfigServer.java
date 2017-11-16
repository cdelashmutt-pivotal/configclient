package io.pivotal.pa;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by grog on 11/16/2017.
 */
public class ConfigServer {
	OAuth2RestTemplate restTemplate;
	URI serverURI;

	public ConfigServer(String serverURI, String oauthTokenURI, String clientId, String clientSecret)
	throws URISyntaxException {
		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setAccessTokenUri(oauthTokenURI);
		resourceDetails.setClientId(clientId);
		resourceDetails.setClientSecret(clientSecret);

		restTemplate = new OAuth2RestTemplate(resourceDetails);
		this.serverURI = new URI(serverURI);
	}

	public String getConfig(String appName) {
		return getConfig(appName, "default");
	}

	/**
	 * Gets the configuration as a block of Java properties text.
	 *
	 * @param appName The name of the application to get configuration for.
	 * @param profileName The name of the application's profile to get configuration for.
	 * @return A string containing Java properties formatted text corresponding to the application's configuration.
	 */
	public String getConfig(String appName, String profileName) {
		ResponseEntity<String> response = restTemplate.getForEntity(serverURI.resolve(String.format("/%s-%s.properties", appName, profileName)), String.class);
		if(response.getStatusCode().value() >= 400) {
			throw new RuntimeException(String.format("Error getting config due to request error. %s", response.getStatusCode().toString()));
		}
		return response.getBody();
	}
}
