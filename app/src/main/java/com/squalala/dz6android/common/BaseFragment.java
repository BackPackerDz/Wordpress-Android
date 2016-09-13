package com.squalala.dz6android.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.squalala.dz6android.App;
import com.squalala.dz6android.data.api.PostService;
import com.squalala.dz6android.data.prefs.Preferences;
import com.squalala.dz6android.greendao.ArticleDao;
import com.squalala.dz6android.greendao.CategoryArticleDao;
import com.squalala.dz6android.greendao.CategoryDao;
import com.squalala.dz6android.utils.ParseUtils;
import com.squalala.dz6android.utils.RxUtils;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Back Packer
 * Date : 23/08/15
 */
public abstract class BaseFragment extends Fragment {

    protected CompositeSubscription _subscriptions = new CompositeSubscription();

    @Inject
    protected PostService postService;

    @Inject
    protected Preferences preferences;

    @Inject
    protected ArticleDao articleDao;

    @Inject
    protected CategoryDao categoryDao;

    @Inject
    protected CategoryArticleDao categoryArticleDao;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getActivity()).component().inject(this);


        if (!preferences.isLoggedIn()) {
            String email = ParseUtils.getEmail(getContext());

            preferences.setEmail(email);

            ParseUtils.subscribeWithEmail(email);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
    }


}
