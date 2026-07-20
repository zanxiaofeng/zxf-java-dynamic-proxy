package zxf.java.dynamic.proxy.comparison;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.CalculatorImpl;
import zxf.java.dynamic.proxy.core.ICalculator;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 性能对比 —— JDK 代理 vs cglib 代理，各调用 N 次的总耗时。
 * <p>
 * 结论（实测 JDK21，仅供参考，每台机器/每次运行不同）：
 *  - 历史上 cglib 的 MethodProxy（FastClass 索引调用）比 JDK 反射 invoke 更快；
 *  - 但现代 JDK 已大幅优化反射，差距明显缩小，【甚至在某些场景反过来】——
 *    例如本 demo 在 JDK21 上常见结果是 JDK 代理反而快于 cglib 3.3.0。
 *  - 所以"cglib 一定更快"是过时认知，选择代理方案应以【能力需求】（是否需代理无接口类）
 *    而非微小的性能差为依据。
 * <p>
 * 注：演示代码用 System.nanoTime 粗测，正式基准请用 JMH。
 */
public class PerformanceComparison {

    private static final int WARMUP = 100_000;
    private static final int ITERATIONS = 5_000_000;

    public static void main(String[] args) throws Throwable {
        ICalculator jdkTarget = new CalculatorImpl();
        ICalculator jdkProxy = (ICalculator) Proxy.newProxyInstance(
                ICalculator.class.getClassLoader(),
                new Class<?>[]{ICalculator.class},
                (p, m, a) -> m.invoke(jdkTarget, a));

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Calculator.class);
        enhancer.setCallback((MethodInterceptor) (p, m, a, mp) -> mp.invokeSuper(p, a));
        Calculator cglibProxy = (Calculator) enhancer.create();

        // 预热，触发 JIT（三组调用方式全部预热，包括反射基线，否则基线测量会混入 JIT 编译开销）
        Method addMethod = ICalculator.class.getMethod("add", int.class, int.class);
        for (int i = 0; i < WARMUP; i++) {
            addMethod.invoke(jdkTarget, 1, 1);
            jdkProxy.add(1, 1);
            cglibProxy.add(1, 1);
        }

        long t1 = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            addMethod.invoke(jdkTarget, 1, 1); // 直接反射基线
        }
        long t2 = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            jdkProxy.add(1, 1);
        }
        long t3 = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            cglibProxy.add(1, 1);
        }
        long t4 = System.nanoTime();

        System.out.printf("反射直接 invoke 基线 : %,d 次 → %,d ms%n", ITERATIONS, (t2 - t1) / 1_000_000);
        System.out.printf("JDK 代理             : %,d 次 → %,d ms%n", ITERATIONS, (t3 - t2) / 1_000_000);
        System.out.printf("cglib 代理           : %,d 次 → %,d ms%n", ITERATIONS, (t4 - t3) / 1_000_000);
    }
}
