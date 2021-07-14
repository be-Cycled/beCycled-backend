package me.becycled.backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author I1yi4
 */
@Configuration
@EnableAuthorizationServer
@Profile("!test")
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

    public static final String RESOURCE_ID = "beCycled";

    private static final String CLIENT_ID = "web";
    private static final String CLIENT_SECRET = "secret";
    private static final String[] AUTHORIZED_GRANT_TYPES = {"password", "authorization_code"};
    private static final String SCOPE = "all";

    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 86_400;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userService;

    public OAuthConfiguration(final AuthenticationManager authenticationManager,
                              final PasswordEncoder passwordEncoder,
                              final UserDetailsService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(CLIENT_ID)
            .secret(passwordEncoder.encode(CLIENT_SECRET))
            .authorizedGrantTypes(AUTHORIZED_GRANT_TYPES)
            .scopes(SCOPE)
            .resourceIds(RESOURCE_ID)
            .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
            .accessTokenConverter(accessTokenConverter())
            .userDetailsService(userService)
            .authenticationManager(authenticationManager);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }
}
