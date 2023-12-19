package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/18 16:38
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.workflows.Login;
import org.junit.jupiter.api.Test;

import java.io.*;

public class PutObjectTest {
    @Test
    public void test(){
        Login login = new Login();
        Account ppzhu = login.login("ppzhu", "123456");
        MegaClient megaClient = new MegaClient(ppzhu);
        AmazonS3 client = megaClient.getClient();
        File file = new File("C:\\appverifUI.dll");
        try {
            InputStream inputStream = new FileInputStream(file);
            client.putObject("aaa","appverifUI.dll",inputStream,new ObjectMetadata());
            inputStream.close();
            client.shutdown();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
