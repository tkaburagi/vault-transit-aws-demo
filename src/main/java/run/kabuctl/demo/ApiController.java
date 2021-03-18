package run.kabuctl.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.Ciphertext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
public class ApiController {

    private final S3Service s3Service;
    private final String keyname = "encrypted-image";

    @Value("${cloud.aws.credentials.secretKey}")
    String key;

    public ApiController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/api/image/decrypted")
    public ResponseEntity downloadFile(@RequestParam("bucketName") String bucketName) {
        ByteArrayOutputStream downloadInputStream = null;
        downloadInputStream = s3Service.downloadFile(keyname, bucketName);
        Ciphertext ciphertext = Ciphertext.of(downloadInputStream.toString());
        VaultOperations vaultOps = BeanUtil.getBean(VaultOperations.class);
        byte[] plaintext = vaultOps.opsForTransit().decrypt("aes256", ciphertext).getPlaintext();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + "image.png" + "\"")
                .body(plaintext);
    }

    @GetMapping("/api/image/non-decrypted")
    public ResponseEntity downloadRowFile(@RequestParam("bucketName") String bucketName) {
        ByteArrayOutputStream downloadInputStream = null;
        downloadInputStream = s3Service.downloadFile(keyname, bucketName);
        Ciphertext ciphertext = Ciphertext.of(downloadInputStream.toString());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + "image.png" + "\"")
                .body(ciphertext);
    }
}
