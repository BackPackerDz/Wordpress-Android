package com.squalala.dz6android.eventbus;

/**
 * Created by Back Packer
 * Date : 29/09/15
 */
public class QueryEvent {
    String query;

    public QueryEvent(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}