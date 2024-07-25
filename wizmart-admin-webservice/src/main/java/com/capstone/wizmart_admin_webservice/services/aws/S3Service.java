package com.capstone.wizmart_admin_webservice.services.aws;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.InputStream;

public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(String bucketName) {
        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_1) // Change to your region
                .build();
    }

    /**
     * Uploads an image to the specified S3 bucket.
     *
     * @param key    The key for the object (e.g., "product-images/image.jpg").
     * @param file   The file to upload.
     * @return       True if the upload was successful, otherwise false.
     */
    public boolean uploadImage(String key, File file) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
            System.out.println("Image uploaded successfully: " + key);
            return true;
        } catch (S3Exception e) {
            System.err.println("Error uploading image: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves an image from the specified S3 bucket.
     *
     * @param key    The key for the object (e.g., "product-images/image.jpg").
     * @return       An InputStream of the image, or null if an error occurred.
     */
    public InputStream retrieveImage(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            System.err.println("Error retrieving image: " + e.getMessage());
            return null;
        }
    }
}
