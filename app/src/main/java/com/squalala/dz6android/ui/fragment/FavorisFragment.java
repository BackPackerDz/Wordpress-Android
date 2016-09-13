package com.squalala.dz6android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squalala.dz6android.R;
import com.squalala.dz6android.adapter.PostAdapter;
import com.squalala.dz6android.common.BaseFragment;
import com.squalala.dz6android.custom.EmptyRecyclerView;
import com.squalala.dz6android.eventbus.QueryEvent;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.greendao.ArticleDao;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 05/10/15
 */
public class FavorisFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    EmptyRecyclerView recyclerView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.rotateloading)
    RotateLoading rotateLoading;

    @Bind(R.id.empty_view)
    TextView emptyView;


    private static final String TAG = FavorisFragment.class.getSimpleName();
    private PostAdapter adapter;
    private List<Article> listPost = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @DebugLog
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        rotateLoading.setVisibility(View.VISIBLE);
        rotateLoading.start();


        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setEmptyView(emptyView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PostAdapter(listPost);
        recyclerView.setAdapter(adapter);

        swipeLayout.setEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();

        listPost.clear();

        QueryBuilder query = articleDao.queryBuilder();
        query.where(ArticleDao.Properties.Favoris.eq(true));

        listPost.addAll(query.list());

        recyclerView.getAdapter().notifyDataSetChanged();

        rotateLoading.stop();
        rotateLoading.setVisibility(View.GONE);
    }

    @DebugLog
    public void onEvent(QueryEvent event) {

        rotateLoading.setVisibility(View.VISIBLE);
        rotateLoading.start();

        String keyWord  = event.getQuery();
        Log.e(TAG, " " + keyWord);

        listPost.clear();

        WhereCondition where1 = ArticleDao.Properties.Content.like("%" + keyWord + "%");
        WhereCondition where2 = ArticleDao.Properties.Title.like("%" +keyWord+ "%");
        WhereCondition where3 = ArticleDao.Properties.Favoris.eq(true);

        QueryBuilder query = articleDao.queryBuilder();

        query.LOG_SQL = true;

        query.where(where3, query.or(where2, where1));

        listPost.addAll(query.list());

        recyclerView.getAdapter().notifyDataSetChanged();

        rotateLoading.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
