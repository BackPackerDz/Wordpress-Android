package com.squalala.dz6android.data.api;

import android.app.Application;
import android.util.Log;

import com.squalala.dz6android.common.AppConstants;
import com.squalala.dz6android.utils.ConnectionDetector;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hugo.weaving.DebugLog;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ApiModule.java
 * Date : 21 juin 2014
 * 
 */
@Module
public final class ApiModule {

	private static final String TAG = ApiModule.class.getSimpleName();

    @Provides
    @Singleton Endpoint provideEndpoint() {
	  return Endpoints.newFixedEndpoint(AppConstants.API_URL);
    }

	@Provides
	@Singleton
	  RestAdapter provideRestAdapter(Endpoint endpoint, final Application app) {

		  int cacheSize = 10 * 1024 * 1024; // 10 MiB
		  File cacheDirectory = new File(app.getCacheDir().getAbsolutePath(), "HttpCache");

		  Cache cache = new Cache(cacheDirectory, cacheSize);

		  OkHttpClient client = new OkHttpClient();

		  client.setCache(cache);
		  client.setConnectTimeout(5, TimeUnit.MINUTES);
		  client.setReadTimeout(5, TimeUnit.MINUTES);
	//	  client.setReadTimeout(60, TimeUnit.SECONDS);    // socket timeout

		  return new RestAdapter.Builder()
			.setLogLevel(RestAdapter.LogLevel.BASIC)
			.setEndpoint(endpoint)
			.setRequestInterceptor(new RequestInterceptor() {
				@Override
				public void intercept(RequestFacade request) {

					request.addHeader("Accept", "application/json;versions=1");

					if (ConnectionDetector.isConnectingToInternet(app)) {
						Log.e(TAG, "Pas de cache");
						int maxAge = 60; // read from cache for 1 minute
						request.addHeader("Cache-Control", "public, max-age=" + maxAge);
					}
                    else {
						Log.e(TAG, "Utilisation du cache");
						int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
						request.addHeader("Cache-Control",
								"public, only-if-cached, max-stale=" + maxStale);
					}
				}
			})
			.setClient(new OkClient(client))
			.build();
	  }

	@DebugLog
	@Provides
	@Singleton
	PostService providePostService(RestAdapter restAdapter) {
		return restAdapter.create(PostService.class);
	}


}
