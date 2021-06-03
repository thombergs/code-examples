package io.jgoerner.s3.application.port.in.object;

public interface RemoveObject {
    void delete(String space, String key);
}
