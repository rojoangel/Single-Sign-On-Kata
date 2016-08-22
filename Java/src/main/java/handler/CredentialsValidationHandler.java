package handler;

import service.Service;
import sso.*;

public class CredentialsValidationHandler implements Service{

    private final Service service;
    private final AuthenticationGateway gateway;

    public CredentialsValidationHandler(
            Service service,
            AuthenticationGateway gateway
    ) {
        this.service = service;
        this.gateway = gateway;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getSSOToken() != null) {
            this.service.handleRequest(request);
        }

        if (request.getCredentials() == null) {
            return new Response("No credentials provided, please log in the application");
        }

        Credentials credentials = request.getCredentials();
        if (!this.gateway.credentialsAreValid(credentials.getUsername(), credentials.getPassword())) {
            return new Response("Invalid credentials provided");
        }

        return this.service.handleRequest(request);
    }
}