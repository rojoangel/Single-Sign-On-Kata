package service;

import handler.SSOTokenHandler;
import myservice.MyService;
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
}
