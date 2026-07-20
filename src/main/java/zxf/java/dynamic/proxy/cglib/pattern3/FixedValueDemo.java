package zxf.java.dynamic.proxy.cglib.pattern3;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;

/**
 * pattern3-A —— FixedValue：代理对象的方法【从不调用真实方法】，统一返回一个固定值。
 * <p>
 * 典型场景：mock 桩、缓存命中后的短路返回。
 * <p>
 * 边界（实测）：固定值必须与【每个被拦截方法的声明返回类型】兼容。
 * Object 基础方法同样会被 FixedValue 拦截 —— 这里返回 999（Integer），
 * 对 add/divide（返回 int）兼容，hashCode() 也兼容；
 * 但对代理调 toString()（声明返回 String）会抛 ClassCastException。
 * 所以"任何方法都返回固定值"成立的前提是返回值类型与所有方法兼容。
 */
public final class FixedValueDemo {

    private FixedValueDemo() {
    }

    public static <T> T create(Class<T> targetClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback((FixedValue) () -> 999); // 任何方法都返回 999
        return (T) enhancer.create();
    }
}
