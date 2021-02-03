package io.jgoerner.s3.application.port.out.object;

public interface DeleteObject {
    void delete(String bucket, String key);
}
