package zxf.java.dynamic.proxy.bytebuddy;

import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.WithFinalMethod;

/**
 * ByteBuddy 子类代理 demo。
 * <p>
 * 注意：被代理的 {@link Calculator}【没有实现任何接口】，ByteBuddy（和 cglib）照样能代理；
 * 而 JDK 代理做不到。同时它能自动跳过 final 方法（见工厂里的 ElementMatchers.not(isFinal())）。
 */
public class TestByteBuddy {
    public static void main(String[] args) throws Exception {
        Calculator calc = ByteBuddyProxyFactory.create(Calculator.class,
                (self, method, a, superCall) -> {
                    System.out.println("[BB-LOG >>] " + method.getName());
                    Object r = superCall.call();
                    System.out.println("[BB-LOG <<] return " + r);
                    return r;
                });
        System.out.println("add(2,3) = " + calc.add(2, 3));

        System.out.println("---- final 方法被跳过 ----");
        WithFinalMethod wf = ByteBuddyProxyFactory.create(WithFinalMethod.class,
                (self, method, a, superCall) -> {
                    System.out.println("[BB-LOG >>] " + method.getName());
                    return superCall.call();
                });
        System.out.println("normal() = " + wf.normal()); // 拦截
        System.out.println("secret() = " + wf.secret()); // final，不被拦截（无 BB-LOG）
    }
}
