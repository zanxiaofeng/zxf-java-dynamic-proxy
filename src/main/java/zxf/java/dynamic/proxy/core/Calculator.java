package zxf.java.dynamic.proxy.core;

/**
 * 一个【没有实现任何接口】的普通类。
 * <p>
 * 关键作用：演示 cglib / ByteBuddy 这类"基于子类"的代理，
 * 可以代理【没有接口】的目标，而 JDK 动态代理做不到。
 * <p>
 * 其中 {@link #divide(int, int)} 在 b==0 时抛 ArithmeticException，
 * 用于演示"代理捕获/转换目标方法异常"。
 */
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        return a / b; // b==0 抛 ArithmeticException
    }
}
