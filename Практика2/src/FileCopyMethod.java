import java.io.IOException;

@FunctionalInterface
public interface FileCopyMethod {
    void copy(String source, String destination) throws IOException;
}
