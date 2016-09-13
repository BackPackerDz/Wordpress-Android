package com.squalala.dz6android.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.squalala.dz6android.R;

/**
 * Created by Back Packer
 * Date : 29/09/15
 */
public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
