package SummerVacationProject.HCI.web.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadHandler {

    public String uploadFile(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String serverFilename = UUID.randomUUID().toString() + fileExtension;
        String serverFilePath = uploadDir + serverFilename;

        File dest = new File(serverFilePath);
        file.transferTo(dest);

        return serverFilename;
    }
}
