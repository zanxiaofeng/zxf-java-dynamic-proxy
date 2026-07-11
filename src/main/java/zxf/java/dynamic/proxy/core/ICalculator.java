package zxf.java.dynamic.proxy.core;

/**
 * 计算器接口。
 * 提供给 JDK 动态代理使用（JDK 代理需要接口）。
 * <p>
 * 与无接口的 {@link Calculator} 形成对照：
 * 同一个"计算器"业务，JDK 走接口代理，cglib 走子类代理。
 */
public interface ICalculator {
    int add(int a, int b);

    /** b==0 时实现类会抛 ArithmeticException，用于演示异常增强。 */
    int divide(int a, int b);
}
