package myservice;

import service.Service;
import sso.Request;
import sso.Response;

public class MyService implements Service {

    @Override
    public Response handleRequest(Request request) {
        return new Response("hello " + request.getName() + "!");
    }
}
