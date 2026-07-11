package zxf.java.dynamic.proxy.cglib.pattern2;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

/**
 * pattern2 —— CallbackFilter：给同一个代理对象的不同方法，路由到不同的 Callback。
 * <p>
 * 这是 cglib 相对 JDK InvocationHandler 的一个重要能力升级：
 * JDK 代理只有【一个】InvocationHandler 处理所有方法；
 * cglib 可以挂【多个】Callback，用 CallbackFilter 决定每个方法走哪一个。
 * <p>
 * 演示策略：
 *   - add   → NoOp.INSTANCE（不增强，直接 super 调用，等价于"不拦截"）
 *   - 其它  → 日志拦截器（MethodInterceptor）
 * <p>
 * CallbackFilter.accept(Method) 返回的整数 = callbacks 数组的下标。
 */
public final class SelectiveCallbackFilter {

    private SelectiveCallbackFilter() {
    }

    public static <T> T create(Class<T> targetClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);

        Callback[] callbacks = new Callback[]{
                NoOp.INSTANCE,                       // index 0：放行
                logInterceptor()                     // index 1：日志
        };
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackFilter(new CallbackFilterImpl());

        return (T) enhancer.create();
    }

    private static MethodInterceptor logInterceptor() {
        return (proxied, method, args, proxy) -> {
            System.out.println("[FILTER-LOG] 拦截 " + method.getName());
            return proxy.invokeSuper(proxied, args);
        };
    }

    /** accept() 返回 callbacks 数组下标。 */
    private static class CallbackFilterImpl implements net.sf.cglib.proxy.CallbackFilter {
        @Override
        public int accept(Method method) {
            // add 方法走 index 0（NoOp 放行）；其余走 index 1（日志）
            return "add".equals(method.getName()) ? 0 : 1;
        }
    }
}
