package me.becycled.backend.service;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author I1yi4
 */
@Service
public class AccessService {

    private final DaoFactory daoFactory;

    @Autowired
    public AccessService(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public User getCurrentAuthUser() {
        return daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
}
