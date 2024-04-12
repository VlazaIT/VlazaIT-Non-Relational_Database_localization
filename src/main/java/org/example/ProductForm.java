package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ProductForm extends JFrame {
    private JTextField nameField, priceField, descriptionField;

    public ProductForm() {
        setTitle("Add Product");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        panel.add(addButton);

        add(panel);
        setVisible(true);
    }

    private void addProduct() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("products");

            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String description = descriptionField.getText();

            Document document = new Document()
                    .append("name", name)
                    .append("price", price)
                    .append("description", description);

            collection.insertOne(document);
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductForm();
            }
        });
    }
}
