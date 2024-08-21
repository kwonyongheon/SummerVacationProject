package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.handler.FileStore;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class FileController {

    private final FileStore fileStore;

    public FileController(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    // 첨부파일 저장
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Resource file = fileStore.loadAsResource(filename);
        String contentDisposition = fileStore.getContentDisposition(file.getFilename());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(file);
    }
}
