package zxf.java.dynamic.proxy.cglib.pattern2;

import zxf.java.dynamic.proxy.core.Calculator;

/**
 * pattern2 —— 运行 CallbackFilter。
 * <p>
 * 期望：
 *   add(2,3)   → 不打印任何日志（走 NoOp），直接返回 5
 *   divide(..) → 打印 [FILTER-LOG] 拦截 divide
 */
public class TestCGLibPattern2 {
    public static void main(String[] args) {
        Calculator calc = SelectiveCallbackFilter.create(Calculator.class);
        System.out.println("add(2,3) = " + calc.add(2, 3));
        System.out.println("divide(10,2) = " + calc.divide(10, 2));
    }
}
