package myservice;

import sso.AuthenticationGateway;
import sso.SingleSignOnRegistry;

class MyServiceFactory {
    public static MyService fromRegistryAndGateway(
            SingleSignOnRegistry singleSignOnRegistry,
            AuthenticationGateway authenticationGateway
    ) {
        return new MyService(singleSignOnRegistry, authenticationGateway);
    }
}
