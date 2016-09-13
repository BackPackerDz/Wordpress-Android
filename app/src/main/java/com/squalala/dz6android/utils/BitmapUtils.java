package com.squalala.dz6android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : BitmapUtils.java
 * Date : 27 nov. 2014
 * 
 */
public class BitmapUtils {
	
	public static Bitmap getBitmapFromURL(String strURL) {
	    try { 
	    	
	    	OkHttpClient client = new OkHttpClient();
	    	
	    	Request request = new Request.Builder()
	        .url(strURL) 
	        .build(); 
	    	
			Response response = client.newCall(request).execute();
	        
	        InputStream input = response.body().byteStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        
	        return myBitmap;
	        
	    } catch (IOException e) {
	    	
	        e.printStackTrace();
	        return null; 
	    } 
	}


	public static Bitmap takeScreenshot(Context context) {
		View rootView = ( (Activity) context).findViewById(android.R.id.content).getRootView();
		rootView.setDrawingCacheEnabled(true);
		return rootView.getDrawingCache();
	}

	public static Bitmap takeScreenshot(View view) {
		View rootView = view;
		rootView.setDrawingCacheEnabled(true);
		return rootView.getDrawingCache();
	}

}
