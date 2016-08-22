package service;

import handler.CredentialsValidationHandler;
import handler.SSOTokenHandler;
import myservice.MyService;
import sso.AuthenticationGateway;
import sso.SingleSignOnRegistry;

public class ServiceFactory {

    public static Service handlingSSOToken(
            SingleSignOnRegistry singleSignOnRegistry
    ) {
        return new SSOTokenHandler(
                new MyService(),
                singleSignOnRegistry
        );
    }

    public static Service handlingSSOTokenAndCredentials(
            AuthenticationGateway authenticationGateway,
            SingleSignOnRegistry singleSignOnRegistry
    ) {
        return new CredentialsValidationHandler(
                new SSOTokenHandler(
                    new MyService(),
                    singleSignOnRegistry),
                authenticationGateway
        );
    }
}
