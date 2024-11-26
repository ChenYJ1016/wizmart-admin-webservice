package com.capstone.wizshop_admin_webservice.Services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AdminS3Service {

    private static final Logger logger = LoggerFactory.getLogger(AdminS3Service.class);
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public AdminS3Service() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(50000); // 50 seconds
        clientConfig.setRequestTimeout(120000); // 2 minutes

        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .withClientConfiguration(clientConfig)
                .build();
    }

    public String uploadFile(MultipartFile file) {
        logger.info("S3Service() - Uploading File");
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name must not be null");
        }

        try {
            byte[] fileBytes = file.getBytes();
            InputStream inputStream = new ByteArrayInputStream(fileBytes);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileBytes.length);
            metadata.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
            request.getRequestClientOptions().setReadLimit(fileBytes.length + 1); // Set read limit

            s3Client.putObject(request);
            return fileName;
        } catch (AmazonServiceException | IOException e) {
            logger.error("Error uploading file to S3", e);
            return null;
        }
    }
}
