package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Generator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.squalala.android_dz.greendao");

        addArticle(schema);
    //    addNote(schema);
     //   addCustomerOrder(schema);

        new DaoGenerator().generateAll(schema, "../Androiddz/app/src/main/java");
    }

    private static void addArticle(Schema schema) {

        Entity article = schema.addEntity("Article");
        article.addIdProperty();
        article.addStringProperty("title").notNull();
        article.addStringProperty("content").notNull();
        article.addStringProperty("urlImage");
        article.addStringProperty("link");
        article.addStringProperty("author");
        // lu ou non lu
        article.addBooleanProperty("status");
        // Supprimer ou non
        article.addBooleanProperty("deleted");
        article.addBooleanProperty("favoris");
        article.addDateProperty("date");

        Entity category = schema.addEntity("Category");
        category.addIdProperty();
        category.addStringProperty("name");
        category.addStringProperty("slug");

        Entity categoryArticle = schema.addEntity("CategoryArticle");
        categoryArticle.addIdProperty();

        Property categoryId = categoryArticle.addLongProperty("categoryId").notNull().getProperty();
        ToMany categoryToArticle = category.addToMany(categoryArticle, categoryId);
        categoryArticle.addToOne(category, categoryId);
        categoryToArticle.setName("categories");

        Property articleId = categoryArticle.addLongProperty("articleId").notNull().getProperty();
        ToMany articleToCategories = article.addToMany(categoryArticle, articleId);
        categoryArticle.addToOne(article, articleId);
        articleToCategories.setName("articles");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }
}
