package org.example;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBExample {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("products");

            Document document = new Document()
                    .append("_id", 2)
                    .append("name", "Sample Product")
                    .append("price", 19.99)
                    .append("description", "This is a sample product description.")
                    .append("description_farsi", "این یک توضیح محصول نمونه است.")
                    .append("description_japanese", "これはサンプル製品の説明です。");

            try {
                collection.insertOne(document);
            } catch (MongoWriteException e) {
                System.err.println("Document with the same _id already exists. Consider updating it instead.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e);
        }
    }
}



