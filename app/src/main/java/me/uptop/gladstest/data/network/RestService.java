package me.uptop.gladstest.data.network;

import me.uptop.gladstest.data.network.res.CategoriesResponse;
import me.uptop.gladstest.data.network.res.PostsResponse;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RestService {
    @GET("categories/{category}/posts")
    Observable<Response<PostsResponse>> getPosts(@Path("category") String category, @Query("access_token") String accessToken);

    @GET("categories")
    Observable<CategoriesResponse> getCategories(@Query("access_token") String accessToken);
}
