package com.dkhai.fileupload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.dkhai.fileupload.configuration.AwsS3Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final AwsS3Configuration awsS3Configuration;

    @Autowired
    public AwsS3Service(AmazonS3 amazonS3, AwsS3Configuration awsS3Configuration) {
        this.amazonS3 = amazonS3;
        this.awsS3Configuration = awsS3Configuration;

        initializeBucket();
    }

    private void initializeBucket() {

        if (!amazonS3.doesBucketExistV2(awsS3Configuration.getBucketName())) {
            amazonS3.createBucket(awsS3Configuration.getBucketName());
        }
    }

    public Boolean uploadObjectToS3(String fileName, byte[] fileData) {

        log.info("Uploading file '{}' to bucket: '{}' ", fileName, awsS3Configuration.getBucketName());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileData.length);
        PutObjectResult putObjectResult =
                amazonS3.putObject(
                        awsS3Configuration.getBucketName(), fileName, byteArrayInputStream, objectMetadata);

        return Objects.nonNull(putObjectResult);


    }

    public S3ObjectInputStream downloadFileFromS3Bucket(final String fileName) {

        log.info("Downloading file '{}' from bucket: '{}' ", fileName, awsS3Configuration.getBucketName());

        final S3Object s3Object = amazonS3.getObject(awsS3Configuration.getBucketName(), fileName);

        return s3Object.getObjectContent();
    }

    public List<S3ObjectSummary> listObjects() {

        log.info("Retrieving object summaries for bucket '{}'", awsS3Configuration.getBucketName());

        ObjectListing objectListing = amazonS3.listObjects(awsS3Configuration.getBucketName());

        return objectListing.getObjectSummaries();
    }

}
