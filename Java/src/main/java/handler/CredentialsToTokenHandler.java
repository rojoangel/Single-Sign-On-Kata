package handler;

import service.CredentialsRequest;
import service.Service;
import sso.Credentials;
import sso.Response;
import sso.SSOToken;
import sso.SingleSignOnRegistry;

public class CredentialsToTokenHandler implements Service{

    private Service service;
    private SingleSignOnRegistry registry;

    public CredentialsToTokenHandler(Service service, SingleSignOnRegistry registry) {
        this.service = service;
        this.registry = registry;
    }

    @Override
    public Response handleRequest(CredentialsRequest request) {
        if (request.getSSOToken() != null) {
            return this.service.handleRequest(request);
        }
        Credentials credentials = request.getCredentials();
        SSOToken token = this.registry.register_new_session(credentials.getUsername(), credentials.getPassword());
        return this.service.handleRequest(new CredentialsRequest(request.getName(), token));
    }
}
