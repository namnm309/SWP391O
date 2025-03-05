package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    //url
    private static final String UPLOAD_DIR = "src/main/resources/static/img/post_img/";

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        // Kiểm tra và tạo thư mục nếu chưa tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Đặt tên file tránh trùng lặp
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Lưu file vào thư mục
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn tương đối để hiển thị trên frontend
        return "/img/post_img/" + fileName;
    }
}
