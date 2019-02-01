package com.anotap.whatagreatmovie.api;

import android.annotation.SuppressLint;

import com.anotap.whatagreatmovie.MovieApp;
import com.anotap.whatagreatmovie.model.Movie;
import com.anotap.whatagreatmovie.model.User;
import com.anotap.whatagreatmovie.util.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Class that handles the results of api calls and passes them to the ui screens
 */

@SuppressLint("CheckResult")
public class ApiManager extends Api {
    private static ApiManager instance;

    private ApiManager() {
        super();
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    public void getUsers(String userName, Consumer<List<User>> responseConsumer, Consumer<Throwable> errorConsumer) {
        RxUtil.networkConsumer(getWebService().getUsers(userName), responseConsumer, errorConsumer);
    }

    public void createUser(User user, Consumer<List<User>> responseConsumer, Consumer<Throwable> errorConsumer) {
        RxUtil.networkConsumer(getWebService().createUser(user), responseConsumer, errorConsumer);
    }

    @SuppressLint("UseSparseArrays")
    public void getMovies(final Consumer<List<Movie>> responseConsumer, Consumer<Throwable> errorConsumer) {
        final HashMap<Integer, Movie> allMovies = new LinkedHashMap<>();

        getWebService().getMovies()
                .flatMap(new Function<List<Movie>, ObservableSource<List<Movie>>>() {
                    @Override
                    public ObservableSource<List<Movie>> apply(List<Movie> movies) throws Exception {
                        for (Movie movie : movies) {
                            allMovies.put(movie.getId(), movie);
                        }
                        return getWebService().getFavorites(MovieApp.getInstance().getUserId());
                    }
                })
                .map(new Function<List<Movie>, List<Movie>>() {
                    @Override
                    public List<Movie> apply(List<Movie> movies) throws Exception {
                        for (Movie favoriteMovie : movies) {
                            if (allMovies.containsKey(favoriteMovie.getId())) {
                                allMovies.get(favoriteMovie.getId()).setListedFavorite(true);
                            }
                        }
                        return movies;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) throws Exception {
                        responseConsumer.accept(new ArrayList<>(allMovies.values()));
                    }
                }, errorConsumer);
    }

    public void setMovieFavorite(int movieId, boolean favorite,
                                 Consumer<List<Movie>> responseConsumer, Consumer<Throwable> errorConsumer) {
        int userId = MovieApp.getInstance().getUserId();
        RxUtil.networkConsumer(favorite ?
                getWebService().favoriteMovie(userId, movieId) :
                getWebService().unfavoriteMovie(userId, movieId), responseConsumer, errorConsumer);
    }
}
