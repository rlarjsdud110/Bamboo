package com.example.bamboo.controller;

import com.example.bamboo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileConstroller {
    private  final FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file){
        System.out.println(file);
        String url = fileService.upload(file);
        return url;
    }

    @GetMapping(value = "{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Resource getImage(@PathVariable("fileName") String fileName){
        Resource resource = fileService.getImage(fileName);
        return resource;
    }
}
