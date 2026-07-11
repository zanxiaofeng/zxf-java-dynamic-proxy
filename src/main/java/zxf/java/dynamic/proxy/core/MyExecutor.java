package zxf.java.dynamic.proxy.core;

/**
 * IExecutor 的真实实现。
 */
public class MyExecutor implements IExecutor {
    @Override
    public String execute(String taskId) {
        return "Finished of " + taskId;
    }
}
