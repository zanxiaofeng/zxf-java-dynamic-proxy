package zxf.java.dynamic.proxy.aop;

/**
 * AOP 实战缩影 —— 注解驱动的声明式增强。
 * <p>
 * 业务类 {@link UserService} 完全不写增强代码，只贴注解；
 * 代理在运行期读注解、织入日志/计时。
 * <p>
 * 期望：
 *   login   —— @Logged            → 只有 [AOP-LOG]
 *   report  —— @Timed + @Logged   → [AOP-LOG] + [AOP-TIMER]
 *   ping    —— 无注解              → 不增强，无任何 [AOP] 输出
 */
public class TestAOP {
    public static void main(String[] args) {
        UserService svc = AnnotationAwareProxyFactory.create(UserService.class);

        System.out.println("==> login:");
        System.out.println(svc.login("alice"));

        System.out.println("\n==> report:");
        System.out.println(svc.report("2026-07"));

        System.out.println("\n==> ping:");
        System.out.println(svc.ping());
    }
}
