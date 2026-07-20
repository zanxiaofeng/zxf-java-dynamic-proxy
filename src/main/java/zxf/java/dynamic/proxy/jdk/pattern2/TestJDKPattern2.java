package zxf.java.dynamic.proxy.jdk.pattern2;

import zxf.java.dynamic.proxy.core.CalculatorImpl;
import zxf.java.dynamic.proxy.core.ICalculator;
import zxf.java.dynamic.proxy.core.IReader;
import zxf.java.dynamic.proxy.core.MyReader;
import zxf.java.dynamic.proxy.jdk.JdkProxyFactory;

/**
 * pattern2 —— 异常容错 + 返回值改写。
 * <p>
 * 注意：divide(10,0) 在原始实现里会抛异常，但被 ExceptionShieldHandler 兜底为 0，
 * 调用方拿到的是【正常返回】，【没有异常抛出】。
 */
public class TestJDKPattern2 {
    public static void main(String[] args) {
        // 1. 异常容错（目标对象只创建一次，由 handler 持有）
        ICalculator safe = JdkProxyFactory.create(
                ICalculator.class, new ExceptionShieldHandler(new CalculatorImpl()));
        System.out.println("divide(10,2) = " + safe.divide(10, 2));
        System.out.println("divide(10,0) = " + safe.divide(10, 0)); // 不抛异常，返回 0

        System.out.println("----");

        // 2. 返回值改写：把 read 的 String 结果追加标记
        IReader rewritten = JdkProxyFactory.create(
                IReader.class, new ResultRewriteHandler(new MyReader()));
        System.out.println("read 改写后 = " + rewritten.read("myFile")); // "Content of myFile [rewritten]"
    }
}
