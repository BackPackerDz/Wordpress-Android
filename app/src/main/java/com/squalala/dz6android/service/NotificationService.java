package com.squalala.dz6android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.squalala.dz6android.App;
import com.squalala.dz6android.R;
import com.squalala.dz6android.common.AppConstants;
import com.squalala.dz6android.data.api.PostService;
import com.squalala.dz6android.data.prefs.Preferences;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.greendao.ArticleDao;
import com.squalala.dz6android.greendao.Category;
import com.squalala.dz6android.greendao.CategoryArticle;
import com.squalala.dz6android.greendao.CategoryArticleDao;
import com.squalala.dz6android.greendao.CategoryDao;
import com.squalala.dz6android.model.Post;
import com.squalala.dz6android.ui.activity.ShowPostActivity;
import com.squalala.dz6android.utils.DateUtils;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Back Packer
 * Date : 29/09/15
 */
public class NotificationService extends Service {

    private CompositeSubscription _subscriptions = new CompositeSubscription();

    private static final String TAG = NotificationService.class.getSimpleName();

    @Inject
    PostService postService;

    @Inject
    protected ArticleDao articleDao;

    @Inject
    protected CategoryDao categoryDao;

    @Inject
    protected CategoryArticleDao categoryArticleDao;

    @Inject Preferences preferences;

    @DebugLog
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        App.get(getApplicationContext()).component().inject(this);

        _subscriptions.add(postService.getPosts(AppConstants.DEFAULT_PAGE, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Post>>() {

                    @DebugLog
                    @Override
                    public void onCompleted() {
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @DebugLog
                    @Override
                    public void onNext(List<Post> posts) {

                        List<CategoryArticle> e = categoryArticleDao._queryArticle_Articles(17149);

                        for(CategoryArticle a : e)
                            System.out.println("THE CAT : " + a.getCategory().getName());

                        for (int i = 0; i < posts.size(); i++)
                        {
                            Article article = articleDao.load(Long.valueOf(posts.get(i).getId()));
                            // Cela veut dire que c'est un nouvel article
                            if (article == null) {

                                Article articleNew = new Article(
                                        Long.valueOf(posts.get(i).getId()),
                                        posts.get(i).getTitle(),
                                        posts.get(i).getContent(),
                                        posts.get(i).getUrlImage(),
                                        posts.get(i).getLink(),
                                        posts.get(i).getAuthor(),
                                        false,
                                        false,
                                        false,
                                        DateUtils.strToDate(posts.get(i).getDateGMT())
                                );



                                for(Post.Category category : posts.get(i).getTerms().getCategory())
                                {
                                    categoryDao.insertOrReplace(new Category(Long.valueOf(category.getID()),
                                            category.getName(),
                                            category.getSlug()));

                                    categoryArticleDao.insertOrReplace(new CategoryArticle(null, Long.valueOf(category.getID()),
                                            Long.valueOf(posts.get(i).getId())));

                                    System.out.println("Article ID : " + articleNew.getId() +
                                            " Categorie : " + category.getName());
                                }

                                articleDao.insert(articleNew);

                            }

                        }

                        Post firstPost = posts.get(0);

                        Date date = DateUtils.strToDate(firstPost.getDateGMT());

                        if (preferences.getDate() == null) {
                            Log.e(TAG, "Pas de nouveau post !");
                            preferences.setLastDatePost(firstPost.getDateGMT());
                        }
                        else {
                          //  Log.e(TAG, date.after(preferences.getDate()) + " IN ELSE " + preferences.isNotification());
                            // Cela veut dire qu'il y a un nouveau post
                            if (date.after(preferences.getDate()) && preferences.isNotification()) {
                                Log.e(TAG, "Nouveau post !");

                                int icon = R.mipmap.ic_launcher;

                                preferences.setLastDatePost(firstPost.getDateGMT());

                                Intent notificationIntent = null;
                                notificationIntent = new Intent(getApplicationContext(), ShowPostActivity.class);

                                notificationIntent.putExtra("id", Long.valueOf(firstPost.getId()));
                                notificationIntent.putExtra("all_category", true);
                                notificationIntent.putExtra("notification", true);

                                NotificationManager mNotificationManager = (NotificationManager)
                                        getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

                                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 9999,
                                        notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                                NotificationCompat.Builder mBuilder = null;

                                long when = DateUtils.strToDate(firstPost.getDateGMT()).getTime();

                                mBuilder =
                                        new NotificationCompat.Builder(getBaseContext())
                                                .setSmallIcon(R.drawable.ic_stat_000)
                                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                                                .setContentTitle(getString(R.string.app_name))
                                                .setAutoCancel(true)
                                                .setTicker(Html.fromHtml(firstPost.getTitle()))
                                                .setWhen(when)
                                             //   .setStyle(new NotificationCompat.BigTextStyle()
                                               //         .bigText(Html.fromHtml(firstPost.getContent())))
                                                .setContentText(Html.fromHtml(firstPost.getTitle()));

                                if (preferences.isSound()) {
                                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    mBuilder.setSound(uri);
                                }

                                if (preferences.isLedFlash()) {
                                    mBuilder.setLights(Color.BLUE, 1000, 1000);
                                }

                                if (preferences.isVibreur()) {
                                    mBuilder.setVibrate(new long[] { 1000, 1000, 1000});
                                }

                                mBuilder.setContentIntent(contentIntent);

                                Notification notification = mBuilder.build();

                                mNotificationManager.notify(1, notification);

                            }


                        } // end else


                        Log.e(TAG, "end Next");
                    }


                }));




        //TODO do something useful
        return Service.START_NOT_STICKY;
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
