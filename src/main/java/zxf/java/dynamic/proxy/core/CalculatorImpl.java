package zxf.java.dynamic.proxy.core;

/**
 * {@link ICalculator} 的真实实现。divide(10,0) 会抛 ArithmeticException。
 */
public class CalculatorImpl implements ICalculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int divide(int a, int b) {
        return a / b;
    }
}
