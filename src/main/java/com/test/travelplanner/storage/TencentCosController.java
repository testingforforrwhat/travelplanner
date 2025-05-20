package com.test.travelplanner.storage;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController  
@RequestMapping("/api/cos")
@Slf4j
public class TencentCosController {  

    private final TencentCosService tencentCosService;

    public TencentCosController(TencentCosService tencentCosService) {
        this.tencentCosService = tencentCosService;
    }

    // 上传文件接口  
    @PostMapping("/upload")  
    public String uploadFile(@RequestParam("file") MultipartFile file) {  
        try {  
            return tencentCosService.uploadFile(file);  
        } catch (IOException e) {  
            e.printStackTrace();  
            return "上传失败：" + e.getMessage();  
        }  
    }  

    // 列出文件接口  
    @GetMapping("/list")  
    public List<String> listFiles() {
        return tencentCosService.listFiles();  
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/{fileName}")
    public void downloadFile(@PathVariable String fileName,
                             HttpServletResponse response) {
        tencentCosService.downloadFile(fileName, response);
    }

    /**
     * 文件删除
     */
    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            tencentCosService.deleteFile("uploads/" + fileName);
            return ResponseEntity.ok("The file was deleted successfully: " + fileName);
        } catch (Exception e) {
            log.error("File deletion failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File deletion failed");
        }
    }

}