package zxf.java.dynamic.proxy.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import zxf.java.dynamic.proxy.aop.annotation.Logged;
import zxf.java.dynamic.proxy.aop.annotation.Timed;

/**
 * 注解感知的代理工厂（迷你 Spring AOP）。
 * <p>
 * 思路：用一个 cglib MethodInterceptor 统一拦截所有方法，
 * 根据【目标方法上的注解】决定执行哪些增强 —— 这正是 Spring AOP
 * @annotation 切点的最小实现。
 * <p>
 * 增强顺序：先开始计时（若 @Timed）→ 进入日志（若 @Logged）→ 调用真实方法 → 退出日志 → 结束计时。
 */
public final class AnnotationAwareProxyFactory {

    private AnnotationAwareProxyFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> targetClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback((MethodInterceptor) (self, method, args, proxy) -> {
            boolean timed = method.isAnnotationPresent(Timed.class);
            boolean logged = method.isAnnotationPresent(Logged.class);

            long start = timed ? System.nanoTime() : 0L;
            if (logged) {
                System.out.println("[AOP-LOG >>] " + method.getName());
            }
            try {
                return proxy.invokeSuper(self, args);
            } finally {
                if (logged) {
                    System.out.println("[AOP-LOG <<] done " + method.getName());
                }
                if (timed) {
                    System.out.println("[AOP-TIMER] " + method.getName()
                            + " 耗时 " + (System.nanoTime() - start) + " ns");
                }
            }
        });
        return (T) enhancer.create();
    }
}
