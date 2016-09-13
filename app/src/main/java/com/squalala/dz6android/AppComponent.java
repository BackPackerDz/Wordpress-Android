package com.squalala.dz6android;

import android.app.Application;
import android.content.Context;

import com.squalala.dz6android.common.BaseFragment;
import com.squalala.dz6android.data.api.ApiModule;
import com.squalala.dz6android.data.prefs.Preferences;
import com.squalala.dz6android.greendao.ArticleDao;
import com.squalala.dz6android.service.NotificationService;
import com.squalala.dz6android.ui.activity.ShowPostActivity;
import com.squalala.dz6android.ui.widget.ListProvider;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                ApiModule.class
        }
)
public interface AppComponent {

        void inject(App app);
        void inject(ShowPostActivity activity);
        void inject(BaseFragment fragment);
        void inject(NotificationService service);
        void inject(ListProvider listProvider);

        Application getApplication();

        Context getContext();

        Preferences getPreferences();

        ArticleDao getArticleDao();
}
