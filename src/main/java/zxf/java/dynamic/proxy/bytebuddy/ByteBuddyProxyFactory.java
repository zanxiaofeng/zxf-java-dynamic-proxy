package zxf.java.dynamic.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * ByteBuddy 动态代理工厂。
 * <p>
 * 与 cglib 同属"子类代理"路线，但是现代工业级方案（Mockito、Spring Boot 自 5 起默认用它替代 cglib）。
 * <p>
 * 相对 cglib 的优势（在本项目里可直接观察到的）：
 *  1. JDK16+ 强封装下【原生可用，无需 --add-opens】——
 *     cglib 靠反射 ClassLoader.defineClass，被模块系统拦截；ByteBuddy 走 ClassLoadingStrategy，
 *     不需要打开 java.base/java.lang。
 *  2. API 更声明式、链式，能力更强（方法匹配、字段、构造器、Advice 注入等）。
 * <p>
 * 这里用 InvocationHandlerAdapter 复用 JDK 的 InvocationHandler 形态，便于和前面对比。
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
