package com.anotap.whatagreatmovie.api;

import com.anotap.whatagreatmovie.model.Movie;
import com.anotap.whatagreatmovie.model.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Class that lists all api-calls
 */

public interface WebService {

    @GET("/api/users")
    Observable<List<User>> getUsers(@Query("username") String userName);

    @POST("/api/users.json")
    Observable<List<User>> createUser(@Body User user);

    @GET("/api/movies")
    Observable<List<Movie>> getMovies();


    @GET("/api/users/{id}/movies")
    Observable<List<Movie>> getFavorites(@Path("id") int id);

    @GET("/api/users/{id}/movies/{movieId}/favorite")
    Observable<List<Movie>> favoriteMovie(@Path("id") int id, @Path("movieId") int movieId);

    @GET("/api/users/{id}/movies/{movieId}/unfavorite")
    Observable<List<Movie>> unfavoriteMovie(@Path("id") int id, @Path("movieId") int movieId);

}
