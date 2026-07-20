package zxf.java.dynamic.proxy.jdk.pattern3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 环绕增强：方法耗时统计（相当于 @Around 的计时）。
 * <p>
 * 计时放在 try/finally 里：即使目标方法抛异常，耗时日志也会打印
 * （真实场景的慢调用往往伴随着异常，不能漏）。
 * <p>
 * 同时演示【方法过滤】：代理对象上调 toString()/hashCode() 也会进 invoke，
 * 对这些 Object 基础方法【直接放行】不增强，否则（比如把 proxy 当字符串打印）容易递归。
 * <p>
 * 这正是 AOP 里 pointcut（切点）的雏形：哪些方法增强，哪些不增强。
 */
public class TimingHandler implements InvocationHandler {

    private final Object target;

    public TimingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(target, args); // 基础方法放行
        }
        long start = System.nanoTime();
        try {
            return method.invoke(target, args);
        } finally {
            System.out.println("[TIMER] " + method.getName() + " 耗时 "
                    + (System.nanoTime() - start) + " ns");
        }
    }
}
