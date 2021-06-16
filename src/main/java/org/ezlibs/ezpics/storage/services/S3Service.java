package org.ezlibs.ezpics.storage.services;

import org.ezlibs.ezpics.storage.providers.S3ClientProvider;
import org.springframework.http.MediaType;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3Service implements StorageService {

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
