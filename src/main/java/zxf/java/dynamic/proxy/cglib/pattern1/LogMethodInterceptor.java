package zxf.java.dynamic.proxy.cglib.pattern1;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * cglib 动态代理的核心：MethodInterceptor。
 * <p>
 * 与 JDK 的 InvocationHandler 对比：
 *  - 多了一个 {@link MethodProxy} 参数，可以用 {@code methodProxy.invokeSuper(...)}
 *    直接调用【父类（目标类）原始方法】，比反射 invoke 更快。这是 cglib 的性能优势之一。
 *  - 因为是基于子类，所以拦截的是对【目标类方法的覆写】。
 */
public class LogMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object proxied, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String argsStr = args == null
                ? ""
                : Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
        System.out.println("[LOG >>] " + method.getName() + "(" + argsStr + ")");
        Object result = methodProxy.invokeSuper(proxied, args); // 调用父类原始方法
        System.out.println("[LOG <<] return " + result);
        return result;
    }
}
