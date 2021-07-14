package me.becycled.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @author binakot
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    private static final String OAUTH_2 = "OAuth2";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("me.becycled.backend.controller"))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(List.of(securityScheme()))
            .securityContexts(List.of(securityContext()))
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("beCycled API")
            .description("")
            .version("1.0")
            .license("Copyright © 2021 beCycled")
            .licenseUrl("https://github.com/be-Cycled/beCycled/blob/main/LICENSE")
            .termsOfServiceUrl("")
            .contact(new Contact("Support", "https://becycled.me", "support@becycled.me"))
            .build();
    }

    public SecurityScheme securityScheme() {
        return new OAuthBuilder()
            .name(OAUTH_2)
            .scopes(List.of(scopes()))
            .grantTypes(List.of(new ResourceOwnerPasswordCredentialsGrant("https://api.becycled.me/oauth/token")))
            .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(List.of(new SecurityReference(OAUTH_2, scopes())))
            .forPaths(PathSelectors.any())
            .build();
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
            new AuthorizationScope("all", "")
        };
    }
}
