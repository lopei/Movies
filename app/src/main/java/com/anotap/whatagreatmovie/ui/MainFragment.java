package com.anotap.whatagreatmovie.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.anotap.whatagreatmovie.MovieApp;
import com.anotap.whatagreatmovie.R;
import com.anotap.whatagreatmovie.api.ApiManager;
import com.anotap.whatagreatmovie.model.Movie;
import com.anotap.whatagreatmovie.util.FragmentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainFragment extends BaseFragment implements TabLayout.BaseOnTabSelectedListener, MoviesAdapter.OnMovieClickListener {
    private static final int TAB_MOVIES = 0;
    private static final int TAB_FAVORITES = 1;

    private MoviesAdapter adapter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateWithLoadingIndicator(R.layout.fragment_main, container);
        ButterKnife.bind(this, view);

        tabLayout.addTab(tabLayout.newTab().setTag(TAB_MOVIES).setText(R.string.movies));
        tabLayout.addTab(tabLayout.newTab().setTag(TAB_FAVORITES).setText(R.string.favorites));

        tabLayout.addOnTabSelectedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchMoviesFromApi(TAB_MOVIES);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem logout = menu.add(R.string.logout);
        logout.setIcon(R.drawable.ic_logout);
        logout.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MovieApp.getInstance().setUser(null);
        FragmentUtil.replaceFragment(getFragmentManager(), new LoginFragment(), false);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle(String.format(Locale.getDefault(), "%s %s", getString(R.string.welcome),
                MovieApp.getInstance().getUsername()));
        setShowBackArrow(false);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int tag = (int) tab.getTag();

        updateMovies(tag);
    }

    private void updateMovies(int tag) {
        if (adapter != null) {
            adapter.setFavoritesOnly(tag == TAB_FAVORITES);
        } else {
            fetchMoviesFromApi(tag);
        }
    }

    private void fetchMoviesFromApi(final int tag) {
        if (adapter == null) {
            setLoading(true);
            ApiManager.getInstance().getMovies(new Consumer<List<Movie>>() {
                @Override
                public void accept(List<Movie> movies) throws Exception {
                    setLoading(false);
                    adapter = new MoviesAdapter(movies);
                    adapter.setOnMovieClickListener(MainFragment.this);
                    recyclerView.setAdapter(adapter);
                    updateMovies(tag);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    setLoading(false);
                    throwable.printStackTrace();
                    showErrorMessage(throwable.getMessage());
                }
            });
        } else {
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onMovieClick(Movie movie) {
        FragmentUtil.replaceFragment(getFragmentManager(), MovieDetailsFragment.newInstance(movie));
    }

    @Override
    public void onMovieFavoriteClick(final Movie movie) {
        movie.setListedFavorite(!movie.isListedFavorite());
        ApiManager.getInstance().setMovieFavorite(movie.getId(), movie.isListedFavorite(), new Consumer<List<Movie>>() {
            @Override
            public void accept(List<Movie> movies) throws Exception {
                adapter.updateMovie(movie);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }
}
