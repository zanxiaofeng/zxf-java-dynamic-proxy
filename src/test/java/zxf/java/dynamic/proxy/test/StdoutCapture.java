package zxf.java.dynamic.proxy.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 测试辅助：捕获 System.out 输出，用于断言代理是否真的打印了增强日志。
 */
public final class StdoutCapture {

    private StdoutCapture() {
    }

    /** 运行 action 并捕获其写往 System.out 的全部文本。 */
    public static String capture(Runnable action) {
        return captureReturn(() -> {
            action.run();
            return null;
        }).output();
    }

    /** 运行 action，同时捕获输出与返回值。 */
    public static <T> CaptureResult<T> captureReturn(ThrowingSupplier<T> action) {
        PrintStream old = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            T result = action.get();
            return new CaptureResult<>(result, baos.toString());
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            System.setOut(old); // 无论成功/异常，唯一恢复点
        }
    }

    public record CaptureResult<T>(T value, String output) {
        public boolean outputContains(String token) {
            return output.contains(token);
        }
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }
}
