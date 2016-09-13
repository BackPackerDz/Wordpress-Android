package com.squalala.dz6android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Back Packer
 * Date : 22/08/15
 */
public class Post implements Serializable {

    @SerializedName("ID")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("featured_image")
    private FeaturedImage featuredImage;

    @SerializedName("content")
    private String content;

    @SerializedName("link")
    private String link;

    @SerializedName("date_gmt")
    private String dateGMT;

    @SerializedName("terms")
    private Terms terms;

    @SerializedName("author")
    private Author author;

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getDateGMT() {
        return dateGMT;
    }

    public FeaturedImage getFeaturedImage() {
        return featuredImage;
    }

    public String getLink() {
        return link;
    }

    public Terms getTerms ()
    {
        return terms;
    }

    public String getUrlImage() {

        try {
            return featuredImage.attachment.sizes.medium.url;
        } catch (NullPointerException e) {
            return  "http://image.noelshack.com/fichiers/2015/53/1451300086-image10.png";
        }

    }

    public String getAuthor() {
        return author.username;
    }

    public class Author {
        @SerializedName("username")
        public String username;
    }

    public class Terms
    {
        @SerializedName("category")
        private Category[] category;

        public Category[] getCategory ()
        {
            return category;
        }
    }

    public class Category
    {
        @SerializedName("name")
        private String name;
        @SerializedName("ID")
        private String ID;
        @SerializedName("slug")
        private String slug;

        public String getName ()
        {
            return name;
        }

        public String getID ()
        {
            return ID;
        }

        public String getSlug ()
        {
            return slug;
        }
    }





    public static class Categories {
        @SerializedName("category")
        List<Category> categries;
    }


    private static class FeaturedImage {
        @SerializedName("attachment_meta")
        public Attachment attachment;
        @SerializedName("ID")
        public String ID;
    }

    private static class Attachment {
        @SerializedName("sizes")
        public Sizes sizes;
    }

    private static class Sizes {
        @SerializedName("medium")
        public Medium medium;
    }

    private static class Medium {
        @SerializedName("url")
        public String url;
    }


}
