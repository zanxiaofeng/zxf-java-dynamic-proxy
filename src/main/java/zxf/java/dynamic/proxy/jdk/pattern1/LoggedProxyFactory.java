package zxf.java.dynamic.proxy.jdk.pattern1;

import java.lang.reflect.Proxy;

/**
 * JDK 动态代理工厂。
 * <p>
 * 要点：
 * 1. {@link Proxy#newProxyInstance} 需要 (ClassLoader, Class[] 接口数组, InvocationHandler)。
 * 2. JDK 代理只能基于【接口】，因此泛型约束 {@code <T>} 限定 interfaceType 必须是接口类型，
 *    并由调用方保证 proxied 实现了该接口（运行期 Proxy 也会校验）。
 */
public final class LoggedProxyFactory {

    private LoggedProxyFactory() {
    }

    public static <T> T create(Object proxied, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new LogInvocationHandler(proxied));
    }
}
