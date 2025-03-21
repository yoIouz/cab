package org.project.by.gateway.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/access-token")
    public String getAccessToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        return Optional.ofNullable(client)
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2Token::getTokenValue)
                .orElse(null);
    }

    @GetMapping("/refresh-token")
    public String getRefreshToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        return Optional.ofNullable(client)
                .map(OAuth2AuthorizedClient::getRefreshToken)
                .map(OAuth2Token::getTokenValue)
                .orElse(null);
    }

}
