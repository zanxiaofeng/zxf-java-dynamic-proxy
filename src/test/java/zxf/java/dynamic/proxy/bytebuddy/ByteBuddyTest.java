package zxf.java.dynamic.proxy.bytebuddy;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.WithFinalMethod;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** ByteBuddy 子类代理。 */
class ByteBuddyTest {

    private static final SubclassInterceptor LOGGING = (self, method, args, superCall) -> {
        System.out.println("[BB-LOG >>] " + method.getName());
        return superCall.call();
    };

    @Test
    void interceptsNonFinalMethod() throws Exception {
        Calculator calc = ByteBuddyProxyFactory.create(Calculator.class, LOGGING);
        var res = StdoutCapture.captureReturn(() -> calc.add(2, 3));
        assertEquals(5, res.value());
        assertTrue(res.outputContains("[BB-LOG >>] add"));
    }

    @Test
    void skipsFinalMethod() throws Exception {
        WithFinalMethod wf = ByteBuddyProxyFactory.create(WithFinalMethod.class, LOGGING);
        String secretOut = StdoutCapture.capture(() -> wf.secret());
        assertEquals("secret", wf.secret());
        assertFalse(secretOut.contains("[BB-LOG"), "final 方法 secret 不应被拦截");
    }
}
