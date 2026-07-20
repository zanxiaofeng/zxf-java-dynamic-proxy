package zxf.java.dynamic.proxy.jdk.pattern1;

import zxf.java.dynamic.proxy.jdk.JdkProxyFactory;

/**
 * 加日志的 JDK 动态代理工厂（pattern1 专用入口）。
 * <p>
 * 要点：
 * 1. {@link java.lang.reflect.Proxy#newProxyInstance} 需要 (ClassLoader, Class[] 接口数组, InvocationHandler)。
 * 2. JDK 代理只能基于【接口】，因此泛型约束 {@code <T>} 限定 interfaceType 必须是接口类型，
 *    并由调用方保证 proxied 实现了该接口（运行期 Proxy 也会校验）。
 * 3. 实际组装代理的逻辑收敛在 {@link JdkProxyFactory}（三个 pattern 共用），
 *    这里只负责绑定本 pattern 的 {@link LogInvocationHandler}。
 */
public final class LoggedProxyFactory {

    private LoggedProxyFactory() {
    }

    public static <T> T create(Object proxied, Class<T> interfaceType) {
        return JdkProxyFactory.create(interfaceType, new LogInvocationHandler(proxied));
    }
}
