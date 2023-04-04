package com.sqe.keycloak;


import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class PINAuthRequiredActionFactory implements RequiredActionFactory {

    private static final PINAuthRequiredAction SINGLETON = new PINAuthRequiredAction();

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return SINGLETON;
    }


    @Override
    public String getId() {
        return PINAuthRequiredAction.PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        return "PIN Auth";
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

}
