package com.anotap.whatagreatmovie.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anotap.whatagreatmovie.R;
import com.anotap.whatagreatmovie.model.Movie;
import com.anotap.whatagreatmovie.util.TextUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;

public class MovieDetailsFragment extends BaseFragment {
    private static final String KEY_MOVIE = "movie";

    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;

    }

    private Movie movie;

    @BindView(R.id.poster)
    ImageView imagePoster;
    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.textYear)
    TextView textYear;
    @BindView(R.id.textDirector)
    TextView textDirector;
    @BindView(R.id.textStar)
    TextView textStar;
    @BindView(R.id.textDescription)
    TextView textDescription;
    @BindView(R.id.tagsGenres)
    TagGroup tagsGenres;

    @BindView(R.id.labelDirector)
    TextView labelDirector;
    @BindView(R.id.labelStar)
    TextView labelStar;
    @BindView(R.id.labelDescription)
    TextView labelDescription;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        movie = (Movie) getArguments().getSerializable(KEY_MOVIE);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateWithLoadingIndicator(R.layout.fragment_movie_details, container);
        ButterKnife.bind(this, view);

        Picasso.get().load(movie.getThumbnail()).fit().centerCrop().placeholder(R.drawable.placeholder_movie).into(imagePoster);

        textTitle.setText(movie.getName());
        textYear.setText(String.valueOf(movie.getYear()));
        if (TextUtils.isEmpty(movie.getDirector())) {
            textDirector.setVisibility(View.GONE);
            labelDirector.setVisibility(View.GONE);
        } else {
            textDirector.setText(movie.getDirector());
        }
        if (TextUtils.isEmpty(movie.getMainStar())) {
            labelStar.setVisibility(View.GONE);
            textStar.setVisibility(View.GONE);
        } else {
            textStar.setText(movie.getMainStar());
        }
        if (TextUtils.isEmpty(movie.getDescription())) {
            labelDescription.setVisibility(View.GONE);
            textDescription.setVisibility(View.GONE);
        } else {
            textDescription.setText(movie.getDescription());
        }

        String genres[] = new String[movie.getGentres().size()];
        for (int i = 0; i < movie.getGentres().size(); i++) {
            genres[i] = movie.getGentres().get(i).getName();
        }
        tagsGenres.setTags(genres);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle("");
        setShowBackArrow(true);
    }
}
