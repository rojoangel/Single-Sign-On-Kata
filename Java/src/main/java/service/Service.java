package service;

import sso.Request;
import sso.Response;

public interface Service {
    Response handleRequest(Request request);
}
