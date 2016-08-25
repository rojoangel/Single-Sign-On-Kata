package service;

import sso.Response;

public interface Service {
    Response handleRequest(CredentialsRequest request);
}
