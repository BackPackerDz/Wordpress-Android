package com.squalala.dz6android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.squalala.dz6android.custom.EndlessRecyclerOnScrollListener;
import com.squalala.dz6android.eventbus.PostActionEvent;
import com.squalala.dz6android.eventbus.PostEvent;
import com.squalala.dz6android.eventbus.QueryEvent;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.greendao.Category;
import com.squalala.dz6android.greendao.CategoryArticle;
import com.squalala.dz6android.model.Post;
import com.squalala.dz6android.utils.ConnectionDetector;
import com.squalala.dz6android.utils.DateUtils;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Back Packer
 * Date : 23/08/15
 */
public class CategoryFragment extends BaseFragment
    implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    EmptyRecyclerView recyclerView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.rotateloading)
    RotateLoading rotateLoading;

    @Bind(R.id.empty_view)
    TextView emptyView;


    private PostAdapter adapter;
    private List<Article> listPost = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private static final int DEFAULT_PAGE = 1;

    private static final String KEY_CATEGORY = "category";

    private EndlessRecyclerOnScrollListener mListener;


    private String category, keyWord;


    private static final String TAG = CategoryFragment.class.getSimpleName();

    public static CategoryFragment newInstance(String category) {

        CategoryFragment fragment = new CategoryFragment();

        Bundle args = new Bundle();
        args.putString(KEY_CATEGORY, category);

        fragment.setArguments(args);

        return fragment;
    }

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

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                R.color.primary,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setEmptyView(emptyView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PostAdapter(listPost);
        recyclerView.setAdapter(adapter);



        initScroll();


    }

    private void initScroll() {

        if(mListener != null)
            recyclerView.removeOnScrollListener(mListener);

        mListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @DebugLog
            @Override
            public void onLoadMore(int current_page) {

                _subscriptions.add(postService.getPosts(current_page, category, null)
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
                                Log.e(TAG, e.getMessage());
                            }

                            @DebugLog
                            @Override
                            public void onNext(List<Post> posts) {


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

                                            System.out.println(category.getSlug());
                                        }



                                        articleDao.insert(articleNew);
                                        listPost.add(articleNew);
                                    }
                                    // L'articl existe déjà dans la DB
                                    else {
                                        // Si il est lu et que l'utilisateur a décide de le cacher
                                        if (article.getStatus() && preferences.isHideReadPost()) {
                                            // nothing
                                        } else if (article.getDeleted()) {

                                        } else
                                            listPost.add(article);
                                    }
                                }

                                //  for (Post post : posts)
                                //     listPost.add(post);

                                recyclerView.getAdapter().notifyDataSetChanged();

                            }
                        }));
            }

        };

        recyclerView.addOnScrollListener(mListener);
    }




    @DebugLog
    @Override
    public void onRefresh() {

        if (ConnectionDetector.isConnectingToInternet(getContext()))
        {

            recyclerView.removeOnScrollListener(mListener);
            listPost.clear();
            recyclerView.getAdapter().notifyDataSetChanged();
            keyWord = null;

            emptyView.setVisibility(View.GONE);

            _subscriptions.add(postService.getPosts(DEFAULT_PAGE, category, null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Post>>() {

                        @DebugLog
                        @Override
                        public void onCompleted() {
                            swipeLayout.setRefreshing(false);

                            initScroll();
                        }

                        @DebugLog
                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }

                        @DebugLog
                        @Override
                        public void onNext(List<Post> posts) {

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

                                        System.out.println(category.getSlug());
                                    }

                                    articleDao.insert(articleNew);
                                    listPost.add(articleNew);
                                }
                                // L'articl existe déjà dans la DB
                                else {
                                    // Si il est lu et que l'utilisateur a décide de le cacher
                                    if (article.getStatus() && preferences.isHideReadPost()) {
                                        // nothing
                                    } else if (article.getDeleted()) {

                                    } else
                                        listPost.add(article);
                                }
                            }

                    //        for (Post post : posts)
                     //           listPost.add(post);

                            recyclerView.getAdapter().notifyDataSetChanged();

                        }
                    }));

        }
        else {
            swipeLayout.setRefreshing(false);
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    getString(R.string.no_conneciton), Snackbar.LENGTH_LONG).show();
        }


    }

    @DebugLog
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        category = getArguments().getString(KEY_CATEGORY);

        _subscriptions.add(postService.getPosts(DEFAULT_PAGE, category, keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Post>>() {

                    @DebugLog
                    @Override
                    public void onCompleted() {
                        rotateLoading.stop();
                        rotateLoading.setVisibility(View.GONE);
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @DebugLog
                    @Override
                    public void onNext(List<Post> posts) {

                        long startTime = System.currentTimeMillis();

                        for (int i = 0; i < posts.size(); i++) {

                            if (i == 0 && category == null)
                                preferences.setLastDatePost(posts.get(i).getDateGMT());

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

                                listPost.add(articleNew);
                            }
                            // L'articl existe déjà dans la DB
                            else {

                                // Si il est lu et que l'utilisateur a décide de le cacher
                                if (article.getStatus() && preferences.isHideReadPost()) {
                                    // nothing
                                } else if (article.getDeleted()) {

                                } else
                                    listPost.add(article);

                            }
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();


                        long endTime = System.currentTimeMillis();


                        System.out.println("That took resize " + (endTime - startTime) + " milliseconds ");
                        System.out.println("That took " + ((endTime - startTime) / 1000) + " seconds");
                    }
                }));


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

    @DebugLog
    public void onEvent(PostActionEvent event) {

        switch (event.getActionType()) {

            case DELETE_ALL_READ_POST:

                for(Iterator<Article> it = listPost.iterator(); it.hasNext();) {

                    Article article = it.next();

                    if (article.getStatus()) {
                        article.setDeleted(true);
                        articleDao.update(article);
                        it.remove();
                    }

                }
/*
                for (Article article : listPost) {
                    if (article.getStatus()) {
                        article.setDeleted(true);
                        listPost.remove(article);
                        articleDao.update(article);
                    }
                }
*/
                break;

            case SET_ALL_POST_READ:

                for(Article article : listPost) {
                    article.setStatus(true);
                    articleDao.update(article);
                }

                break;

            case HIDE_READ_ARTICLES:

                try {
                    Log.e(TAG, "SIZE " + listPost.size());
                    //  preferences.setHideReadPost(true);

                    for(Iterator<Article> it = listPost.iterator(); it.hasNext();) {
                        Article article = it.next();
                        if (article.getStatus()) {
                            it.remove();
                        }
                    }

                    Log.e(TAG, "AFTER SIZE " + listPost.size());
                } catch (Exception e ) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }



                break;

            case DELETE_ALL:



                for(Iterator<Article> it = listPost.iterator(); it.hasNext();) {
                    Article article = it.next();
                    article.setDeleted(true);
                    articleDao.update(article);
                    it.remove();
                }
            /*    for (Article article : listPost) {

                    listPost.remove(article);
                    articleDao.update(article);
                }*/

                break;

        }

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @DebugLog
    public void onEvent(PostEvent event) {
        articleDao.insertOrReplace(event.getArticle());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void onEvent(QueryEvent event) {

        keyWord  = event.getQuery();

        listPost.clear();
        recyclerView.getAdapter().notifyDataSetChanged();

        rotateLoading.setVisibility(View.VISIBLE);
        rotateLoading.start();

        emptyView.setVisibility(View.GONE);

        _subscriptions.add(postService.getPosts(DEFAULT_PAGE, null, event.getQuery())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Post>>() {

                    @DebugLog
                    @Override
                    public void onCompleted() {
                        rotateLoading.stop();
                        rotateLoading.setVisibility(View.GONE);
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                    }

                    @DebugLog
                    @Override
                    public void onNext(List<Post> posts) {

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

                                System.out.println("Avant boucle");

                                // On récupère toutes les catégories de l'articles
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
                                listPost.add(articleNew);
                            }
                            // L'articl existe déjà dans la DB
                            else {
                                // Si il est lu et que l'utilisateur a décide de le cacher
                                if (article.getStatus() && preferences.isHideReadPost()) {
                                    // nothing
                                } else if (article.getDeleted()) {

                                } else
                                    listPost.add(article);
                            }
                        }


                     //   for (Post post : posts)
                     //       listPost.add(post);


                        recyclerView.getAdapter().notifyDataSetChanged();

                    }
                }));
    }





}
