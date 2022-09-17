package jumba.auth.service.client;

import feign.HeaderMap;
import feign.Headers;
import feign.RequestLine;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.account.UserRepresentation;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface KeycloakClient {

    @RequestLine("POST realms/master/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    AccessTokenResponse authenticate(@Param("username") String username, @Param("password") String password,
                                     @Param("grant_type") String grantType, @Param("client_id") String clientId);

    @RequestLine("POST admin/realms/jumba/users")
    void createUser(UserRepresentation userRequest, @HeaderMap Map<String,String> headers);

    @RequestLine("GET admin/realms/jumba/users?username={username}")
    List<UserRepresentation> getUser(@Param("username") String username, @HeaderMap Map<String, String> headers);
}
