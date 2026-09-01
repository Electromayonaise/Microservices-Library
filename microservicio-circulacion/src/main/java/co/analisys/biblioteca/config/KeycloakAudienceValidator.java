package co.analisys.biblioteca.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * Verifica el claim "azp" (authorized party) del token contra una lista de clientes de
 * Keycloak permitidos para este servicio. El issuer por si solo no distingue quien pidio
 * el token dentro del mismo realm; esto evita que un token emitido para un cliente ajeno
 * (p.ej. usuario-service) sea aceptado por un servicio que no deberia confiar en el.
 */
public class KeycloakAudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final List<String> allowedClients;

    public KeycloakAudienceValidator(List<String> allowedClients) {
        this.allowedClients = allowedClients;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        String azp = jwt.getClaimAsString("azp");
        if (azp != null && allowedClients.contains(azp)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "El token fue emitido por un cliente de Keycloak no autorizado para este servicio (azp=" + azp + ")",
                null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
