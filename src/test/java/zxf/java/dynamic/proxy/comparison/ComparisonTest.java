package zxf.java.dynamic.proxy.comparison;

import org.junit.jupiter.api.Test;
import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.WithFinalMethod;
import zxf.java.dynamic.proxy.test.StdoutCapture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** comparison：final 边界 + cglib 代理是子类。 */
class ComparisonTest {

    @Test
    void finalMethodIsNotIntercepted() {
        // 复刻 FinalBoundary 的核心断言：normal 拦截，secret 不拦截
        WithFinalMethod proxy = zxf.java.dynamic.proxy.cglib.pattern1.LoggedProxyFactory.create(WithFinalMethod.class);
        String normalOut = StdoutCapture.capture(() -> proxy.normal());
        String secretOut = StdoutCapture.capture(() -> proxy.secret());
        assertTrue(normalOut.contains("[LOG >>] normal"), "normal 应被拦截");
        assertFalse(secretOut.contains("[LOG >>] secret"), "final 方法 secret 不应被拦截");
    }

    @Test
    void finalClassCannotBeSubclassed() {
        // 直接用 Enhancer 验证 final 类报错
        net.sf.cglib.proxy.Enhancer enhancer = new net.sf.cglib.proxy.Enhancer();
        enhancer.setSuperclass(zxf.java.dynamic.proxy.core.FinalClass.class);
        enhancer.setCallback((net.sf.cglib.proxy.MethodInterceptor) (p, m, a, mp) -> mp.invokeSuper(p, a));
        assertThrows(IllegalArgumentException.class, enhancer::create,
                "final 类不可被 cglib 子类化");
    }

    @Test
    void cglibProxyIsSubclass() {
        Calculator proxy = zxf.java.dynamic.proxy.cglib.pattern1.LoggedProxyFactory.create(Calculator.class);
        assertTrue(proxy instanceof Calculator, "cglib 代理应是目标类的子类实例");
        assertTrue(proxy.getClass().getName().contains("$$EnhancerByCGLIB$$"),
                "代理类名应含 EnhancerByCGLIB 标识");
    }
}
