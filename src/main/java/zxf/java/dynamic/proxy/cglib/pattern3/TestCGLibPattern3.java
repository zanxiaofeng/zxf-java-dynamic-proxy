package zxf.java.dynamic.proxy.cglib.pattern3;

import zxf.java.dynamic.proxy.core.Calculator;
import zxf.java.dynamic.proxy.core.HeavyService;

/**
 * pattern3 —— FixedValue + LazyLoader 两种 Callback。
 */
public class TestCGLibPattern3 {
    public static void main(String[] args) {
        // 1. FixedValue：所有方法返回 999，真实方法根本不会执行
        Calculator mock = FixedValueDemo.create(Calculator.class);
        System.out.println("mock.add(2,3) = " + mock.add(2, 3));        // 999
        System.out.println("mock.divide(10,2) = " + mock.divide(10, 2)); // 999

        System.out.println("----");

        // 2. LazyLoader：构造昂贵对象，懒到第一次调用 work() 才初始化
        HeavyService lazy = LazyLoaderDemo.create(HeavyService.class);
        System.out.println("代理已创建，真实对象尚未构造");
        System.out.println(lazy.work("first"));  // 这里才看到 HeavyService::ctor
        System.out.println(lazy.work("second")); // 复用，不再构造
    }
}
