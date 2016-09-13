package com.squalala.dz6android.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.squalala.dz6android.App;
import com.squalala.dz6android.R;
import com.squalala.dz6android.data.prefs.Preferences;
import com.squalala.dz6android.eventbus.PostActionEvent;
import com.squalala.dz6android.eventbus.PostEvent;
import com.squalala.dz6android.eventbus.QueryEvent;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.ui.fragment.AboutFragment;
import com.squalala.dz6android.ui.fragment.CategoryFragment;
import com.squalala.dz6android.ui.fragment.FavorisFragment;
import com.squalala.dz6android.utils.NoteAppReminder;
import com.squalala.dz6android.utils.ParseUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class MainActivity extends AppCompatActivity
    implements ShowPostActivity.PostListener {

    private Drawer drawer;

    private final static String TAG = MainActivity.class.getSimpleName();

    private String [] categoryTags, categoryNames;

    private NoteAppReminder noteAppReminder;

    private boolean exit = false;

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUtils.verifyParseConfiguration(this);


        categoryNames = getResources().getStringArray(R.array.categories_names);
        categoryTags = getResources().getStringArray(R.array.categories_tags);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .withHeaderBackground(R.drawable.logo)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(categoryNames[0]).withIcon(FontAwesome.Icon.faw_clock_o).withIdentifier(1),
                        new PrimaryDrawerItem().withName(categoryNames[1]).withIcon(FontAwesome.Icon.faw_newspaper_o).withIdentifier(2),
                        new PrimaryDrawerItem().withName(categoryNames[2]).withIcon(GoogleMaterial.Icon.gmd_shop).withIdentifier(3),
                        new PrimaryDrawerItem().withName(categoryNames[3]).withIcon(MaterialDesignIconic.Icon.gmi_lamp).withIdentifier(4),
                        new PrimaryDrawerItem().withName(categoryNames[4]).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(5),
                        new PrimaryDrawerItem().withName(categoryNames[5]).withIcon(GoogleMaterial.Icon.gmd_build).withIdentifier(6),
                        new PrimaryDrawerItem().withName(categoryNames[6]).withIcon(FontAwesome.Icon.faw_star).withIdentifier(7),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(categoryNames[7]).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(8),
                        new PrimaryDrawerItem().withName(categoryNames[8]).withIcon(FontAwesome.Icon.faw_envelope).withIdentifier(9),
                        new PrimaryDrawerItem().withName(categoryNames[9]).withIcon(FontAwesome.Icon.faw_play).withIdentifier(10),
                        new PrimaryDrawerItem().withName(categoryNames[10]).withIcon(FontAwesome.Icon.faw_info_circle).withIdentifier(11)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        if (drawerItem != null && drawerItem instanceof Nameable) {

                            App.tracker.setScreenName(categoryNames[drawerItem.getIdentifier() - 1]);

                            if (drawerItem.getIdentifier() < 8) {

                                if (drawerItem.getIdentifier() == 7)
                                {
                                    getSupportActionBar().setTitle(((Nameable) drawerItem).getName().toString());
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            new FavorisFragment()).commit();
                                }
                                else if (drawerItem.getIdentifier() == 1)
                                {
                                    getSupportActionBar().setTitle(((Nameable) drawerItem).getName().toString());
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            CategoryFragment.newInstance(null)).commit();
                                }
                                else
                                {
                                    getSupportActionBar().setTitle(((Nameable) drawerItem).getName().toString());
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            CategoryFragment.newInstance(categoryTags[drawerItem.getIdentifier() - 1])).commit();
                                }

                            }
                            else {

                                switch (drawerItem.getIdentifier()) {

                                    case 8:

                                        App.tracker.setScreenName(getString(R.string.reglage));

                                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                                        startActivity(intent);

                                        break;

                                    case 9:

                                        App.tracker.send(new HitBuilders.EventBuilder()
                                                .setCategory("UX")
                                                .setAction("Clique")
                                                .setLabel("Contact")
                                                .build());

                                        Intent email = new Intent(Intent.ACTION_SEND);
                                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@dz-android.com"});
                                        email.putExtra(Intent.EXTRA_SUBJECT, "Android-Dz");
                                        email.putExtra(Intent.EXTRA_TEXT, "");
                                        email.setType("message/rfc822");
                                        startActivity(Intent.createChooser(email, "Choisissez :"));

                                        break;

                                    case 10:

                                        String appPackageName2 = getPackageName();
                                        Intent marketIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName2));
                                        marketIntent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                        startActivity(marketIntent2);

                                        break;

                                    case 11:

                                        getSupportActionBar().setTitle(((Nameable) drawerItem).getName().toString());
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                new AboutFragment()).commit();


                                        break;
                                }


                            }


                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        drawer.setSelection(1);


        noteAppReminder = new NoteAppReminder(this, new Preferences(this));
    }

    @Override
    public void onSelectPost(Article post) {

        Intent intent = new Intent(this, ShowPostActivity.class);
        intent.putExtra("id", post.getId());

       // Log.e(TAG, "cat " + categoryNames[drawer.getCurrentSelectedPosition() - 1]);

        // 8 c'est la position des favoris
        if (drawer.getCurrentSelectedPosition() - 1 == 6)
        {
            intent.putExtra("favoris", true);
        }
        else
        {
            intent.putExtra("category", categoryTags[drawer.getCurrentSelectedPosition() - 1]);

            Log.e(TAG, "cat " + categoryNames[drawer.getCurrentSelectedPosition() - 1]);

            if (categoryNames[drawer.getCurrentSelectedPosition() - 1].equals("Dernières 24h"))
                intent.putExtra("all_category", true);
        }

        startActivity(intent);

        // On le met en lus
        post.setStatus(true);

        // Et on notifie notre CategoryFragment
        EventBus.getDefault().post(new PostEvent(post));

    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAppReminder.checkMomentNoteApp();
    }

    @DebugLog
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent();
        intent.setAction("com.squalala.dz6android_ACTION");
        sendBroadcast(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {

            if (exit) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, getString(R.string.appuyer_quitter),
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                EventBus.getDefault().post(new QueryEvent(query));

                invalidateOptionsMenu();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_cacher_lus:

                EventBus.getDefault().post(new PostActionEvent(PostActionEvent.ActionType.HIDE_READ_ARTICLES));

                break;

            case R.id.action_tout_marque_comme_lu:

                EventBus.getDefault().post(new PostActionEvent(PostActionEvent.ActionType.SET_ALL_POST_READ));

                break;

            case R.id.action_tout_supprimer:

                EventBus.getDefault().post(new PostActionEvent(PostActionEvent.ActionType.DELETE_ALL));

                break;

            case R.id.action_supprimer_tous_les_lu:

                EventBus.getDefault().post(new PostActionEvent(PostActionEvent.ActionType.DELETE_ALL_READ_POST));

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
