package com.squalala.dz6android.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.squalala.dz6android.ui.widget.ListProvider;

/**
 * Created by Back Packer
 * Date : 13/10/15
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }


}
