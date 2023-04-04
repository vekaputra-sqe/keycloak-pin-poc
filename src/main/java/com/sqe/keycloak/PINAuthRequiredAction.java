package com.sqe.keycloak;

import javax.ws.rs.core.Response;

import org.keycloak.common.util.Time;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.UserCredentialModel;

public class PINAuthRequiredAction implements RequiredActionProvider {
	public static final String PROVIDER_ID = "pin_auth_config";
    public static final String PIN_AUTH = "pin-auth";

	@Override
	public void evaluateTriggers(RequiredActionContext context) {
	}

	@Override
	public void requiredActionChallenge(RequiredActionContext context) {
		Response challenge = context.form().createForm("pin-auth-config.ftl");
		context.challenge(challenge);
	}

	@Override
	public void processAction(RequiredActionContext context) {
		String answer = (context.getHttpRequest().getDecodedFormParameters().getFirst("pin"));
		UserCredentialModel input = new UserCredentialModel("", PIN_AUTH, answer);
		
		CredentialModel secret = new CredentialModel();
		secret.setType(PIN_AUTH);
		secret.setSecretData(input.getChallengeResponse());
		secret.setCreatedDate(Time.currentTimeMillis());

		CredentialModel result = context.getUser().credentialManager().createStoredCredential(secret);
		System.out.println("pin auth ra updateCredential: " + result.getId() + " type: " + result.getType());
		context.success();
	}

	@Override
	public void close() {

	}
}
