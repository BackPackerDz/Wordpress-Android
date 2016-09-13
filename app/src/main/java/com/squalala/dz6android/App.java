package com.squalala.dz6android;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dz6android.utils.ParseUtils;
import com.squareup.leakcanary.LeakCanary;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication {

    private AppComponent component;

    public static GoogleAnalytics analytics;
    public static Tracker tracker;


    @DebugLog
    @Override public void onCreate() {
        super.onCreate();

        setupGraph();

        setupAnalytics();

        Fabric.with(this, new Crashlytics());

        LeakCanary.install(this);

        ParseUtils.registerParse(this);
    }


    private void setupAnalytics() {
        analytics = GoogleAnalytics.getInstance(this);

        tracker = analytics.newTracker(getString(R.string.id_google_analytics));

        // Cela permet de pouvoir récupérer les erreurs
        tracker.enableExceptionReporting(true);

        /**
         *  Pouvoir récupérer des données démographiques et leurs centre d'intêrets
         */
        tracker.enableAdvertisingIdCollection(true);

        tracker.enableAutoActivityTracking(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public AppComponent component() {
        return component;
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        component.inject(this);
    }

}
