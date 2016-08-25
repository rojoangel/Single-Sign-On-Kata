package myservice;

import service.CredentialsRequest;
import service.Service;
import sso.Response;

public class MyService implements Service {

    @Override
    public Response handleRequest(CredentialsRequest request) {
        return new Response("hello " + request.getName() + "!");
    }
}
