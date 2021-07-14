package me.becycled.backend.config.security;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.UserAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

/**
 * @author I1yi4
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@Profile("!test")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailService userDetailsService;
    private final DaoFactory daoFactory;

    public WebSecurityConfig(final CustomUserDetailService userDetailsService,
                             final DaoFactory daoFactory) {
        this.userDetailsService = userDetailsService;
        this.daoFactory = daoFactory;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
            .authenticationEventPublisher(new CustomAuthEventPublisher(daoFactory))
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * @author I1yi4
     */
    public static final class CustomAuthEventPublisher implements AuthenticationEventPublisher {

        private final DaoFactory daoFactory;

        public CustomAuthEventPublisher(final DaoFactory daoFactory) {
            this.daoFactory = daoFactory;
        }

        @Override
        public void publishAuthenticationSuccess(final Authentication authentication) {
            final User authUser = (User) authentication.getPrincipal();
            final me.becycled.backend.model.entity.user.User user = daoFactory.getUserDao().getByLogin(authUser.getUsername());
            final UserAccount userAccount = daoFactory.getUserAccountDao().getByUserId(user.getId());
            userAccount.setLastAuthTime(Instant.now());
            daoFactory.getUserAccountDao().update(userAccount);
        }

        @Override
        public void publishAuthenticationFailure(final AuthenticationException exception, final Authentication authentication) {
            // NOP
        }
    }
}
