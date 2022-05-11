package zxf.java.dynamic.proxy.jdk.parttern1;

public class MyExecutor implements IExecutor {
    @Override
    public String execute(String taskId) {
        return "Finished of " + taskId;
    }
}
