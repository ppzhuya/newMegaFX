package com.ppzhu.newmegafx.client;/*
 * @Author ppzhu
 * @Date 2023/12/19 17:03
 * @Discription
 */

import com.ppzhu.newmegafx.entry.Account;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.lang.ref.PhantomReference;
import java.net.URI;
import java.net.URISyntaxException;

public class NewMegaClient {
    private Account account;
    private String ACCESS_KEY="";
    private String SECRET_KEY="";

    public NewMegaClient(Account account) {
        this.account = account;
        ACCESS_KEY = account.getAccesskey();
        SECRET_KEY = account.getSecretkey();
    }
    private static final String SERVICE_ENDPOINT = "https://s3.eu-central-1.s4.mega.io";
    private static final String REGION = "eu-central-1";

    public S3Client getClient(){
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

        return client;
    }

}
