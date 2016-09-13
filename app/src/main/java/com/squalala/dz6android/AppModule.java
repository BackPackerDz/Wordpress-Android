package com.squalala.dz6android;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.squalala.dz6android.common.AppConstants;
import com.squalala.dz6android.data.prefs.Preferences;
import com.squalala.dz6android.greendao.ArticleDao;
import com.squalala.dz6android.greendao.CategoryArticleDao;
import com.squalala.dz6android.greendao.CategoryDao;
import com.squalala.dz6android.greendao.DaoMaster;
import com.squalala.dz6android.greendao.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private App app;
    private DaoSession daoSession;

    public AppModule(App app) {
        this.app = app;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app, AppConstants.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    @Provides
    @Singleton
    public ArticleDao provideArticleDao() {
        return daoSession.getArticleDao();
    }

    @Provides
    @Singleton
    public CategoryDao provideCategoryDao() {
        return daoSession.getCategoryDao();
    }

    @Provides
    @Singleton
    public CategoryArticleDao provideCategoryArticleDao() {
        return daoSession.getCategoryArticleDao();
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    public Preferences providePreferences() {
        return new Preferences(app.getApplicationContext());
    }

}
