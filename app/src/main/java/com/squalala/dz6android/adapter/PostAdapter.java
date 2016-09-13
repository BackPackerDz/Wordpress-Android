package com.squalala.dz6android.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squalala.dz6android.R;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.holder.PostViewHolder;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 22/08/15
 */
public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private List<Article> posts;


    private static final String TAG = PostAdapter.class.getSimpleName();

    @DebugLog
    public PostAdapter(List<Article> posts) {
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_cards, parent,false);

        return new PostViewHolder(view);
    }

    @DebugLog
    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Article post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}
