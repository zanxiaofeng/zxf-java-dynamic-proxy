package zxf.java.dynamic.proxy.aop;

import zxf.java.dynamic.proxy.aop.annotation.Logged;
import zxf.java.dynamic.proxy.aop.annotation.Timed;

/**
 * 业务类：方法上【只声明注解，不写任何增强代码】。
 * <p>
 * 增强逻辑全部交给代理（见 {@link AnnotationAwareProxyFactory}）。
 * 对比：如果没有 AOP，每个方法都要手写 try-finally 计时、日志，且和业务逻辑耦合。
 */
public class UserService {

    @Logged
    public String login(String user) {
        return user + " logged in";
    }

    @Timed
    @Logged // 注解可叠加：既计时又打日志
    public String report(String month) {
        return "report of " + month;
    }

    // 无注解 → 不被增强
    public String ping() {
        return "pong";
    }
}
