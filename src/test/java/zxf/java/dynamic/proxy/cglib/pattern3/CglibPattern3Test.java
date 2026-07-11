package zxf.java.dynamic.proxy.cglib.pattern3;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.HeavyService;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** cglib pattern3：FixedValue + LazyLoader。 */
class CglibPattern3Test {

    @Test
    void fixedValueReturnsConstant() {
        Calculator mock = FixedValueDemo.create(Calculator.class);
        assertEquals(999, mock.add(2, 3), "FixedValue 应固定返回 999");
        assertEquals(999, mock.divide(100, 5));
    }

    @Test
    void lazyLoaderInitializesOnlyOnce() {
        HeavyService lazy = LazyLoaderDemo.create(HeavyService.class);
        // 连续调用两次
        String out = StdoutCapture.capture(() -> {
            lazy.work("first");
            lazy.work("second");
        });
        long initCount = out.lines().filter(l -> l.contains("HeavyService::init")).count();
        assertEquals(1, initCount, "LazyLoader 应只初始化一次");
        assertTrue(out.contains("[LAZY]"), "首次访问应触发 loadObject");
    }
}
