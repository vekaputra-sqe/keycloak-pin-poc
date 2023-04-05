package com.sqe.keycloak;

import static org.keycloak.common.util.ServerCookie.SameSiteAttributeValue.NONE;

import java.net.URI;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.ServerCookie;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

public class PINAuthAuthenticator implements Authenticator {

	public static final String CREDENTIAL_TYPE = "pin_auth";


	@Override
	public void authenticate(AuthenticationFlowContext context) {
		Response challenge = context.form().createForm("pin-auth.ftl");
		context.challenge(challenge);
	}

	@Override
	public void action(AuthenticationFlowContext context) {
		MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
		if (formData.containsKey("cancel")) {
			context.cancelLogin();
			return;
		}
		boolean validated = validateAnswer(context);
		if (!validated) {
			Response challenge = context.form().setError("badSecret").createForm("pin-auth.ftl");
			context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
			return;
		}
		context.success();
	}

	protected boolean validateAnswer(AuthenticationFlowContext context) {
		MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
		String secret = formData.getFirst("pin");
		UserCredentialModel input = new UserCredentialModel("", PINAuthRequiredAction.PIN_AUTH,
				secret);
		return context.getUser().credentialManager().isValid(input);
	}

	@Override
	public boolean requiresUser() {
		return true;
	}

	@Override
	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		return user.credentialManager().isConfiguredFor(PINAuthRequiredAction.PIN_AUTH);
	}

	@Override
	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
		user.addRequiredAction(PINAuthRequiredAction.PROVIDER_ID);
	}

	@Override
	public void close() {

	}
}
