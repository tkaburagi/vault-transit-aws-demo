package run.kabuctl.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.vault.support.Ciphertext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;

@Controller
public class UiController {


    private final S3Service s3Service;

    @Value("${cloud.aws.credentials.secretKey}")
    String key;

    public UiController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @RequestMapping("/playground-kabu")
    public String index(){
        return "ui/index";
    }

    @RequestMapping("/row")
    public String index_2(Model model, @RequestParam("bucketName") String bucketName){
        String keyname = "encrypted-image";
        ByteArrayOutputStream downloadInputStream = s3Service.downloadFile(keyname, bucketName);
        Ciphertext ciphertext = Ciphertext.of(downloadInputStream.toString());
        model.addAttribute("ciphertext", ciphertext.getCiphertext());
        return "ui/index-2";
    }

    @RequestMapping("/demo-images-kabu")
    public String index_3(Model model){
        ByteArrayOutputStream downloadInputStream = s3Service.downloadFile("aes256", "demo-images-kabu");
        model.addAttribute("message", downloadInputStream.toString());
        return "ui/index-3";
    }
}
