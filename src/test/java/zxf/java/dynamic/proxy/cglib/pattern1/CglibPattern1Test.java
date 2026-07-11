package zxf.java.dynamic.proxy.cglib.pattern1;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.PrefixExecutor;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** cglib pattern1：无接口类代理 + 构造器参数。 */
class CglibPattern1Test {

    @Test
    void proxyClassWithoutInterface() {
        Calculator calc = LoggedProxyFactory.create(Calculator.class);
        var res = StdoutCapture.captureReturn(() -> calc.add(2, 3));
        assertEquals(5, res.value());
        assertTrue(res.outputContains("[LOG >>] add(2, 3)"), "应打印日志");
    }

    @Test
    void ctorArgsAreForwarded() {
        PrefixExecutor exec = LoggedProxyFactory.create(
                PrefixExecutor.class,
                new Class<?>[]{String.class},
                new Object[]{"P-"});
        String out = StdoutCapture.capture(() -> exec.execute("task"));
        assertTrue(out.contains("[LOG <<] return Finished of P-task"));
        assertEquals("Finished of P-task", exec.execute("task"));
    }
}
