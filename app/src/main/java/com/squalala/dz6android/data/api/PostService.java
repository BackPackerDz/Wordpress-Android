package com.squalala.dz6android.data.api;


import com.squalala.dz6android.common.AppConstants;
import com.squalala.dz6android.model.Post;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Back Packer
 * Date : 22/08/15
 */
public interface PostService {

    @GET("/posts?filter[posts_per_page]=" + AppConstants.LIMIT_NUMBER_ARTICLES)
    Observable<List<Post>> getPosts(@Query("page") int page,
                                    @Query("filter[category_name]") String category,
                                    @Query("filter[s]") String keyWord);





}
