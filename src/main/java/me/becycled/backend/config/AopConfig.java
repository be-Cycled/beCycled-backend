package me.becycled.backend.config;

import me.becycled.backend.aop.UserPrivacyAspect;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.service.AccessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author I1yi4
 */
@Configuration
public class AopConfig {

    @Bean
    public UserPrivacyAspect userPrivacyAspect(final DaoFactory daoFactory, final AccessService accessService) {
        return new UserPrivacyAspect(daoFactory, accessService);
    }
}
