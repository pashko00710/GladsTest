package me.uptop.gladstest.data.network;

import java.util.List;

import me.uptop.gladstest.data.network.res.CategoriesResponse;
import me.uptop.gladstest.data.network.res.PostsResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RestService {
    @GET("categories/{category}/posts")
    Observable<List<PostsResponse>> getPosts(@Path("category") String category, @Query("access_token") String accessToken);

    @GET("categories")
    Observable<List<CategoriesResponse>> getCategories(@Query("access_token") String accessToken);
}
