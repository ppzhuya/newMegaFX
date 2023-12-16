package com.ppzhu.newmegafx.test;

import com.ppzhu.newmegafx.database.MongoDB;
import com.ppzhu.newmegafx.entry.Account;
import org.junit.jupiter.api.Test;

public class MongoDBtest {
    @Test
    public void test() {
        MongoDB mongoDB = new MongoDB();
        Account ppzhu = mongoDB.login("ppzhu", "123456");
        System.out.println(ppzhu.getAccesskey());
        System.out.println(ppzhu.getSecretkey());
    }
}
