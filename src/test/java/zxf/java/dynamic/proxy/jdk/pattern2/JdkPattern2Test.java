package zxf.java.dynamic.proxy.jdk.pattern2;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.CalculatorImpl;
import zxf.java.dynamic.proxy.core.ICalculator;
import zxf.java.dynamic.proxy.core.IReader;
import zxf.java.dynamic.proxy.core.MyReader;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** pattern2：异常兜底 + 返回值改写。 */
class JdkPattern2Test {

    private ICalculator shielded() {
        return (ICalculator) Proxy.newProxyInstance(
                ICalculator.class.getClassLoader(),
                new Class<?>[]{ICalculator.class},
                new ExceptionShieldHandler(new CalculatorImpl()));
    }

    private IReader rewritten() {
        return (IReader) Proxy.newProxyInstance(
                IReader.class.getClassLoader(),
                new Class<?>[]{IReader.class},
                new ResultRewriteHandler(new MyReader()));
    }

    @Test
    void divideByZeroIsShieldedToZero() {
        var res = StdoutCapture.captureReturn(() -> shielded().divide(10, 0));
        assertEquals(0, res.value(), "除零应被兜底为 0");
        assertTrue(res.outputContains("[SHIELD]"), "应打印兜底日志");
    }

    @Test
    void normalDivideStillWorks() {
        assertEquals(5, shielded().divide(10, 2));
    }

    @Test
    void returnValueIsRewritten() {
        assertEquals("Content of f [rewritten]", rewritten().read("f"));
    }
}
