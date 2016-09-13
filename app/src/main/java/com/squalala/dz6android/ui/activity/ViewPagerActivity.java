package com.squalala.dz6android.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.squalala.dz6android.App;
import com.squalala.dz6android.R;
import com.squalala.dz6android.custom.HackyViewPager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Back Packer
 * Date : 25/04/15
 */
public class ViewPagerActivity extends AppCompatActivity {


    private static final String ISLOCKED_ARG = "isLocked";

    @Bind(R.id.view_pager) HackyViewPager mViewPager;

    @Bind(R.id.tool_bar)
    Toolbar mToolbar;

    private List<String> urlImages;

    private static final String TAG = ViewPagerActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.tracker.setScreenName(getClass().getName());

        setContentView(R.layout.activity_view_pager_images);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        // Position sur laquelle l'utilisateur a cliqué.
        int position = getIntent().getExtras().getInt("position");

        urlImages = getIntent().getStringArrayListExtra("urls");

        if (urlImages.size() > 1)
            getSupportActionBar().setTitle( (position + 1) + "/" + urlImages.size());
        else
            getSupportActionBar().setTitle("");

        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(position);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle((position + 1) + "/" + urlImages.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return urlImages.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            Log.e(TAG, "instantiateItem");

            PhotoView photoView = new PhotoView(container.getContext());

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            Picasso.with(ViewPagerActivity.this)
                    .load(urlImages.get(position).toString())
                    .fit().centerInside()
                    .into(photoView);


            return photoView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }


    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

}