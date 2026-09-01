package co.analisys.biblioteca.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * No se anota con @Configuration a proposito: Spring Cloud OpenFeign la registra igual al
 * pasarla en {@code @FeignClient(configuration = ...)}, y sin la anotacion se evita que el
 * @ComponentScan de la app la tome tambien como configuracion global (duplicando el bean).
 */
public class FeignClientConfig {

    @Bean
    public RequestInterceptor bearerTokenForwardingInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                requestTemplate.header("Authorization", "Bearer " + jwtAuth.getToken().getTokenValue());
            }
        };
    }
}
