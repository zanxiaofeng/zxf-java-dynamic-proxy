package zxf.java.dynamic.proxy.cglib.pattern1;

import net.sf.cglib.proxy.Enhancer;

/**
 * cglib 动态代理工厂。
 * <p>
 * cglib 通过【生成目标类的子类】来做代理，因此：
 *  - 目标类【不需要实现接口】；
 *  - 目标类不能是 final（无法继承）；
 *  - 目标方法不能是 final / private（无法覆写，也就拦截不到）。
 * <p>
 * 构造器参数：必须【显式传入参数类型 Class[]】，不能用 args[i].getClass() 去猜
 * （例如 int 装箱成 Integer，但目标构造器可能收 int，会 NoSuchMethodError）。
 */
public final class LoggedProxyFactory {

    private LoggedProxyFactory() {
    }

    /** 无参构造：创建一个用无参构造实例化的代理。 */
    public static <T> T create(Class<T> targetClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new LogMethodInterceptor());
        return (T) enhancer.create();
    }

    /** 有参构造：显式传入参数类型 + 参数值，调用目标类对应的构造器。 */
    public static <T> T create(Class<T> targetClass, Class<?>[] paramTypes, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new LogMethodInterceptor());
        return (T) enhancer.create(paramTypes, args);
    }
}
