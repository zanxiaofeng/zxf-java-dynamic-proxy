package zxf.java.dynamic.proxy.cglib.pattern3;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

import zxf.java.dynamic.proxy.core.HeavyService;

/**
 * pattern3-B —— LazyLoader：懒加载。
 * <p>
 * 第一次访问代理对象的【任意方法】时，才调用 loadObject() 创建真实对象并缓存；
 * 之后的调用都复用该对象。适合构造昂贵、但不一定用得到的对象（虚拟代理模式）。
 * <p>
 * 与 {@link net.sf.cglib.proxy.Dispatcher} 的区别：Dispatcher 每次调用都重新 loadObject，
 * 不缓存。
 */
public final class LazyLoaderDemo {

    private LazyLoaderDemo() {
    }

    public static <T> T create(Class<T> targetClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback((LazyLoader) () -> {
            System.out.println("[LAZY] loadObject() 首次触发，创建并初始化真实对象");
            try {
                Object obj = targetClass.getDeclaredConstructor().newInstance();
                // 首次加载时执行昂贵初始化（HeavyService.init），之后复用，不再触发
                if (obj instanceof HeavyService hs) {
                    hs.init();
                }
                return obj;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return (T) enhancer.create();
    }
}
