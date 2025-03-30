package com.example.lantconsulting.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud_name}") String cloudName,
                             @Value("${cloudinary.api_key}") String apiKey,
                             @Value("${cloudinary.api_secret}") String apiSecret) {
        
        System.out.println("Cloudinary Cloud Name: " + cloudName);
        System.out.println("Cloudinary API key: " + apiKey);
        
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
        try {
            String originalFileName = file.getOriginalFilename();
            String resourceType = "auto";

            // Enlève l'extension pour éviter doublon (.jpg.jpg)
            String fileNameWithoutExt = originalFileName.replaceFirst("[.][^.]+$", "");

            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image")) {
                resourceType = "image";
            } else if ("application/pdf".equals(contentType)) {
                resourceType = "raw";
            }

            return (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", resourceType,
                "public_id", fileNameWithoutExt, // Nom sans extension
                "use_filename", true,
                "unique_filename", false
            ));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Échec de l'upload du fichier vers Cloudinary.");
        }
    }





}
