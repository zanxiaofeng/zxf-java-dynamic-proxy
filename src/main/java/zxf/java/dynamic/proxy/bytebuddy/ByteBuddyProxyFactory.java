package zxf.java.dynamic.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * ByteBuddy 动态代理工厂。
 * <p>
 * 与 cglib 同属"子类代理"路线，但是现代工业级方案（Mockito 2+、Hibernate 等框架的默认字节码实现）。
 * <p>
 * 相对 cglib 的优势（在本项目里可直接观察到的）：
 *  1. JDK16+ 强封装下【原生可用，无需 --add-opens】——
 *     cglib 靠反射 ClassLoader.defineClass，被模块系统拦截；ByteBuddy 走 ClassLoadingStrategy，
 *     不需要打开 java.base/java.lang。
 *  2. API 更声明式、链式，能力更强（方法匹配、字段、构造器、Advice 注入等）。
 * <p>
 * 这里用 MethodDelegation + 自定义的 {@link SubclassInterceptor}
 * （功能对应 cglib 的 MethodInterceptor），便于和前面对比。
 * <p>
 * 类加载边界：{@code .load(classLoader)} 默认用 WRAPPER 策略（新建子类加载器加载代理类），
 * 因此只能代理 public 类；包私有类需改用 ClassLoadingStrategy.UsingLookup。
 */
public final class ByteBuddyProxyFactory {

    private ByteBuddyProxyFactory() {
    }

    public static <T> T create(Class<T> targetClass, SubclassInterceptor handler) throws Exception {
        Class<? extends T> proxyClass = new ByteBuddy()
                .subclass(targetClass)
                .method(ElementMatchers.not(ElementMatchers.isFinal())) // final 方法拦截不了，跳过
                .intercept(MethodDelegation.to(new ByteBuddyInterceptorAdapter(handler)))
                .make()
                .load(targetClass.getClassLoader())
                .getLoaded();
        return proxyClass.getDeclaredConstructor().newInstance();
    }
}
