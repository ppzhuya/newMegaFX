package com.ppzhu.newmegafx.test;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.apache.client.impl.SdkHttpClient;
import com.amazonaws.services.s3.transfer.internal.DownloadS3ObjectCallable;
import com.ppzhu.newmegafx.entry.Account;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.ServiceConfiguration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.identity.spi.IdentityProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
/*
 * @Author ppzhu
 * @Date 2023/12/19 12:51
 * @Discription
 */

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.auth.scheme.S3AuthSchemeProvider;
import software.amazon.awssdk.services.s3.auth.scheme.internal.DefaultS3AuthSchemeProvider;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DownloadDirectoryRequest;

import java.io.*;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

public class CreateClientTest {

    private Account account;

    private  String ACCESS_KEY="AKAIM75HSZY3PKTEQNLA";
    private  String SECRET_KEY="ix6daoIn9tgTnc8LT0dSmwbAOF5aP7DO5nuXMpcb";

    private static final String SERVICE_ENDPOINT = "https://s3.eu-central-1.s4.mega.io";
    private static final String REGION = "eu-central-1";
    private static final ProxyConfiguration PROXY_CONFIG = ProxyConfiguration.builder().addNonProxyHost("localhost:7890").build();
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
        File file = new File("D:\\iptest.txt");
        //s3.eu-central-1.s4.mega.io:443 [s3.eu-central-1.s4.mega.io/31.13.91.33]
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket("aaa")
//                .key(file.getName())
//                .build();
//        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
//                .bucket("aaa")
//                .build();

//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket("aaa")
//                .key("iptest.txt")
//                .build();
//        ResponseInputStream<GetObjectResponse> object = client.getObject(getObjectRequest);
//        byte[] bytes = new byte[1024];
//        try {
//            object.read(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        String s = new String(bytes);
//        System.out.println(s);

        /*
        Download object
         */
        GetObjectRequest getObjectRequest  =GetObjectRequest.builder()
                .bucket("aaa")
                .key("th.png")
                .build();
        ResponseBytes<GetObjectResponse> objectAsBytes = client.getObjectAsBytes(getObjectRequest);
        byte[] bytes = objectAsBytes.asByteArray();
        File localFile = new File("D:\\MegaDownload\\th.png");
        try {
            OutputStream outputStream = new FileOutputStream(localFile);
            outputStream.write(bytes);
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
