package handler;

import service.Service;
import sso.*;

public class CredentialsHandler implements Service{

    private final Service service;
    private final AuthenticationGateway gateway;
    private SingleSignOnRegistry registry;

    public CredentialsHandler(
            Service service,
            AuthenticationGateway gateway,
            SingleSignOnRegistry registry
    ) {
        this.service = service;
        this.gateway = gateway;
        this.registry = registry;
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

        return this.service.handleRequest(
                new Request(
                        request.getName(),
                        this.registry.register_new_session(
                                credentials.getUsername(),
                                credentials.getPassword()
        )));
    }
}
