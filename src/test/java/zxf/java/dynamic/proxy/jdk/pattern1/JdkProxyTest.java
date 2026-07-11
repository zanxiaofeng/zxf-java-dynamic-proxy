package zxf.java.dynamic.proxy.jdk.pattern1;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.IExecutor;
import zxf.java.dynamic.proxy.core.IReader;
import zxf.java.dynamic.proxy.core.MyExecutor;
import zxf.java.dynamic.proxy.core.MyReader;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** pattern1：JDK 代理基础日志。 */
class JdkProxyTest {

    @Test
    void readerIsLoggedAndReturnsRealValue() {
        var res = StdoutCapture.captureReturn(() ->
                LoggedProxyFactory.create(new MyReader(), IReader.class).read("fileA"));
        assertEquals("Content of fileA", res.value());
        assertTrue(res.outputContains("[LOG >>] read(fileA)"), "应有前置日志");
        assertTrue(res.outputContains("[LOG <<] return Content of fileA"));
    }

    @Test
    void executorIsLogged() {
        IExecutor exec = LoggedProxyFactory.create(new MyExecutor(), IExecutor.class);
        String out = StdoutCapture.capture(() -> exec.execute("t1"));
        assertTrue(out.contains("[LOG >>] execute(t1)"));
        assertEquals("Finished of t1", exec.execute("t1"));
    }
}
