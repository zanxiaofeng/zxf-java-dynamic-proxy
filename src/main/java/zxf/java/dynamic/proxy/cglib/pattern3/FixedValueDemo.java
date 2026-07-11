package zxf.java.dynamic.proxy.cglib.pattern3;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;

/**
 * pattern3-A —— FixedValue：代理对象【任何方法】都返回一个固定值，【从不调用真实方法】。
 * <p>
 * 典型场景：mock 桩、缓存命中后的短路返回。
 * 注意：FixedValue 的返回类型要能转成目标方法的返回类型（这里 add 返回 int，999 自动装箱/拆箱）。
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
