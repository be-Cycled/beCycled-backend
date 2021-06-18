package me.becycled.backend.config.security;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.User;
import me.becycled.backend.model.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author I1yi4
 */
@Service
@Primary
@Profile("!test")
public class CustomUserDetailService implements UserDetailsService {

    private final DaoFactory daoFactory;

    @Autowired
    public CustomUserDetailService(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = daoFactory.getUserDao().getByLogin(username);
        if (user == null) {
            throw new BadCredentialsException("Username not found");
        }
        final UserAccount userAccount = daoFactory.getUserAccountDao().getByUserId(user.getId());
        if (userAccount == null) {
            throw new BadCredentialsException("Account not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getLogin(), userAccount.getPassword(), Collections.emptyList());
    }
}
