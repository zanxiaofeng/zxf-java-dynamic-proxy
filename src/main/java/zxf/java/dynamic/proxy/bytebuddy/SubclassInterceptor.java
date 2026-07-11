package zxf.java.dynamic.proxy.bytebuddy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * ByteBuddy 子类代理的拦截器接口（功能上类似 cglib 的 MethodInterceptor）。
 * <p>
 * {@code superCall.call()} 等价于 cglib 的 methodProxy.invokeSuper() —— 调用父类原始方法。
 */
@FunctionalInterface
public interface SubclassInterceptor {
    Object intercept(Object self, Method method, Object[] args, Callable<?> superCall) throws Throwable;
}
