package zxf.java.dynamic.proxy.comparison;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import zxf.java.dynamic.proxy.core.FinalClass;
import zxf.java.dynamic.proxy.core.WithFinalMethod;

/**
 * final 边界 —— cglib 拦截不到 final 方法，也无法代理 final 类。
 * <p>
 * 运行后能看到：
 *  1. normal()  被拦截（[BOUNDARY-LOG] 打印）
 *  2. secret()  没被拦截（final 方法，cglib 子类无法覆写，直接走原始逻辑）
 *  3. 代理 FinalClass 时抛出 IllegalArgumentException（final 类不可继承）
 */
public class FinalBoundary {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(WithFinalMethod.class);
        enhancer.setCallback((MethodInterceptor) (p, m, a, mp) -> {
            System.out.println("[BOUNDARY-LOG] 拦截 " + m.getName());
            return mp.invokeSuper(p, a);
        });
        WithFinalMethod proxy = (WithFinalMethod) enhancer.create();

        System.out.println("normal() = " + proxy.normal()); // 拦截
        System.out.println("secret() = " + proxy.secret()); // 不拦截（final）

        System.out.println("---- 尝试代理 final 类 ----");
        try {
            Enhancer bad = new Enhancer();
            bad.setSuperclass(FinalClass.class);
            bad.setCallback((MethodInterceptor) (p, m, a, mp) -> mp.invokeSuper(p, a));
            bad.create();
        } catch (IllegalArgumentException e) {
            System.out.println("cglib 代理 final 类失败: " + e.getMessage());
        }
    }
}
