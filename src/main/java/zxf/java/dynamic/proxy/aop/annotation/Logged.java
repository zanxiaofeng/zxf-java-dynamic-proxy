package zxf.java.dynamic.proxy.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明式注解：标注在方法上，表示该方法【需要打印调用日志】。
 * <p>
 * 业务方法只要贴这个注解，代理就会自动加日志 —— 业务代码本身完全不感知。
 * 这就是 Spring AOP @注解切面 的核心思想。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
}
