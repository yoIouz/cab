package org.project.by.passenger.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakResponse(

        @JsonProperty("access_token")
        String access_token,

        @JsonProperty("expires_in")
        Integer expires_in,

        @JsonProperty("refresh_expires_in")
        Integer refresh_expires_in,

        @JsonProperty("refresh_token")
        String refresh_token,

        @JsonProperty("token_type")
        String token_type,

        @JsonProperty("session_state")
        String session_state,

        @JsonProperty("scope")
        String scope

) {
}
