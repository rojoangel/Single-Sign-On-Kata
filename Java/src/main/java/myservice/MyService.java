package myservice;

import service.Service;
import sso.AuthenticationGateway;
import sso.Request;
import sso.Response;
import sso.SingleSignOnRegistry;

public class MyService implements Service {

    private AuthenticationGateway authenticationGateway;
    private SingleSignOnRegistry registry;
    
    public MyService(SingleSignOnRegistry registry) {
        this.registry = registry;
    }

    public MyService(SingleSignOnRegistry ssoRegistry, AuthenticationGateway authenticationGateway) {
        this(ssoRegistry);
        this.authenticationGateway = authenticationGateway;
    }

    @Override
    public Response handleRequest(Request request) {
        if (!registry.is_valid(request.getSSOToken())) {
            return new Response("Invalid Single Sign On Token Provided");
        }
        return new Response("hello " + request.getName() + "!");
    }
}
