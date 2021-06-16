package org.ezlibs.ezpics.storage.services;

import org.ezlibs.ezpics.storage.providers.S3ClientProvider;
import org.springframework.http.MediaType;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.util.concurrent.atomic.AtomicBoolean;

public class S3Service implements StorageService {

    public static boolean createBucket(String name) {
        S3Client s3 = S3ClientProvider.getS3Client();
        AtomicBoolean success = new AtomicBoolean(false);
        try {
            S3Waiter waiter = s3.waiter();
            CreateBucketRequest createRequest = CreateBucketRequest.builder().bucket(name).build();
            s3.createBucket(createRequest);
            HeadBucketRequest headRequest = HeadBucketRequest.builder().bucket(name).build();
            WaiterResponse<HeadBucketResponse> waiterResponse = waiter.waitUntilBucketExists(headRequest);
            waiterResponse.matched().response().ifPresent(val -> success.set(true));
        } catch (S3Exception e) {
            e.printStackTrace();
        }
        return success.get();
    }

    public void upload(byte[] bytes, String filename, String bucketName, MediaType mimeType) {
        S3Client s3 = S3ClientProvider.getS3Client();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .contentType(mimeType.getType())
                .build();
        s3.putObject(objectRequest, RequestBody.fromBytes(bytes));
    }

    public byte[] download(String filename, String bucketName) {
        S3Client s3 = S3ClientProvider.getS3Client();
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
        ResponseBytes<GetObjectResponse> s3ObjectResponse = s3.getObject(objectRequest, ResponseTransformer.toBytes());
        return s3ObjectResponse.asByteArray();
    }

}
