package run.kabuctl.demo;

import java.io.ByteArrayOutputStream;

public interface S3Service {
    public ByteArrayOutputStream downloadFile(String keyName, String bucketName);
}
