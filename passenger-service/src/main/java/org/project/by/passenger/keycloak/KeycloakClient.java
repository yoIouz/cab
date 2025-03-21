package org.project.by.passenger.keycloak;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "keycloak", url = "http://localhost:8080/auth/realms/test/protocol/openid-connect")
public interface KeycloakClient {

    @PostMapping(value = "/token")
    KeycloakResponse refreshToken(String refreshToken);

}
