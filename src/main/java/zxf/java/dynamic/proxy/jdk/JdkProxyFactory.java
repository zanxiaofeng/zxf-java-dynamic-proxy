package zxf.java.dynamic.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理通用工厂，三个 pattern 共用。
 * <p>
 * 把 (接口类型, InvocationHandler) 组装成代理：
 *  - 泛型 {@code <T>} 把返回类型与接口类型绑定，调用方无需强转；
 *  - 用 {@link Class#cast} 替代 {@code (T)} 强转，编译期无 unchecked 警告。
 * <p>
 * 注意：
 *  - JDK 代理只能基于【接口】，interfaceType 必须是接口（运行期 Proxy 会校验）；
 *  - 目标对象（target）由 handler 自己持有，工厂不感知 ——
 *    所以 handler 构造时要传入 target，如 {@code new LogInvocationHandler(target)}。
 */
public final class JdkProxyFactory {

    private JdkProxyFactory() {
    }

    public static <T> T create(Class<T> interfaceType, InvocationHandler handler) {
        return interfaceType.cast(Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                handler));
    }
}
