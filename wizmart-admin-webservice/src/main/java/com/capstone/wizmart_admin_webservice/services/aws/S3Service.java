package com.capstone.wizmart_admin_webservice.services.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class S3Service {

    private final AmazonS3 s3Client;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service() {
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    public String uploadFile(File file, String keyName) {
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(Files.probeContentType(Paths.get(file.getPath())));
            request.setMetadata(metadata);
            s3Client.putObject(request);
            return s3Client.getUrl(bucketName, keyName).toString();
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
}
