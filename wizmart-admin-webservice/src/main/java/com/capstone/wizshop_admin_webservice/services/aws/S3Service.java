package com.capstone.wizshop_admin_webservice.services.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capstone.wizshop_admin_webservice.controller.ProductController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(@Value("${aws.accessKeyId}") String accessKey, 
                     @Value("${aws.secretAccessKey}") String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String uploadFile(MultipartFile file) {
    	logger.info("S3Service() - UploadingFile");
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name must not be null");
        }

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
            s3Client.putObject(request);
            return fileName;
        } catch (AmazonServiceException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] downloadFile(String keyName) {
        try {
            return s3Client.getObject(bucketName, keyName).getObjectContent().readAllBytes();
        } catch (AmazonServiceException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String generatePresignedUrl(String fileName, long expirationTimeMillis) {
        try {
            // Set the URL to expire after the specified time
            java.util.Date expiration = new java.util.Date();
            expiration.setTime(expiration.getTime() + expirationTimeMillis);

            // Generate the presigned URL
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                    .withMethod(com.amazonaws.HttpMethod.GET)
                    .withExpiration(expiration);

            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            logger.info("url - {}", url);
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
