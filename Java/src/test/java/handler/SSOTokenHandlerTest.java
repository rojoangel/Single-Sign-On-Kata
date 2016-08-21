package handler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import service.Service;
import sso.Request;
import sso.Response;
import sso.SSOToken;
import sso.SingleSignOnRegistry;

import static org.junit.Assert.*;

public class SSOTokenHandlerTest {

    @Test
    public void invalidSSOTokenIsRejected() throws Exception {
        SSOToken token = new SSOToken();

        Mockery context = new Mockery();
        Service service = context.mock(Service.class);
        SingleSignOnRegistry registry = context.mock(SingleSignOnRegistry.class);

        context.checking(new Expectations() {{
            never(service);

            oneOf(registry).is_valid(token);
            will(returnValue(false));
        }});

        SSOTokenHandler handler = new SSOTokenHandler(service, registry);
        Response response = handler.handleRequest(new Request("World", token));

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }
}
