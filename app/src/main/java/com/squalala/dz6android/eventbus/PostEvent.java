package com.squalala.dz6android.eventbus;

import com.squalala.dz6android.greendao.Article;

/**
 * Created by Back Packer
 * Date : 04/10/15
 */
public class PostEvent {

    private Article article;

    public PostEvent(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }
}

