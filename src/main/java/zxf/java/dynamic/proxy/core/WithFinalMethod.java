package zxf.java.dynamic.proxy.core;

/**
 * 含【final 方法】的目标类。
 * <p>
 * cglib 通过生成子类【覆写】方法来拦截，而 final 方法不能被覆写，
 * 所以 {@link #secret()} 不会被拦截 —— 体现 cglib 的一个边界。
 */
public class WithFinalMethod {

    public String normal() {
        return "normal";
    }

    public final String secret() {
        return "secret"; // final：cglib 拦截不到
    }
}
