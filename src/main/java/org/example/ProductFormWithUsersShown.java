package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ProductFormWithUsersShown extends JFrame {
    private JTextField idField, nameField, priceField, descriptionField;

    public ProductFormWithUsersShown() {
        setTitle("Product Management");
        setSize(400, 300);  // Increased height to accommodate new fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Price:"));
        priceField = new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> addProduct());
        panel.add(addButton);

        JButton readButton = new JButton("Read Product");
        readButton.addActionListener(e -> readProduct());
        panel.add(readButton);

        JButton updateButton = new JButton("Update Product");
        updateButton.addActionListener(e -> updateProduct());
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> deleteProduct());
        panel.add(deleteButton);

        add(panel);
        setVisible(true);
    }

    private void addProduct() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("products");

            Document document = new Document()
                    .append("name", nameField.getText())
                    .append("price", Double.parseDouble(priceField.getText()))
                    .append("description", descriptionField.getText());

            collection.insertOne(document);
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readProduct() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("products");

            Document product = collection.find(Filters.eq("_id", new ObjectId(idField.getText()))).first();
            if (product != null) {
                nameField.setText(product.getString("name"));
                priceField.setText(String.valueOf(product.getDouble("price")));
                descriptionField.setText(product.getString("description"));
            } else {
                JOptionPane.showMessageDialog(this, "No product found with given ID.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("products");

            collection.updateOne(Filters.eq("_id", new ObjectId(idField.getText())),
                    Updates.combine(
                            Updates.set("name", nameField.getText()),
                            Updates.set("price", Double.parseDouble(priceField.getText())),
                            Updates.set("description", descriptionField.getText())
                    ));
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("products");

            collection.deleteOne(Filters.eq("_id", new ObjectId(idField.getText())));
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductFormWithUsersShown::new);
    }
}