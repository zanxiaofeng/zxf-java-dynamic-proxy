package zxf.java.dynamic.proxy.cglib.pattern2;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** cglib pattern2：CallbackFilter 按方法路由。 */
class CglibPattern2Test {

    @Test
    void addIsBypassedDivideIsIntercepted() {
        Calculator calc = SelectiveCallbackFilter.create(Calculator.class);
        String addOut = StdoutCapture.capture(() -> calc.add(2, 3));
        String divOut = StdoutCapture.capture(() -> calc.divide(10, 2));

        assertEquals(5, calc.add(2, 3));
        assertFalse(addOut.contains("[FILTER-LOG]"), "add 走 NoOp，不应拦截");
        assertTrue(divOut.contains("[FILTER-LOG] 拦截 divide"), "divide 应被拦截");
    }
}
