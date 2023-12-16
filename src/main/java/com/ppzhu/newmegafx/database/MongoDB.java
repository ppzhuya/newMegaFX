package com.ppzhu.newmegafx.database;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.ppzhu.newmegafx.entry.Account;
import org.bson.Document;

public class MongoDB {
    private Account account;

    public Account login(String username, String password) {
        String uri = "mongodb://localhost:27017";
        ConnectionString connectString = new ConnectionString(uri);
        MongoClient mongoClient = MongoClients.create(connectString);
        MongoDatabase mega = mongoClient.getDatabase("mega");
        MongoCollection<Document> accountDoc = mega.getCollection("account");

        Document document = new Document();
        document.put("username",username);
        document.put("password",password);

        FindIterable<Document> documents = accountDoc.find(document);

        if (documents.first() == null) {
            return null;
        }else {
            Document first = documents.first();
            Gson gson = new Gson();

            account = gson.fromJson(first.toJson(), Account.class);
            return account;
        }

    }
}
