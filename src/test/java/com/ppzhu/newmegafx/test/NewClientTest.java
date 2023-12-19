package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/19 20:00
 * @Discription
 */

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class NewClientTest {
    private  String ACCESS_KEY="AKAIM75HSZY3PKTEQNLA";
    private  String SECRET_KEY="ix6daoIn9tgTnc8LT0dSmwbAOF5aP7DO5nuXMpcb";

    private static final String SERVICE_ENDPOINT = "https://s3.eu-central-1.s4.mega.io";
    private static final String REGION = "eu-central-1";
    @Test
    public void test(){
        S3Client client = null;
        try {
            client = S3Client.builder()
                    .region(Region.of(REGION))
                    .credentialsProvider(() -> AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY))
                    .endpointOverride(new URI(SERVICE_ENDPOINT))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("aaa")
                .key("test")
                .contentDisposition("this is a test file")
                .build();
        File file = new File("D:\\达尔文.mp3");
        PutObjectResponse putObjectResponse = client.putObject(putObjectRequest, Path.of(file.getAbsolutePath()));

        client.close();

    }
}
