package zxf.java.dynamic.proxy.jdk.pattern3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 选择性增强：只有方法名以 "execute" 开头时才打日志，其它方法【原样转发、不增强】。
 * <p>
 * 对应 AOP 的 pointcut 表达式 —— 只切特定方法。
 */
public class SelectiveLogHandler implements InvocationHandler {

    private final Object target;

    public SelectiveLogHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith("execute")) {
            System.out.println("[SELECTIVE] 拦截业务方法: " + method.getName());
        }
        return method.invoke(target, args); // 无论是否打日志，都照常转发
    }
}
