package com.ppzhu.newmegafx.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ppzhu.newmegafx.entry.Account;


public class MegaClient {

    private  Account account;

    private  String ACCESS_KEY="";
    private  String SECRET_KEY="";

    public MegaClient(Account account) {
        this.account = account;
        ACCESS_KEY = account.getAccesskey();
        SECRET_KEY = account.getSecretkey();
    }

    private static final String SERVICE_ENDPOINT = "s3.eu-central-1.s4.mega.io";
    private static final String REGION = "eu-central-1";


    public AmazonS3 getClient() {
        String proxyHost = "127.0.0.1";
        int proxyPort = 7890;
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProxyHost(proxyHost);
        clientConfiguration.setProxyPort(proxyPort);

        AmazonS3 AMAZON_S3_CLIENT = AmazonS3ClientBuilder.standard()

                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(SERVICE_ENDPOINT, REGION))

                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .withClientConfiguration(clientConfiguration)

                .build();

        return AMAZON_S3_CLIENT;
    }
}
