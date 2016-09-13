package com.squalala.dz6android.holder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squalala.dz6android.R;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.ui.activity.ShowPostActivity;
import com.squalala.dz6android.utils.DateUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 24/08/15
 */
public class PostViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

    private ShowPostActivity.PostListener mListener;
    private Article post;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.txt_date)
    TextView txtDate;

    @Bind(R.id.image)
    ImageView image;

    @DebugLog
    public PostViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
    }

    @DebugLog
    public void bind(Article post) {
        this.post = post;

        title.setText(Html.fromHtml(post.getTitle()));

        txtDate.setText(DateUtils.getRelativeTime(post.getDate()));

        Picasso.with(image.getContext()).load(post.getUrlImage())
                .placeholder(R.drawable.image10)
                .fit().centerCrop()
                .into(image);

        // Il est lu
        if (post.getStatus()) {
            title.setTextColor(Color.GRAY);
        }
        else {
            title.setTextColor(Color.BLACK);
        }

    }

    @Override
    public void onClick(View v) {
        mListener = (ShowPostActivity.PostListener) v.getContext();
        mListener.onSelectPost(post);
    }


}
