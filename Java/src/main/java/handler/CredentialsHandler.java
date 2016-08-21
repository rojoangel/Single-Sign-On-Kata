package handler;

import service.Service;
import sso.AuthenticationGateway;
import sso.Credentials;
import sso.Request;
import sso.Response;

public class CredentialsHandler implements Service{

    private final Service service;
    private final AuthenticationGateway gateway;

    public CredentialsHandler(Service service, AuthenticationGateway gateway) {
        this.service = service;
        this.gateway = gateway;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getCredentials() != null) {
            Credentials credentials = request.getCredentials();
            if (!gateway.credentialsAreValid(credentials.getUsername(), credentials.getPassword())) {
                return new Response("Invalid credentials provided");
            }
        }
        return new Response(null);
    }
}
