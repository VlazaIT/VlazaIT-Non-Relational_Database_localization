package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class MongoDBQueryExample {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("localized");
            MongoCollection<Document> collection = database.getCollection("products");

            // Define the query criteria
            Document query = new Document("price", new Document("$gt", 2.0)); // Query for documents with price greater than 10.0

            // Execute the query
            FindIterable<Document> result = collection.find(query);

            // Iterate over the query result
            System.out.println("Executing query, documents with price greater than 2.0:");
            for (Document document : result) {
                System.out.println(document.toJson() + "\n"); // Output the JSON representation of each document and add a newline
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e);
        }

    }
}

