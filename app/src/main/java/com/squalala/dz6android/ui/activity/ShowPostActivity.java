package com.squalala.dz6android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squalala.dz6android.App;
import com.squalala.dz6android.R;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.greendao.ArticleDao;
import com.squalala.dz6android.greendao.Category;
import com.squalala.dz6android.greendao.CategoryArticle;
import com.squalala.dz6android.greendao.CategoryArticleDao;
import com.squalala.dz6android.greendao.CategoryDao;
import com.squalala.dz6android.ui.fragment.ShowPostFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.QueryBuilder;
import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 24/08/15
 */
public class ShowPostActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Inject
    protected ArticleDao articleDao;

    @Inject
    protected CategoryDao categoryDao;

    @Inject
    protected CategoryArticleDao categoryArticleDao;

    @Bind(R.id.pager)
    ViewPager mPager;

    private String linkToShare;
    private boolean isNotification;

    private Long articleId;


    private MenuItem favorisMenuItem;

    private PagerAdapter mPagerAdapter;

    private List<Long> articleIds = new ArrayList<>();

    private int defaultPositionArticle;


    @DebugLog
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_posts);

        App.get(this).component().inject(this);

        App.tracker.setScreenName("ShowPost");

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        articleId = getIntent().getLongExtra("id", 0);

        // On récupère l'article
        Article article = articleDao.load(articleId);

        boolean favoris = getIntent().getBooleanExtra("favoris", false);

        boolean isAllCategorie = getIntent().getBooleanExtra("all_category", false);

        String category = getIntent().getStringExtra("category");

        System.out.println("All cat " + isAllCategorie);

        if (favoris)
        {
            QueryBuilder query = articleDao.queryBuilder()
                    .where(ArticleDao.Properties.Favoris.eq(true));

            List<Article> articles = query.list();

            for (Article article_ : articles)
                articleIds.add(article_.getId());
        }
        else if (isAllCategorie)
        {
            for(Article article_ : articleDao.loadAll())
                articleIds.add(article_.getId());
        }
        else
        {
            // On récupère l'id de la catégorie de l'article
            // Pour pouvoir ensuite récupérer les autres articles
            // de la meme catégorie
            List<Category> categories = categoryDao.queryBuilder()
                    .where(CategoryDao.Properties.Slug.eq(category))
                    .list();

            System.out.println(categories.get(0).getName() + " check size cat " + categories.size()
            );

            Long categorieId = categories.get(0).getId();

            List<CategoryArticle> categoryArticles = categoryArticleDao._queryCategory_Categories(categorieId);

            for (CategoryArticle categoryArticle : categoryArticles) {
                if (!categoryArticle.getArticle().getDeleted())
                    articleIds.add(categoryArticle.getArticleId());
            }

        }

        System.out.println(articleIds);

        linkToShare = article.getLink();

        isNotification = getIntent().getBooleanExtra("notification", false);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        defaultPositionArticle = articleIds.indexOf(articleId);

        System.out.println("POSITION ITEM : " + defaultPositionArticle);

        mPager.setCurrentItem(defaultPositionArticle);
    }


    @Override
    public void onBackPressed() {

        System.out.println(mPager.getCurrentItem() + " current "  + defaultPositionArticle);
        if (mPager.getCurrentItem() == defaultPositionArticle) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            if (isNotification) {
                Intent intent = new Intent(this, com.squalala.dz6android.ui.activity.MainActivity.class);
                startActivity(intent);
                finish();
            }

            super.onBackPressed();

        } else {


            // Otherwise, select the previous step.
            mPager.setCurrentItem(defaultPositionArticle);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            reloadMenu(mPager.getCurrentItem());

            markAsRead(mPager.getCurrentItem());

            return ShowPostFragment.newInstance(articleIds.get(position));
        }


        @Override
        public int getCount() {
            return articleIds.size();
        }
    }

    @DebugLog
    private void reloadMenu(int position) {

        if (favorisMenuItem != null)
        {
            Article article = articleDao.load(articleIds.get(position));

            linkToShare = article.getLink();

            if (article.getFavoris()) {
                favorisMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_white_36dp));
            }
            else {
                favorisMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_border_white_36dp));
            }
        }
    }

    private void markAsRead(int position) {

        Article article = articleDao.load(articleIds.get(position));

        article.setStatus(true);

        articleDao.update(article);

    }

    @DebugLog
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        favorisMenuItem = menu.findItem(R.id.action_favoris);

        Article article = articleDao.load(articleId);

        if (article.getFavoris())
            favorisMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_white_36dp));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (isNotification) {
                    Intent intent = new Intent(this, com.squalala.dz6android.ui.activity.MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    NavUtils.navigateUpFromSameTask(this);

                return true;

            case R.id.action_share:

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, linkToShare);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Jette un coup d'oeil sur cette article intéressant !");
                startActivity(Intent.createChooser(intent, "Share"));

                return true;

            case R.id.action_favoris:

                Article article = articleDao.load(articleIds.get(mPager.getCurrentItem()));

                article.setFavoris(!article.getFavoris());

                articleDao.update(article);

                if (article.getFavoris()) {
                    favorisMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_white_36dp));
                    Snackbar.make(this.findViewById(android.R.id.content),
                            getString(R.string.article_ajouter), Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(this.findViewById(android.R.id.content),
                            getString(R.string.article_retirer), Snackbar.LENGTH_LONG).show();
                    favorisMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_border_white_36dp));
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }




    public interface PostListener {
        void onSelectPost(Article post);
    }









}
