package zxf.java.dynamic.proxy.jdk.pattern3;

import zxf.java.dynamic.proxy.core.CalculatorImpl;
import zxf.java.dynamic.proxy.core.ICalculator;
import zxf.java.dynamic.proxy.core.IExecutor;
import zxf.java.dynamic.proxy.core.MyExecutor;
import zxf.java.dynamic.proxy.jdk.JdkProxyFactory;

/**
 * pattern3 —— 环绕计时 + 选择性放行。
 */
public class TestJDKPattern3 {
    public static void main(String[] args) {
        ICalculator calc = JdkProxyFactory.create(
                ICalculator.class, new TimingHandler(new CalculatorImpl()));
        calc.add(2, 3);         // 被计时
        System.out.println(calc); // toString() 放行，不计时

        System.out.println("----");

        // 选择性增强：只拦截 execute 开头的方法
        IExecutor exec = JdkProxyFactory.create(
                IExecutor.class, new SelectiveLogHandler(new MyExecutor()));
        exec.execute("task-A"); // 以 execute 开头 → 打印拦截日志
    }
}
