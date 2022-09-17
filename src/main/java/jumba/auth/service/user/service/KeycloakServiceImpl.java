package jumba.auth.service.user.service;

import feign.Feign;
import feign.codec.Encoder;
import jumba.auth.service.client.KeycloakClient;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.account.UserRepresentation;
import feign.Feign;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KeycloakServiceImpl implements  KeycloakService{

    @Value("${keycloak.auth-server-url}")
    private String KEYCLOAK_URL;

    @Value("${application.services.keycloak.username}")
    private String KEYCLOAK_USER;

    @Value("${application.services.keycloak.password}")
    private String KEYCLOAK_PASS;
    @Override
    public void  createUser(String username) {
        KeycloakClient keycloakClient = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(KeycloakClient.class, KEYCLOAK_URL);

        UserRepresentation userRequest = new UserRepresentation();
        userRequest.setUsername(username);

        Map<String,String > headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "bearer " + generateToken());

        var user = keycloakClient.getUser(username, headers);
        if (user.isEmpty()) {
            //create keycloak user
            try {
                log.info("UserRepresentation::createUser {}  request", userRequest.getUsername());
                keycloakClient.createUser(userRequest, headers);
            } catch (Exception e) {
                log.info("UserRepresentation::createUser {}  failed with reason {}", userRequest.getUsername(), e.getMessage());
            }
        }
        else {
            log.info("User {}  Already exist Skipping Creation", username);
        }
    }

    private String generateToken() {
        log.info("Request to generate token  received");

        KeycloakClient keycloakClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(KeycloakClient.class, KEYCLOAK_URL);

        AccessTokenResponse accessToken = keycloakClient
                .authenticate(KEYCLOAK_USER, KEYCLOAK_PASS, "password", "jumba");
        final String authToken = accessToken.getToken();
        log.info("Keycloak token retrieved successfully");
        log.info("Keycloak token {}", authToken);
        return authToken;
    }
}
