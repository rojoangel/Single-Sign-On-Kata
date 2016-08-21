package myservice;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import sso.*;

public class MyServiceTest {

    @Test
    public void invalidSSOTokenIsRejected() {
        SSOToken token = new SSOToken();
        Mockery context = new Mockery();
        SingleSignOnRegistry ssoRegistry = context.mock(SingleSignOnRegistry.class);
        AuthenticationGateway authenticationGateway = context.mock(AuthenticationGateway.class);
        context.checking(new Expectations() {{
            ignoring(authenticationGateway);

            oneOf(ssoRegistry).is_valid(token);
            will(returnValue(false));
        }});

        MyService service = new MyService(ssoRegistry, authenticationGateway);
        Response response = service.handleRequest(new Request("Foo", token));

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }
}
