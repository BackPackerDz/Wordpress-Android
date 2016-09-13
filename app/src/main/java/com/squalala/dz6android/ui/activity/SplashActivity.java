package com.squalala.dz6android.ui.activity;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.squalala.dz6android.R;
import com.squalala.dz6android.common.AppConstants;
import com.squalala.dz6android.data.prefs.Preferences;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 10/10/15
 */
public class SplashActivity extends AwesomeSplash {

    private Preferences preferences;

    @Override
    public void initSplash(ConfigSplash configSplash) {

        preferences = new Preferences(this);

        if (!preferences.isSplashScreenViewed()) {
             /* you don't have to override every property */

            //Customize Circular Reveal
            configSplash.setBackgroundColor(R.color.primary); //any color you want form colors.xml
            configSplash.setAnimCircularRevealDuration(2000); //int ms
            configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
            configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

            //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

            //Customize Path
            configSplash.setPathSplash(AppConstants.PATH_LOGO); //set path String
            configSplash.setOriginalHeight(520); //in relation to your svg (path) resource
            configSplash.setOriginalWidth(520); //in relation to your svg (path) resource
            configSplash.setAnimPathStrokeDrawingDuration(3000);
            configSplash.setPathSplashStrokeSize(3); //I advise value be <5
            configSplash.setPathSplashStrokeColor(android.R.color.white); //any color you want form colors.xml
            configSplash.setAnimPathFillingDuration(3000);
            configSplash.setPathSplashFillColor(R.color.white_custom); //path object filling color


            //Customize Title
            configSplash.setTitleSplash(getString(R.string.app_name));
            configSplash.setTitleTextColor(android.R.color.white);
            configSplash.setTitleTextSize(30f); //float value
            configSplash.setAnimTitleDuration(3000);
            configSplash.setAnimTitleTechnique(Techniques.FadeIn);
            //    configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/
        }
        else
            navigateToMainActivity();

    }

    @DebugLog
    @Override
    public void animationsFinished() {
        preferences.setSpashScreen(true);
        navigateToMainActivity();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
