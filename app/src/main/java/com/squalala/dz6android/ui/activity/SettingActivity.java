package com.squalala.dz6android.ui.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.squalala.dz6android.R;
import com.squalala.dz6android.ui.fragment.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Back Packer
 * Date : 29/09/15
 */
public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.reglage));

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        FragmentTransaction mFragmentTransaction = getFragmentManager()
                .beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, new SettingsFragment());
        mFragmentTransaction.commit();
    }


}
