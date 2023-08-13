package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.FileDTO;
import io.minio.*;
import io.minio.http.Method;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinioService {

    private final Logger log = LoggerFactory.getLogger(MinioService.class);
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String getObject(String filename) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(filename).method(Method.GET).build()
            );
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            return null;
        }
    }

    public String uploadFile(FileDTO request) {
        String uuidName = UUID.randomUUID().toString();
        String fileName = uuidName + "." + FileNameUtils.getExtension(request.getFile().getOriginalFilename());
        try {
            minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(request.getFile().getInputStream(), request.getFile().getSize(), -1)
                    .contentType("image/jpeg")
                    .build()
            );
        } catch (Exception e) {
            log.error("Happened error when upload file: ", e);
        }
        return fileName;
    }
}
