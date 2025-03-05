package com.example.SpringBootTurialVip.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {

    public String saveFile(MultipartFile file) throws IOException;
}
