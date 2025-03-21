package org.project.by.gateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.*;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;
import org.springframework.security.web.server.header.ServerHttpHeadersWriter;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http,
                                       ServerOAuth2AuthorizationRequestResolver requestResolver,
                                       ServerLogoutSuccessHandler logoutSuccessHandler,
                                       ServerLogoutHandler logoutHandler) {
        return http.authorizeExchange(exchange ->
                        exchange.pathMatchers("/actuator/**", "/auth/**").permitAll()
                                .anyExchange().authenticated())
                .oauth2Login(oauth2 -> {
                    oauth2.authorizationRequestResolver(requestResolver);
                    oauth2.authorizedClientRepository(authorizedClientRepository());
                })
                .logout(logout ->
                        logout.logoutSuccessHandler(logoutSuccessHandler)
                                .logoutHandler(logoutHandler))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    public ServerOAuth2AuthorizationRequestResolver requestResolver() {
        DefaultServerOAuth2AuthorizationRequestResolver requestResolver =
                new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
        requestResolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
        return requestResolver;
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return oidcLogoutSuccessHandler;
    }

    @Bean
    public ServerLogoutHandler logoutHandler() {
        ServerHttpHeadersWriter clearSiteDataSWriter =
                new ClearSiteDataServerHttpHeadersWriter(ClearSiteDataServerHttpHeadersWriter.Directive.COOKIES);
        return new DelegatingServerLogoutHandler(
                new SecurityContextServerLogoutHandler(),
                new WebSessionServerLogoutHandler(),
                new HeaderWriterServerLogoutHandler(clearSiteDataSWriter)
        );
    }

}
