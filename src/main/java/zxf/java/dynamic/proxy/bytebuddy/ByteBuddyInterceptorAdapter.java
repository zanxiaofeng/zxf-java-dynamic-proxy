package zxf.java.dynamic.proxy.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 把 ByteBuddy 的方法绑定，桥接到通用的 {@link SubclassInterceptor}。
 * <p>
 * 注解含义：
 *   {@link Origin}        Method —— 被调用的方法
 *   {@link This}          —— 代理对象本身（子类实例）
 *   {@link AllArguments}  —— 实参数组
 *   {@link SuperCall}     —— 调用父类原始方法的句柄
 * <p>
 * {@link RuntimeType} 表示按运行时返回类型自适应。
 */
public class ByteBuddyInterceptorAdapter {

    private final SubclassInterceptor delegate;

    public ByteBuddyInterceptorAdapter(SubclassInterceptor delegate) {
        this.delegate = delegate;
    }

    @RuntimeType
    public Object intercept(
            @Origin Method method,
            @This Object self,
            @AllArguments Object[] args,
            @SuperCall Callable<?> superCall) throws Throwable {
        return delegate.intercept(self, method, args, superCall);
    }
}
