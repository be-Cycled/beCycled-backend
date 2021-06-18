/*
 * Copyright © 2018 ООО "Первая Мониторинговая Компания".
 * All rights reserved.
 */

package me.becycled.config;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author I1yi4
 */
@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public DaoFactory daoFactory(final SqlSessionFactory sqlSessionFactory) {
        return new DaoFactory(sqlSessionFactory);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
