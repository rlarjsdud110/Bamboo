package com.example.bamboo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    @Value("${file.path}")
    private String filePath;
    @Value("${file.url}")
    private String fileUrl;

    public String upload(MultipartFile file){
        if(file.isEmpty()){
            return null;
        }

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + extension;
        String savePath = filePath + saveFileName;

        try {
            file.transferTo(new File(savePath));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        String url = fileUrl + saveFileName;
        return url;
    }

    public Resource getImage(String fileName){
        Resource resource = null;

        try {
            resource = new UrlResource("file:"+filePath+fileName);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return resource;
    }
}
