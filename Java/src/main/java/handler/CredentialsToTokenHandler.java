package handler;

import service.Service;
import sso.*;

public class CredentialsToTokenHandler implements Service{

    private Service service;
    private SingleSignOnRegistry registry;

    public CredentialsToTokenHandler(Service service, SingleSignOnRegistry registry) {
        this.service = service;
        this.registry = registry;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getSSOToken() != null) {
            return this.service.handleRequest(request);
        }
        Credentials credentials = request.getCredentials();
        SSOToken token = this.registry.register_new_session(credentials.getUsername(), credentials.getPassword());
        return this.service.handleRequest(new Request(request.getName(), token));
    }
}
