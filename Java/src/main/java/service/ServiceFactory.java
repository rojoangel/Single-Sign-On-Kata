package service;

import handler.CredentialsToTokenHandler;
import handler.CredentialsValidationHandler;
import handler.SSOTokenHandler;
import myservice.MyService;
import sso.AuthenticationGateway;
import sso.SingleSignOnRegistry;

public class ServiceFactory {

    public static Service handlingSSOTokenAndCredentials(
            AuthenticationGateway authenticationGateway,
            SingleSignOnRegistry singleSignOnRegistry
    ) {
        return new CredentialsValidationHandler(
                new CredentialsToTokenHandler(
                        new SSOTokenHandler(
                                new MyService(),
                                singleSignOnRegistry),
                        singleSignOnRegistry),
                authenticationGateway
        );
    }
}
