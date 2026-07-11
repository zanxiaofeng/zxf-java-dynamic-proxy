package zxf.java.dynamic.proxy.cglib.pattern1;

import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.PrefixExecutor;

/**
 * pattern1 —— cglib 动态代理。
 * <p>
 * 关键点：被代理的 {@link Calculator} / {@link PrefixExecutor}
 * 【都没有实现任何接口】，cglib 照样能代理 —— 这正是它相对 JDK 代理的最大优势。
 */
public class TestCGLibProxy {
    public static void main(String[] args) {
        // 1. 代理无接口的普通类，无参构造
        Calculator calc = LoggedProxyFactory.create(Calculator.class);
        System.out.println("add(2,3) = " + calc.add(2, 3));

        System.out.println("----");

        // 2. 代理带构造器参数的类（显式传参类型 String.class）
        PrefixExecutor exec = LoggedProxyFactory.create(
                PrefixExecutor.class,
                new Class<?>[]{String.class},
                new Object[]{"prefix-"});
        System.out.println(exec.execute("myTask"));
    }
}
