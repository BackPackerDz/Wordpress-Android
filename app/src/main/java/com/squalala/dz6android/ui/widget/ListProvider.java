package com.squalala.dz6android.ui.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squalala.dz6android.App;
import com.squalala.dz6android.R;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.greendao.ArticleDao;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 13/10/15
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private List<Article> articles;
    private Context context;
    private int appWidgetId;

    private RemoteViews remoteView;

    private static final String TAG = ListProvider.class.getSimpleName();


    @Inject
    ArticleDao articleDao;


    @DebugLog
    public ListProvider(Context context, Intent intent) {
        App.get(context).component().inject(this);

        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        articles = articleDao.queryBuilder()
                .list();

        Collections.reverse(articles);
    }



    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @DebugLog
    @Override
    public RemoteViews getViewAt(int position) {
        remoteView = new RemoteViews(
                context.getPackageName(), R.layout.article_item);

        Article article = articles.get(position);

        final Bitmap b;
        try {
            b = Picasso.with(context).load(article.getUrlImage()).get();
            remoteView.setImageViewBitmap(R.id.article_image, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("id", article.getId());
        fillInIntent.putExtra("notification", true);
        fillInIntent.putExtra("all_category", true);
        remoteView.setOnClickFillInIntent(R.id.article_image, fillInIntent);

      /*  Picasso.with(context)
                .load(article.getUrlImage())
                .into(remoteView,
                        R.id.article_image,
                        new int [] {appWidgetId});*/

        remoteView.setTextViewText(R.id.article_text, Html.fromHtml(article.getTitle()));

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
