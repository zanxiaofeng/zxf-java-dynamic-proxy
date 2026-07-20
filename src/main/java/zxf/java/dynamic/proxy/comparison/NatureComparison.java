package zxf.java.dynamic.proxy.comparison;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.CalculatorImpl;
import zxf.java.dynamic.proxy.core.ICalculator;

import java.lang.reflect.Proxy;

/**
 * 本质对比 —— JDK 代理 vs cglib 代理。
 * <p>
 * 关键差异：
 * <table>
 *   <tr><th></th><th>JDK 动态代理</th><th>cglib</th></tr>
 *   <tr><td>机制</td><td>实现接口（运行期生成 implements 目标接口的类）</td><td>继承类（生成目标类的子类）</td></tr>
 *   <tr><td>要求</td><td>目标必须实现接口</td><td>目标类可被继承（非 final）</td></tr>
 *   <tr><td>代理类名</td><td>com.sun.proxy.$ProxyN</td><td>xxx$$EnhancerByCGLIB$$xxxx</td></tr>
 *   <tr><td>调用方式</td><td>反射 Method.invoke</td><td>MethodProxy（FastClass 索引调用；历史上更快，
 *       现代 JDK 已大幅优化反射，差距抹平甚至反超，见 PerformanceComparison）</td></tr>
 *   <tr><td>final 方法</td><td>不适用（接口无 final 实现方法）</td><td>拦截不到</td></tr>
 * </table>
 */
public class NatureComparison {

    public static void main(String[] args) {
        // —— JDK 代理：必须有接口，代理对象【是】ICalculator，但【不是】CalculatorImpl ——
        ICalculator jdkTarget = new CalculatorImpl(); // 目标对象只创建一次，由 handler 持有
        ICalculator jdkProxy = (ICalculator) Proxy.newProxyInstance(
                ICalculator.class.getClassLoader(),
                new Class<?>[]{ICalculator.class},
                (proxy, method, a) -> method.invoke(jdkTarget, a));
        System.out.println("JDK 代理类名  : " + jdkProxy.getClass().getName());
        System.out.println("  是 ICalculator? " + (jdkProxy instanceof ICalculator));
        System.out.println("  是 CalculatorImpl? " + (jdkProxy instanceof CalculatorImpl)); // false

        System.out.println("----");

        // —— cglib 代理：无需接口，代理对象【是】Calculator 的子类 ——
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Calculator.class);
        enhancer.setCallback((MethodInterceptor) (p, m, a, mp) -> mp.invokeSuper(p, a));
        Calculator cglibProxy = (Calculator) enhancer.create();
        System.out.println("cglib 代理类名: " + cglibProxy.getClass().getName());
        System.out.println("  是 Calculator? " + (cglibProxy instanceof Calculator)); // true —— 子类
    }
}
