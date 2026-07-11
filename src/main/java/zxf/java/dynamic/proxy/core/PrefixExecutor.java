package zxf.java.dynamic.proxy.core;

/**
 * 一个【没有实现任何接口】、且【带构造器参数】的普通类。
 * <p>
 * 用于 cglib / ByteBuddy 演示：
 * 1. 子类代理无需目标实现接口；
 * 2. 创建代理实例时如何把构造器参数透传给父类（super）。
 */
public class PrefixExecutor {

    private final String prefix;

    public PrefixExecutor() {
        this("");
    }

    public PrefixExecutor(String prefix) {
        this.prefix = prefix;
        System.out.println("PrefixExecutor::ctor(" + prefix + ")");
    }

    public String execute(String taskId) {
        return "Finished of " + prefix + taskId;
    }
}
