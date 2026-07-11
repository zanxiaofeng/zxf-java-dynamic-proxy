package zxf.java.dynamic.proxy.jdk.pattern1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * JDK 动态代理的核心：InvocationHandler。
 * 代理对象上【任意方法】的调用，都会被转发到这里。
 * <p>
 * 三个参数：
 *   proxy  —— 运行期生成的代理对象本身（$Proxy0）
 *   method —— 被调用的方法
 *   args   —— 实参
 * <p>
 * 这里演示最常见的增强：在调用目标前后打印一行日志（相当于 AOP 的 @Around）。
 */
public class LogInvocationHandler implements InvocationHandler {

    /** 真实的被代理对象（target）。用 final 保证不可变。 */
    private final Object proxied;

    public LogInvocationHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String argsStr = args == null
                ? ""
                : Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
        System.out.println("[LOG >>] " + method.getName() + "(" + argsStr + ")");
        Object result = method.invoke(proxied, args); // 真正转发给目标对象
        System.out.println("[LOG <<] return " + result);
        return result;
    }
}
