package me.becycled.backend.config;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * @author I1yi4
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @Profile("!test")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @Profile("!test")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(final DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Profile("!test")
    public DaoFactory daoFactory(final SqlSessionFactory sqlSessionFactory) {
        return new DaoFactory(sqlSessionFactory);
    }
}
