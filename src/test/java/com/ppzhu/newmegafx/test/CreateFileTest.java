package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/18 0:08
 * @Discription
 */

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class CreateFileTest {
    @Test
    public void test(){
        File file = new File("D:\\MegaDownload\\small214326eRpO81628603006.jpg");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
