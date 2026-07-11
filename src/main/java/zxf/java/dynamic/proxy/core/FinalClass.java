package zxf.java.dynamic.proxy.core;

/**
 * 【final 类】，无法被继承。
 * <p>
 * cglib 依赖"生成子类"，遇到 final 类直接 Enhancer 失败。
 * 这是 cglib / ByteBuddy（子类方案）相对 JDK 代理之外的又一限制：
 * final 类两种方案都代理不了（JDK 需要接口，cglib 需要可继承）。
 */
public final class FinalClass {
    public String hello() {
        return "hello";
    }
}
