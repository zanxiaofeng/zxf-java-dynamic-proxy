package zxf.java.dynamic.proxy.jdk.pattern3;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.CalculatorImpl;
import zxf.java.dynamic.proxy.core.ICalculator;
import zxf.java.dynamic.proxy.core.IExecutor;
import zxf.java.dynamic.proxy.core.MyExecutor;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** pattern3：环绕计时 + 选择性放行。 */
class JdkPattern3Test {

    @Test
    void timingLogsElapsedAndReturnsValue() {
        ICalculator calc = (ICalculator) Proxy.newProxyInstance(
                ICalculator.class.getClassLoader(),
                new Class<?>[]{ICalculator.class},
                new TimingHandler(new CalculatorImpl()));
        String out = StdoutCapture.capture(() -> calc.add(2, 3));
        assertTrue(out.contains("[TIMER]"), "业务方法应被计时");
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void objectMethodsAreNotTimed() {
        ICalculator calc = (ICalculator) Proxy.newProxyInstance(
                ICalculator.class.getClassLoader(),
                new Class<?>[]{ICalculator.class},
                new TimingHandler(new CalculatorImpl()));
        String out = StdoutCapture.capture(() -> System.out.print(calc.toString()));
        assertFalse(out.contains("[TIMER]"), "toString 等基础方法不应被计时");
    }

    @Test
    void selectiveOnlyInterceptsExecute() {
        IExecutor exec = (IExecutor) Proxy.newProxyInstance(
                IExecutor.class.getClassLoader(),
                new Class<?>[]{IExecutor.class},
                new SelectiveLogHandler(new MyExecutor()));
        String out = StdoutCapture.capture(() -> exec.execute("x"));
        assertTrue(out.contains("[SELECTIVE]"), "execute 方法应被选择性拦截");
    }
}
