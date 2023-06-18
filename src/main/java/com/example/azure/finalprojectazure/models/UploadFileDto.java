package com.example.azure.finalprojectazure.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFileDto {

    String name;
    String fileBase64;
    MultipartFile file;
}
