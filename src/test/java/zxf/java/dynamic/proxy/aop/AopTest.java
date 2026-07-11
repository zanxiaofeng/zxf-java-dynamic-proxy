package zxf.java.dynamic.proxy.aop;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** AOP 注解驱动增强。 */
class AopTest {

    @Test
    void loggedMethodGetsLogAdvice() {
        UserService svc = AnnotationAwareProxyFactory.create(UserService.class);
        var res = StdoutCapture.captureReturn(() -> svc.login("alice"));
        assertEquals("alice logged in", res.value());
        assertTrue(res.outputContains("[AOP-LOG >>] login"), "@Logged 应织入日志");
        assertFalse(res.outputContains("[AOP-TIMER]"), "login 无 @Timed，不应计时");
    }

    @Test
    void timedAndLoggedMethodGetsBoth() {
        UserService svc = AnnotationAwareProxyFactory.create(UserService.class);
        String out = StdoutCapture.capture(() -> svc.report("2026-07"));
        assertTrue(out.contains("[AOP-LOG >>] report"), "report 同时有 @Logged");
        assertTrue(out.contains("[AOP-TIMER] report"), "report 有 @Timed");
    }

    @Test
    void unAnnotatedMethodIsNotEnhanced() {
        UserService svc = AnnotationAwareProxyFactory.create(UserService.class);
        var res = StdoutCapture.captureReturn(() -> svc.ping());
        assertEquals("pong", res.value());
        assertFalse(res.output().contains("[AOP"), "ping 无注解，不应被增强");
    }
}
