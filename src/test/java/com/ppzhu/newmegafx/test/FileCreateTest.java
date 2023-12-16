package com.ppzhu.newmegafx.test;

import com.ppzhu.newmegafx.MegaApplication;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


public class FileCreateTest {
    @Test
    public void test(){
        File file = new File("src/main/resources/account.json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
