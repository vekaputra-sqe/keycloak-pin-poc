package com.sqe.keycloak;

import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

public class PINAuthResourceProvider implements RealmResourceProvider {

    private KeycloakSession session;

    public PINAuthResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @POST
    @Path("validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validatePIN(ValidatePINPayload payload) {
        AuthResult auth = checkAuth();

        CredentialModel result = auth.getUser().credentialManager().getStoredCredentialsStream()
            .filter(cred -> cred.getType().equals(PINAuthRequiredAction.PIN_AUTH))
            .findFirst()
            .orElse(null);

        if (result == null) {
            throw new NotFoundException("pin credentials not found");
        }

        boolean pinValid = result.getSecretData().equals(payload.getPIN());

        return Response.ok(Map.of("success", pinValid)).build();
    }

    @Override
    public void close() {
    }

	private AuthResult checkAuth() {
		AuthResult auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
		if (auth == null) {
			throw new NotAuthorizedException("Bearer");
		} else if (auth.getToken().getIssuedFor() == null || !auth.getToken().getIssuedFor().equals("account")) {
            System.out.println("issued for " + auth.getToken().getIssuedFor());
			throw new ForbiddenException("Token is not properly issued for admin-cli");
		}
		return auth;
	}
}
