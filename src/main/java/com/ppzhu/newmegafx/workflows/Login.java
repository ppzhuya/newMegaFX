package com.ppzhu.newmegafx.workflows;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ppzhu.newmegafx.entry.Account;
import org.bson.Document;
import com.ppzhu.newmegafx.database.DbConnection;


public class Login {
    private Account account;
    /*
     * 登录
     */
    public  Account login(String username, String password) {
        DbConnection db = DbConnection.instance;
        MongoClient mongoClient = db.getMongoClient();
        MongoDatabase mega = mongoClient.getDatabase("mega");
        MongoCollection<Document> accountCollection = mega.getCollection("account");

        Document document = new Document();
        document.put("username", username);
        document.put("password", password);

        FindIterable<Document> documents = accountCollection.find(document);
        Document first = documents.first();

        if (first !=null){
            String json = first.toJson();

            Gson gson = new Gson();
            account = gson.fromJson(json, Account.class);
        }else {
            return null;
        }

        mongoClient.close();

        return account;
    }

}
