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
        if (request.getCredentials() != null) {
            Credentials credentials = request.getCredentials();
            if (!this.gateway.credentialsAreValid(credentials.getUsername(), credentials.getPassword())) {
                return new Response("Invalid credentials provided");
            } else {
                SSOToken token = this.registry.register_new_session(
                        credentials.getUsername(),
                        credentials.getPassword()
                );
                this.service.handleRequest(new Request(request.getName(), token));
            }
        }
        return new Response(null);
    }
}
