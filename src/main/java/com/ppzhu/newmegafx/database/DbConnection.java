package com.ppzhu.newmegafx.database;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public enum DbConnection {
    instance;
    private static MongoClient mongoClient;

    static {
        // 从配置文件中获取属性值
        String uri = "mongodb://localhost:27017";

        // 创建 MongoClient 对象
        mongoClient = MongoClients.create(uri);
    }

    /**
     * 获取 MongoClient 对象
     *
     * @return MongoClient 对象
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }


}
