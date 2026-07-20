package zxf.java.dynamic.proxy.jdk.pattern2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 返回值改写：在结果返回给调用方之前，把它改掉。
 * <p>
 * 重要约束：代理对象会【按目标方法的声明返回类型】做拆箱/类型校验，
 * 所以改写后的值必须【兼容声明类型】——例如 int 方法只能返回 Integer
 * （返回 null 会在拆箱时抛 NullPointerException），不能返回 String（会抛 ClassCastException）。
 * 因此本 demo 只改写 String 返回。
 * <p>
 * 真实场景：脱敏（手机号中间 4 位 → ****）、单位换算、结果缓存命中直接返回。
 */
public class ResultRewriteHandler implements InvocationHandler {

    private final Object target;

    public ResultRewriteHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object raw = method.invoke(target, args);
        if (raw instanceof String s) {
            return s + " [rewritten]"; // 仅对 String 返回做兼容改写
        }
        return raw;
    }
}
