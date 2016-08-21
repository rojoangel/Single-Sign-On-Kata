package handler;

import service.Service;
import sso.Request;
import sso.Response;
import sso.SingleSignOnRegistry;

public class SSOTokenHandler implements Service {

    private Service service;
    private SingleSignOnRegistry registry;

    public SSOTokenHandler(Service service, SingleSignOnRegistry registry) {
        this.service = service;
        this.registry = registry;
    }

    @Override
    public Response handleRequest(Request request) {
        if (!registry.is_valid(request.getSSOToken())) {
            return new Response("Invalid Single Sign On Token Provided");
        }
        return service.handleRequest(request);
    }
}