package zxf.java.dynamic.proxy.jdk.pattern2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 异常容错（@Around 的 try-catch 语义）。
 * <p>
 * 目标方法抛出 {@link ArithmeticException} 时，代理【吞掉异常并返回兜底值 0】，
 * 调用方完全感知不到异常。常用于：降级、重试、熔断、默认值回退。
 * <p>
 * 这演示了代理对【异常】的控制力 —— 这正是 RPC stub / Spring 事务回滚的基础。
 */
public class ExceptionShieldHandler implements InvocationHandler {

    private final Object target;

    public ExceptionShieldHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException ite) {
            // 目标方法抛出的异常会被 InvocationTargetException 包装，真正的异常在 cause 里。
            Throwable real = ite.getCause();
            if (real instanceof ArithmeticException ae) {
                System.out.println("[SHIELD] 捕获 " + ae.getClass().getSimpleName() + ": " + ae.getMessage()
                        + " → 返回兜底值 0");
                return 0; // 兜底
            }
            throw real; // 其它异常原样抛出
        }
    }
}
