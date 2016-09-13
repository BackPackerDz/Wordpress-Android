package com.squalala.dz6android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squalala.dz6android.R;
import com.squalala.dz6android.common.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 30/09/15
 */
public class AboutFragment extends BaseFragment {

    @Bind(R.id.txt_about)
    TextView txtAbout;

    @Bind(R.id.txt_squalala)
    TextView txtSqualala;

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        txtAbout.setText(Html.fromHtml(getString(R.string.about)));
        txtSqualala.setText(Html.fromHtml(getString(R.string.developped_by)));

        txtSqualala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@squalala.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Team Squalala DZ");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choisissez :"));
            }
        });
    }


}
